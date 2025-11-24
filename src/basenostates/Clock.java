package basenostates;

import java.time.LocalDateTime;
import java.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Clock class that implements the singleton pattern
public class Clock extends Observable implements Runnable {
  private LocalDateTime dateTime;
  private boolean running;
  private static Clock clockInstance;
  private static final Logger logger = LoggerFactory.getLogger(Clock.class);

  // Private constructor for singleton pattern
  private Clock() {
    this.dateTime = LocalDateTime.now();
    this.running = false;
    logger.debug("Clock instance created");
  }

  // Get the singleton instance of Clock
  public static Clock getInstance() {
    if (clockInstance == null) {
      clockInstance = new Clock();
      logger.debug("New Clock singleton instance created");
    }
    return clockInstance;
  }

  // Set whether the clock is running or not
  public void setRunning(boolean running) {
    this.running = running;
    logger.debug("Clock running set to: {}", running);
  }

  // Check if the clock is running
  public boolean isRunning() {
    return running;
  }

  // Update the time and notify all observers
  private void tick() {
    this.dateTime = LocalDateTime.now();
    setChanged();
    notifyObservers(dateTime);
    logger.debug("Clock tick: {} (notifying observers)", dateTime);
  }

  //Every second the tick() function is called which notifies the observers
  @Override
  public void run() {
    logger.debug("Clock thread started");
    while (running) {
      logger.debug("Clock tick");
      this.tick();
      try {
        Thread.sleep(1000); // Wait 1 second
      } catch (InterruptedException e) {
        logger.warn("Clock thread interrupted: {}", e.getMessage());
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.debug("Clock thread stopped");
  }
}