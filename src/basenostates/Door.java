package basenostates;

import basenostates.requests.RequestReader;
import org.json.JSONObject;

//Class that represents a door and its actions
public class Door {
  private final String id;
  private boolean closed; // physically
  private State state;
  private Area areaTo;

  // Constructor with only door ID
  public Door(String id) {
    this.id = id;
    closed = true;
    setState(new Unlocked(this));
  }

  // Constructor with door ID and connected areas
  public Door(String id, Area areaFrom, Area areaTo) {
    this.id = id;
    this.areaTo = areaTo;
    this.areaTo.getDoorsGivingAccess();
    closed = true;
    setState(new Unlocked(this));
  }

  // Process a door access request
  public void processRequest(RequestReader request) {
    // it is the Door that process the request because the door has and knows
    // its state, and if closed or open
    if (request.isAuthorized()) {
      String action = request.getAction();
      doAction(action);
    } else {
      System.out.println("Door " + id + ": not authorized");
    }
    // Update the request with current door state
    request.setDoorStateName(getStateName());
  }

  // Execute the requested action on the door
  private void doAction(String action) {
    System.out.println("Door " + id + ": processing action " + action);

    switch (action) {
      case Actions.OPEN:
        if (closed && ((this.getStateName().equals("unlocked"))
            || (this.getStateName().equals("unlocked_shortly")))) {
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

    System.out.println("Door " + id + " state after action: "
        + getStateName() + ", closed: " + closed);
  }

  // Check if the door is closed
  public boolean isClosed() {
    return closed;
  }

  // Get the door ID
  public String getId() {
    return id;
  }

  // Get the current state name of the door
  public String getStateName() {
    return state.getState();
  }

  // Get the destination area of the door
  public Area getAreaTo() {
    return areaTo;
  }

  // String representation of the door
  @Override
  public String toString() {
    return "Door{"
        + "id='" + id + '\''
        + ", closed=" + closed
        + ", state=" + getStateName()
        + "}";
  }

  // Convert door information to JSON format
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("state", getStateName());
    json.put("closed", closed);
    return json;
  }

  // Set the state of the door
  public void setState(State estado) {
    System.out.println("Door " + id + ": changing state from "
        + (this.state != null ? this.state.getState() : "null")
        + " to " + estado.getState());
    this.state = estado;
    this.state.setDoor(this);
    state.intClock();
  }
}