package basenostates;

//Class that represents the state of the door when it is propped (left open after unlocked_shortly)
public class Propped extends State {
  private Door door;

  public Propped(Door door) {
    this.door = door;
    System.out.println("DOOR PROPPED ALARM");
    System.out.println("Door " + door.getId() + " entered PROPPED state - LEFT OPEN TOO LONG!");
    System.out.println("ALARM: Door should be closed but remains open");
  }

  @Override
  public void unlock() {
    System.out.println("Cannot unlock door "
            + door.getId() + " in PROPPED state - CLOSE IT FIRST!");
  }

  @Override
  public void lock() {
    System.out.println("Cannot lock door "
            + door.getId() + " in PROPPED state - CLOSE IT FIRST!");
  }

  @Override
  public void unlockShortly() {
    System.out.println("Cannot unlock_shortly door "
            + door.getId() + " in PROPPED state - CLOSE IT FIRST!");
  }

  public void closeDoor() {
    if (door.isClosed()) {
      System.out.println("Door " + door.getId()
              + " closed in PROPPED state - returning to LOCKED");
      door.setState(new Locked(door));
    }
  }

  @Override
  public void setDoor(Door door) {
    this.door = door;
  }

  public String getState() {
    return "propped";
  }

  // Does not need clock in propped state
  @Override
  public void intClock() {
  }
}