package baseNoStates;

import baseNoStates.requests.RequestReader;
import baseNoStates.requests.RequestRefresh;
import org.json.JSONObject;

import java.time.LocalDateTime;

//Class that represents a door and its actions
public class Door {
  private final String id;
  private boolean closed; // physically
  private State state;
  private Area AreaTo;

  public Door(String id) {
    this.id = id;
    closed = true;
    setState(new Unlocked(this));
  }

  public Door(String id, Area AreaFrom, Area AreaTo) {
    this.id = id;
    this.AreaTo = AreaTo;
    this.AreaTo.getDoorsGivingAccess();
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
      System.out.println("Door " + id + ": not authorized");
    }
    request.setDoorStateName(getStateName());
  }

  private void doAction(String action) {
    System.out.println("Door " + id + ": processing action " + action);

    switch (action) {
      case Actions.OPEN:
        if (closed && ((this.getStateName().equals("unlocked")) || (this.getStateName().equals("unlocked_shortly")))) {
          closed = false;
          System.out.println("Door " + id + " opened");
        } else {
          System.out.println("Can't open door " + id + " because it's already open or locked");
        }
        break;
      case Actions.CLOSE:
        if (closed) {
          System.out.println("Can't close door " + id + " because it's already closed");
        } else {
          closed = true;
          System.out.println("✅ Door " + id + " closed");

          // If it is in the propped state and is closed, return to locked
          if (getStateName().equals("propped")) {
            System.out.println("Door was PROPPED - returning to LOCKED state after close");
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
        System.out.println("Unlock shortly action processed for door " + id);
        break;
      default:
        assert false : "Unknown action " + action;
        System.exit(-1);
    }

    System.out.println("Door " + id + " state after action: " + getStateName() + ", closed: " + closed);
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
    return AreaTo;
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
    System.out.println("Door " + id + ": changing state from " +
        (this.state != null ? this.state.getState() : "null") +
        " to " + estado.getState());
    this.state = estado;
    this.state.setDoor(this);
    state.intClock();
  }
}