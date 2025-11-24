package basenostates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the state of the door when it is propped (left open after unlocked_shortly)
public class Propped extends State {
  private Door door;
  private static final Logger logger = LoggerFactory.getLogger(Propped.class);

  // Constructor for Propped state - triggers alarm when door is left open too long
  public Propped(Door door) {
    this.door = door;
    logger.warn("DOOR PROPPED ALARM");
    logger.warn("Door {} entered PROPPED state - LEFT OPEN TOO LONG!", door.getId());
    logger.warn("ALARM: Door should be closed but remains open");
  }

  // Cannot unlock door in propped state - must close it first
  @Override
  public void unlock() {
    logger.warn("Cannot unlock door {} in PROPPED state - CLOSE IT FIRST!", door.getId());
  }

  // Cannot lock door in propped state - must close it first
  @Override
  public void lock() {
    logger.warn("Cannot lock door {} in PROPPED state - CLOSE IT FIRST!", door.getId());
  }

  // Cannot unlock shortly in propped state - must close it first
  @Override
  public void unlockShortly() {
    logger.warn("Cannot unlock_shortly door {} in PROPPED state - CLOSE IT FIRST!", door.getId());
  }

  // Close the door when in propped state - returns to locked state
  public void closeDoor() {
    if (door.isClosed()) {
      logger.info("Door {} closed in PROPPED state - returning to LOCKED", door.getId());
      door.setState(new Locked(door));
    }
  }

  // Set the door reference for this state
  @Override
  public void setDoor(Door door) {
    this.door = door;
  }

  // Get the name of this state
  public String getState() {
    return "propped";
  }

  // Does not need clock in propped state
  @Override
  public void intClock() {
  }
}