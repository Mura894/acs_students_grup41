package basenostates;

//Class to be able to apply the State pattern to the doors
public abstract class State {

  // Lock the door
  public abstract void lock();

  // Unlock the door
  public abstract void unlock();

  // Unlock the door for a short period
  public abstract void unlockShortly();

  // Set the door reference for this state
  public abstract void setDoor(Door door);

  // Get the name of this state
  public abstract String getState();

  // Initialize clock for this state
  public abstract void intClock();
}