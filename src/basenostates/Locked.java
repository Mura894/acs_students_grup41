package basenostates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the state of the door when it is locked
public class Locked extends State {
  private Door door;
  private static final Logger logger = LoggerFactory.getLogger(Locked.class);

  public Locked(Door door) {
    this.door = door;
  }

  @Override
  public void unlock() {
    if (door.isClosed()) {
      door.setState(new Unlocked(door));
      logger.info("Unlocking door {}", door.getId());
    }
  }

  @Override
  public void lock() {
    logger.warn("The door {} is already locked", door.getId());
  }

  @Override
  public void unlockShortly() {
    if (door.isClosed()) {
      door.setState(new UnlockedShortly(door));
      logger.info("Unlocking Shortly door {}", door.getId());
    }
  }

  @Override
  public void setDoor(Door door) {
    this.door = door;
  }

  public String getState() {
    return "locked";
  }

  @Override
  public void intClock() {
  }
}