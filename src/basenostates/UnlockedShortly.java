package basenostates;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

//Class that represents the state of the unlocked shortly
public class UnlockedShortly extends State implements Observer {
  private Door door;
  private LocalDateTime dateTime;
  private Clock clock;
  Thread clockThread;

  public UnlockedShortly(Door door) {
    this.door = door;
    System.out.println("Door " + door.getId() + " entered UnlockedShortly state");
  }

  @Override
  public void unlock() {
    System.out.println("The door " + door.getId() + " is already in unlocked_shortly state");
  }

  @Override
  public void lock() {
    if (door.isClosed()) {
      door.setState(new Locked(door));
      System.out.println("Locking door " + door.getId() + " from unlocked_shortly state");
    } else {
      System.out.println("Not possible to lock door " + door.getId() + " because it is open");
    }
  }

  @Override
  public void unlockShortly() {
    System.out.println("The door " + door.getId() + " is already in unlocked_shortly state");
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

      System.out.println("=== UNLOCKED_SHORTLY TIMER UPDATE ===");
      System.out.println("Door " + door.getId() + " - Time elapsed: " + secondsPassed + "s");
      System.out.println("Door closed: " + door.isClosed());
      System.out.println("Current state: " + door.getStateName());

      if (secondsPassed >= 10) {
        if (door.isClosed()) {
          System.out.println(">>> 10+ seconds passed and door CLOSED - locking automatically");
          this.lock();
          clock.deleteObserver(this);
          dateTime = null;
          clock.setRunning(false);
          System.out.println(">>> Door " + door.getId() + " returned to LOCKED state");
        } else {
          System.out.println(">>> 10+ seconds passed and door STILL OPEN - changing to PROPPED");
          door.setState(new Propped(door));
          clock.deleteObserver(this);
          dateTime = null;
          clock.setRunning(false);
          System.out.println(">>> Door " + door.getId() + " changed to PROPPED state");
        }
      } else {
        System.out.println("Timer still running... " + (10 - secondsPassed) + "s remaining");
      }
      System.out.println("=== END TIMER UPDATE ===");
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
      System.out.println("UnlockedShortly timer started for door "
              + door.getId() + " at " + dateTime);
    }
  }
}