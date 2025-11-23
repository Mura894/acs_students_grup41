package basenostates;

//Class that represents the state of the door when it is unlocked
public class Unlocked extends State {
  private Door door;

  public Unlocked(Door door) {
    this.door = door;
  }

  @Override
  public void unlock() {
    System.out.println("The door " + door.getId() + " is already unlocked");
  }

  @Override
  public void lock() {
    if (door.isClosed()) {
      door.setState(new Locked(door));
      System.out.println("Locking door " + door.getId());
    } else {
      System.out.println("Not possible to lock door " + door.getId() + " because it is open");
    }
  }

  @Override
  public void unlockShortly() {
    System.out.println("The door " + door.getId() + " is already unlocked");
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