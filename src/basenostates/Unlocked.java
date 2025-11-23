package basenostates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the state of the door when it is unlocked
public class Unlocked extends State {
  private Door door;
  private static final Logger logger = LoggerFactory.getLogger(Unlocked.class);

  public Unlocked(Door door) {
    this.door = door;
  }

  @Override
  public void unlock() {
    logger.warn("The door {} is already unlocked", door.getId());
  }

  @Override
  public void lock() {
    if (door.isClosed()) {
      door.setState(new Locked(door));
      logger.info("Locking door {}", door.getId());
    } else {
      logger.warn("Not possible to lock door {} because it is open", door.getId());
    }
  }

  @Override
  public void unlockShortly() {
    logger.warn("The door {} is already unlocked", door.getId());
  }

  @Override
  public void setDoor(Door puerta) {
    this.door = puerta;
  }

  public String getState() {
    return "unlocked";
  }

  @Override
  public void intClock() {
  }
}