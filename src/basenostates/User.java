package basenostates;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that represents the user
public class User {
  private final String name;
  private final String credential;
  private Group group;
  private static final Logger logger = LoggerFactory.getLogger(User.class);

  // Constructor for User with name, credential, and group
  public User(String name, String credential, Group group) {
    this.name = name;
    this.credential = credential;
    this.group = group;
    logger.debug("User created: {} with group: {}", name,
        (group != null ? group.getName() : "none"));
  }

  // Constructor for User with only name and credential (no group)
  public User(String name, String credential) {
    this.name = name;
    this.credential = credential;
    this.group = null;
    logger.debug("User created: {} (no group assigned)", name);
  }

  // Get the user's credential
  public String getCredential() {
    return credential;
  }

  // Get the user's name
  public String getUsername() {
    return name;
  }

  // Set the user's group
  public void setGroup(Group group) {
    this.group = group;
    logger.debug("User {} assigned to group: {}", name,
        (group != null ? group.getName() : "none"));
  }

  // Get the user's group
  public Group getGroup() {
    return group;
  }

  // String representation of the user
  @Override
  public String toString() {
    return "User{name=" + name + ", credential=" + credential
        + ", group=" + (group != null ? group.getName() : "none") + "}";
  }

  // Get the reason code for access denial from the user's group
  public int getReason() {
    if (group != null) {
      return group.getReason();
    } else {
      logger.warn("User {} has no group assigned", name);
      return 6; // No group assigned
    }
  }

  // Set the reason code for access denial in the user's group
  public void setReason(int reason) {
    if (group != null) {
      group.setReason(reason);
    } else {
      logger.warn("Cannot set reason: User {} has no group", name);
    }
  }

  // Check if the user can access a specific area with given action at specified time
  public boolean canAccess(Area area, String action, LocalDateTime now) {
    if (group != null) {
      logger.debug("Checking access for user: {} in group: {}", name, group.getName());
      return group.canAccess(area, action, now);
    } else {
      logger.warn("Access denied: User {} has no group assigned", name);
      return false;
    }
  }

  // Get human-readable reason message for access denial
  public String getReasonMessage() {
    if (group != null) {
      return group.getReasonMessage();
    } else {
      return "User " + name + " has no group assigned";
    }
  }
}