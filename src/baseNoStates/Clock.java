package baseNoStates;

import java.util.Observable;
import java.time.LocalDateTime;

//Clock class that implements the singleton pattern
public class Clock extends Observable implements Runnable {
  private LocalDateTime dateTime;
  private boolean running;
  private static Clock clockInstance;

  private Clock() {
    this.dateTime = LocalDateTime.now();
    this.running = false;
    System.out.println("Clock instance created"); // ← Cambiado
  }

  public static Clock getInstance() {
    if (clockInstance == null) {
      clockInstance = new Clock();
      System.out.println("New Clock singleton instance created");
    }
    return clockInstance;
  }

  public void setRunning(boolean running) {
    this.running = running;
    System.out.println("Clock running set to: " + running);
  }

  public boolean isRunning() {
    return running;
  }

  private void tick() {
    this.dateTime = LocalDateTime.now();
    setChanged();
    notifyObservers(dateTime);
    System.out.println("Clock tick: " + dateTime + " (notifying observers)");
  }

  //Every second the tick() function is called which notifies the observers
  @Override
  public void run() {
    System.out.println("Clock thread started");
    while (running) {
      System.out.println("Clock tick");
      this.tick();
      try {
        Thread.sleep(1000); // Espera 1 segundo
      } catch (InterruptedException e) {
        System.out.println("Clock thread interrupted: " + e.getMessage());
        Thread.currentThread().interrupt();
        break;
      }
    }
    System.out.println("Clock thread stopped");
  }
}