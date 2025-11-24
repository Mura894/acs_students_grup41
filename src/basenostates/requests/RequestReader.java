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

  // Constructor for door access request
  public RequestReader(String credential, String action, LocalDateTime now, String doorId) {
    this.credential = credential;
    this.action = action;
    this.doorId = doorId;
    reasons = new ArrayList<>();
    this.now = now;
  }

  // Get the timestamp of the request
  public LocalDateTime getNow() {
    return now;
  }

  // Get the credential used for the request
  public String getCredential() {
    return credential;
  }

  // Set the current state name of the door
  public void setDoorStateName(String name) {
    doorStateName = name;
  }

  // Set whether the door is closed or not
  public void setDoorClosed(boolean closed) {
    doorClosed = closed;
  }

  // Get the action requested (lock/unlock)
  public String getAction() {
    return action;
  }

  // Check if the request is authorized
  public boolean isAuthorized() {
    return authorized;
  }

  // Add a reason for authorization denial
  public void addReason(String reason) {
    reasons.add(reason);
  }

  // Methods to access door states (if you need them)
  public String getDoorStateName() {
    return doorStateName;
  }

  // Check if the door is closed
  public boolean isDoorClosed() {
    return doorClosed;
  }

  // String representation of the request
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

  // Convert the request response to JSON format
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

    // Find user by credential and door by ID
    User user = DirectoryUsers.getInstance().findUserByCredential(credential);
    Door door = DirectoryDoors.getInstance().findDoorById(doorId);

    // Check if door exists
    if (door == null) {
      logger.error("ERROR: Door {} not found", doorId);
      authorized = false;
      addReason("Door not found");
      return;
    }

    assert door != null : "door " + doorId + " not found";
    // Authorize the user for this request
    authorize(user, door);
    // this sets the boolean authorize attribute of the request
    // Process the request through the door
    door.processRequest(this);
    // even if not authorized we process the request, so that if desired we could log all
    // the requests made to the server as part of processing the request
    // Update door closed status
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

      // Get the destination area of the door
      Area areaTo = door.getAreaTo();
      if (areaTo == null) {
        logger.warn("Warning: Door {} has no destination area", doorId);
        authorized = true; // Temporary for testing
      } else {
        // Verify permissions using the user's group
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