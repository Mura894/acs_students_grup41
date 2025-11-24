package basenostates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the state of the door when it is unlocked
public class Unlocked extends State {
  private Door door;
  private static final Logger logger = LoggerFactory.getLogger(Unlocked.class);

  // Constructor for Unlocked state
  public Unlocked(Door door) {
    this.door = door;
  }

  // Door is already unlocked, so log a warning
  @Override
  public void unlock() {
    logger.warn("The door {} is already unlocked", door.getId());
  }

  // Lock the door if it is closed
  @Override
  public void lock() {
    if (door.isClosed()) {
      door.setState(new Locked(door));
      logger.info("Locking door {}", door.getId());
    } else {
      logger.warn("Not possible to lock door {} because it is open", door.getId());
    }
  }

  // Door is already unlocked, so log a warning
  @Override
  public void unlockShortly() {
    logger.warn("The door {} is already unlocked", door.getId());
  }

  // Set the door reference for this state
  @Override
  public void setDoor(Door puerta) {
    this.door = puerta;
  }

  // Get the name of this state
  public String getState() {
    return "unlocked";
  }

  // Initialize clock for this state (no action needed for unlocked state)
  @Override
  public void intClock() {
  }
}