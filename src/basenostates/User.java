package basenostates;

import java.time.LocalDateTime;

//Class that represents the user
public class User {
  private final String name;
  private final String credential;
  private Group group;



  public User(String name, String credential, Group group) {
    this.name = name;
    this.credential = credential;
    this.group = group;
    System.out.println("User created: " + name + " with group: "
        + (group != null ? group.getName() : "none"));
  }


  public User(String name, String credential) {
    this.name = name;
    this.credential = credential;
    this.group = null;
    System.out.println("User created: " + name + " (no group assigned)");
  }

  public String getCredential() {
    return credential;
  }

  public String getUsername() {
    return name;
  }

  public void setGroup(Group group) {
    this.group = group;
    System.out.println("User " + name + " assigned to group: "
        + (group != null ? group.getName() : "none"));
  }

  public Group getGroup() {
    return group;
  }

  @Override
  public String toString() {
    return "User{name=" + name + ", credential=" + credential
       + ", group=" + (group != null ? group.getName() : "none") + "}";
  }

  public int getReason() {
    if (group != null) {
      return group.getReason();
    } else {
      System.out.println("User " + name + " has no group assigned");
      return 6; // No group assigned
    }
  }

  public void setReason(int reason) {
    if (group != null) {
      group.setReason(reason);
    } else {
      System.out.println("Cannot set reason: User " + name + " has no group");
    }
  }

  public boolean canAccess(Area area, String action, LocalDateTime now) {
    if (group != null) {
      System.out.println("Checking access for user: " + name + " in group: " + group.getName());
      return group.canAccess(area, action, now);
    } else {
      System.out.println("Access denied: User " + name + " has no group assigned");
      return false;
    }
  }

  public String getReasonMessage() {
    if (group != null) {
      return group.getReasonMessage();
    } else {
      return "User " + name + " has no group assigned";
    }
  }
}