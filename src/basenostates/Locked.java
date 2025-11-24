package basenostates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the state of the door when it is locked
public class Locked extends State {
  private Door door;
  private static final Logger logger = LoggerFactory.getLogger(Locked.class);

  // Constructor for Locked state
  public Locked(Door door) {
    this.door = door;
  }

  // Unlock the door if it is closed
  @Override
  public void unlock() {
    if (door.isClosed()) {
      door.setState(new Unlocked(door));
      logger.info("Unlocking door {}", door.getId());
    }
  }

  // Door is already locked, so log a warning
  @Override
  public void lock() {
    logger.warn("The door {} is already locked", door.getId());
  }

  // Unlock the door shortly if it is closed
  @Override
  public void unlockShortly() {
    if (door.isClosed()) {
      door.setState(new UnlockedShortly(door));
      logger.info("Unlocking Shortly door {}", door.getId());
    }
  }

  // Set the door reference for this state
  @Override
  public void setDoor(Door door) {
    this.door = door;
  }

  // Get the name of this state
  public String getState() {
    return "locked";
  }

  // Initialize clock for this state (no action needed for locked state)
  @Override
  public void intClock() {
  }
}