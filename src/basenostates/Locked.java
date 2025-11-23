package basenostates;

//Class that represents the state of the door when it is locked
public class Locked extends State {
  private Door door;

  public Locked(Door door) {
    this.door = door;
  }

  @Override
  public void unlock() {
    if (door.isClosed()) {
      door.setState(new Unlocked(door));
      System.out.println("Unlocking door" + door.getId());
    }
  }

  @Override
  public void lock() {
    System.out.println("The door " + door.getId() + " is already locked");
  }

  @Override
  public void unlockShortly() {
    if (door.isClosed()) {
      door.setState(new UnlockedShortly(door));
      System.out.println("Unlocking Shortly door " + door.getId());
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