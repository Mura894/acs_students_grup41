package basenostates.requests;

import basenostates.Area;
import basenostates.DirectoryDoors;
import basenostates.DirectoryUsers;
import basenostates.Door;
import basenostates.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestReader implements Request {
  private final String credential; // who
  private final String action;     // what
  private final LocalDateTime now; // when
  private final String doorId;     // where
  private String userName;
  private boolean authorized;
  private final ArrayList<String> reasons; // why not authorized
  private String doorStateName;
  private boolean doorClosed;
  private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);

  public RequestReader(String credential, String action, LocalDateTime now, String doorId) {
    this.credential = credential;
    this.action = action;
    this.doorId = doorId;
    reasons = new ArrayList<>();
    this.now = now;
  }

  public LocalDateTime getNow() {
    return now;
  }

  public String getCredential() {
    return credential;
  }

  public void setDoorStateName(String name) {
    doorStateName = name;
  }

  public void setDoorClosed(boolean closed) {
    doorClosed = closed;
  }

  public String getAction() {
    return action;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void addReason(String reason) {
    reasons.add(reason);
  }

  // Métodos para acceder a los estados de la puerta (si los necesitas)
  public String getDoorStateName() {
    return doorStateName;
  }

  public boolean isDoorClosed() {
    return doorClosed;
  }

  @Override
  public String toString() {
    if (userName == null) {
      userName = "unknown";
    }
    return "Request{"
        + "credential=" + credential
        + ", userName=" + userName
        + ", action=" + action
        + ", now=" + now
        + ", doorID=" + doorId
        + ", closed=" + doorClosed
        + ", authorized=" + authorized
        + ", reasons=" + reasons
        + "}";
  }

  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("authorized", authorized);
    json.put("action", action);
    json.put("doorId", doorId);
    json.put("closed", doorClosed);
    json.put("state", doorStateName);
    json.put("reasons", new JSONArray(reasons));
    return json;
  }

  // see if the request is authorized and put this into the request, then send it to the door.
  // if authorized, perform the action.
  public void process() {
    logger.debug("Processing reader request - Door: {}, Action: {}, User: {}",
        doorId, action, credential);

    User user = DirectoryUsers.getInstance().findUserByCredential(credential);
    Door door = DirectoryDoors.getInstance().findDoorById(doorId);

    if (door == null) {
      logger.error("ERROR: Door {} not found", doorId);
      authorized = false;
      addReason("Door not found");
      return;
    }

    assert door != null : "door " + doorId + " not found";
    authorize(user, door);
    // this sets the boolean authorize attribute of the request
    door.processRequest(this);
    // even if not authorized we process the request, so that if desired we could log all
    // the requests made to the server as part of processing the request
    doorClosed = door.isClosed();

    logger.debug("Request processed - Authorized: {}, Final state: {}",
        authorized, doorStateName);
  }

  // the result is put into the request object plus, if not authorized, why not,
  // only for testing
  private void authorize(User user, Door door) {
    if (user == null) {
      authorized = false;
      addReason("User doesn't exist");
      logger.warn("Authorization failed: user not found");
    } else {
      logger.debug("Authorizing user: {} for door: {}", user.getUsername(), doorId);

      // Obtener el área de destino de la puerta
      Area areaTo = door.getAreaTo();
      if (areaTo == null) {
        logger.warn("Warning: Door {} has no destination area", doorId);
        authorized = true; // Temporal para testing
      } else {
        // Verificar permisos usando el grupo del usuario
        authorized = user.canAccess(areaTo, getAction(), getNow());
        if (!authorized) {
          addReason(user.getReasonMessage());
          logger.warn("Authorization failed: {}", user.getReasonMessage());
        } else {
          logger.info("Authorization granted for user: {}", user.getUsername());
        }
      }
    }
  }
}