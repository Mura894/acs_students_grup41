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

  public User(String name, String credential, Group group) {
    this.name = name;
    this.credential = credential;
    this.group = group;
    logger.debug("User created: {} with group: {}", name,
        (group != null ? group.getName() : "none"));
  }

  public User(String name, String credential) {
    this.name = name;
    this.credential = credential;
    this.group = null;
    logger.debug("User created: {} (no group assigned)", name);
  }

  public String getCredential() {
    return credential;
  }

  public String getUsername() {
    return name;
  }

  public void setGroup(Group group) {
    this.group = group;
    logger.debug("User {} assigned to group: {}", name,
        (group != null ? group.getName() : "none"));
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
      logger.warn("User {} has no group assigned", name);
      return 6; // No group assigned
    }
  }

  public void setReason(int reason) {
    if (group != null) {
      group.setReason(reason);
    } else {
      logger.warn("Cannot set reason: User {} has no group", name);
    }
  }

  public boolean canAccess(Area area, String action, LocalDateTime now) {
    if (group != null) {
      logger.debug("Checking access for user: {} in group: {}", name, group.getName());
      return group.canAccess(area, action, now);
    } else {
      logger.warn("Access denied: User {} has no group assigned", name);
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