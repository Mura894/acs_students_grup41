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

  private Clock() {
    this.dateTime = LocalDateTime.now();
    this.running = false;
    logger.debug("Clock instance created");
  }

  public static Clock getInstance() {
    if (clockInstance == null) {
      clockInstance = new Clock();
      logger.debug("New Clock singleton instance created");
    }
    return clockInstance;
  }

  public void setRunning(boolean running) {
    this.running = running;
    logger.debug("Clock running set to: {}", running);
  }

  public boolean isRunning() {
    return running;
  }

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
        Thread.sleep(1000); // Espera 1 segundo
      } catch (InterruptedException e) {
        logger.warn("Clock thread interrupted: {}", e.getMessage());
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.debug("Clock thread stopped");
  }
}