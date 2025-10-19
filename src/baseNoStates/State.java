package baseNoStates;

//Class to be able to apply the State pattern to the doors
public abstract class State {

  public abstract void lock();

  public abstract void unlock();

  public abstract void unlockShortly();

  public abstract void setDoor(Door door);

  public abstract String getState();

  public abstract void intClock();
}

