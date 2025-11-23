package basenostates;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the state of the unlocked shortly
public class UnlockedShortly extends State implements Observer {
  private Door door;
  private LocalDateTime dateTime;
  private Clock clock;
  Thread clockThread;
  private static final Logger logger = LoggerFactory.getLogger(UnlockedShortly.class);

  public UnlockedShortly(Door door) {
    this.door = door;
    logger.info("Door {} entered UnlockedShortly state", door.getId());
  }

  @Override
  public void unlock() {
    logger.warn("The door {} is already in unlocked_shortly state", door.getId());
  }

  @Override
  public void lock() {
    if (door.isClosed()) {
      door.setState(new Locked(door));
      logger.info("Locking door {} from unlocked_shortly state", door.getId());
    } else {
      logger.warn("Not possible to lock door {} because it is open", door.getId());
    }
  }

  @Override
  public void unlockShortly() {
    logger.warn("The door {} is already in unlocked_shortly state", door.getId());
  }

  @Override
  public void setDoor(Door puerta) {
    this.door = puerta;
  }

  public String getState() {
    return "unlocked_shortly";
  }

  //Function that runs when the clock is updated and checks if 10 seconds have passed
  // and the door is closed
  @Override
  public void update(Observable o, Object arg) {
    if (dateTime != null) {
      LocalDateTime currentTime = (LocalDateTime) arg;
      long secondsPassed = dateTime.until(currentTime, java.time.temporal.ChronoUnit.SECONDS);

      logger.debug("=== UNLOCKED_SHORTLY TIMER UPDATE ===");
      logger.debug("Door {} - Time elapsed: {}s", door.getId(), secondsPassed);
      logger.debug("Door closed: {}", door.isClosed());
      logger.debug("Current state: {}", door.getStateName());

      if (secondsPassed >= 10) {
        if (door.isClosed()) {
          logger.info("10+ seconds passed and door CLOSED - locking automatically");
          this.lock();
          clock.deleteObserver(this);
          dateTime = null;
          clock.setRunning(false);
          logger.info("Door {} returned to LOCKED state", door.getId());
        } else {
          logger.info("10+ seconds passed and door STILL OPEN - changing to PROPPED");
          door.setState(new Propped(door));
          clock.deleteObserver(this);
          dateTime = null;
          clock.setRunning(false);
          logger.info("Door {} changed to PROPPED state", door.getId());
        }
      } else {
        logger.debug("Timer still running... {}s remaining", (10 - secondsPassed));
      }
      logger.debug("=== END TIMER UPDATE ===");
    }
  }

  @Override
  public void intClock() {
    if (dateTime == null) {
      clock = Clock.getInstance();
      clock.setRunning(true);
      clockThread = new Thread(clock);
      clockThread.start();
      clock.addObserver(this);
      dateTime = LocalDateTime.now();
      logger.info("UnlockedShortly timer started for door {} at {}", door.getId(), dateTime);
    }
  }
}