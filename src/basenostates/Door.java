package basenostates;

import basenostates.requests.RequestReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents a door and its actions
public class Door {
  private final String id;
  private boolean closed; // physically
  private State state;
  private Area areaTo;
  private static final Logger logger = LoggerFactory.getLogger(Door.class);

  public Door(String id) {
    this.id = id;
    closed = true;
    setState(new Unlocked(this));
  }

  public Door(String id, Area areaFrom, Area areaTo) {
    this.id = id;
    this.areaTo = areaTo;
    this.areaTo.getDoorsGivingAccess();
    closed = true;
    setState(new Unlocked(this));
  }

  public void processRequest(RequestReader request) {
    // it is the Door that process the request because the door has and knows
    // its state, and if closed or open
    if (request.isAuthorized()) {
      String action = request.getAction();
      doAction(action);
    } else {
      logger.warn("Door {}: not authorized", id);
    }
    request.setDoorStateName(getStateName());
  }

  private void doAction(String action) {
    logger.debug("Door {}: processing action {}", id, action);

    switch (action) {
      case Actions.OPEN:
        if (closed && ((this.getStateName().equals("unlocked"))
            || (this.getStateName().equals("unlocked_shortly")))) {
          closed = false;
          logger.info("Door {} opened", id);
        } else {
          logger.warn("Can't open door {} because it's already open or locked", id);
        }
        break;
      case Actions.CLOSE:
        if (closed) {
          logger.warn("Can't close door {} because it's already closed", id);
        } else {
          closed = true;
          logger.info("Door {} closed", id);

          // If it is in the propped state and is closed, return to locked
          if (getStateName().equals("propped")) {
            logger.info("Door was PROPPED - returning to LOCKED state after close");
            setState(new Locked(this));
          }
        }
        break;
      case Actions.LOCK:
        state.lock();
        break;
      case Actions.UNLOCK:
        state.unlock();
        break;
      case Actions.UNLOCK_SHORTLY:
        state.unlockShortly();
        logger.debug("Unlock shortly action processed for door {}", id);
        break;
      default:
        assert false : "Unknown action " + action;
        System.exit(-1);
    }

    logger.debug("Door {} state after action: {}, closed: {}", id, getStateName(), closed);
  }

  public boolean isClosed() {
    return closed;
  }

  public String getId() {
    return id;
  }

  public String getStateName() {
    return state.getState();
  }

  public Area getAreaTo() {
    return areaTo;
  }

  @Override
  public String toString() {
    return "Door{"
        + "id='" + id + '\''
        + ", closed=" + closed
        + ", state=" + getStateName()
        + "}";
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("state", getStateName());
    json.put("closed", closed);
    return json;
  }

  public void setState(State estado) {
    logger.debug("Door {}: changing state from {} to {}", id,
        (this.state != null ? this.state.getState() : "null"), estado.getState());
    this.state = estado;
    this.state.setDoor(this);
    state.intClock();
  }
}