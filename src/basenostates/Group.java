package basenostates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Group {
  private final String name;
  private final ArrayList<String> actions;
  private final ArrayList<User> user;
  private final ArrayList<Area> areas;
  private final Schedule schedule;
  private int reason;
  private static final Logger logger = LoggerFactory.getLogger(Group.class);

  public Group(String name, ArrayList<String> actions, ArrayList<User> user,
               ArrayList<Area> areas, Schedule schedule) {
    this.name = name;
    this.actions = actions;
    this.user = user;
    this.areas = areas;
    this.schedule = schedule;
    this.reason = 0;
  }

  public String getName() {
    return name;
  }

  public void addUser(User user) {
    user.setGroup(this);
    this.user.add(user);
    logger.debug("User {} added to group {}", user.getUsername(), name);
  }

  public int getReason() {
    return reason;
  }

  public void setReason(int reason) {
    this.reason = reason;
  }

  public boolean canAccess(Area area, String action, LocalDateTime now) {
    logger.debug("Checking access for group: {}, area: {}, action: {}, time: {}",
        name, area.getName(), action, now);

    boolean authoriseAction = false;
    boolean authoriseArea = false;
    int reasonSchedule = 0;
    boolean authoriseSchedule;

    switch (name) {
      case "Employee":
        for (String accion : actions) {
          if (action.equals(accion)) {
            authoriseAction = true;
            break;
          }
        }
        if (!authoriseAction) {
          reason = 1; // No valid action
          logger.warn("Access denied: Invalid action for Employee group");
          return false;
        }

        for (Area areaIteration : areas) {
          if (area.getName().equals(areaIteration.getName())) {
            authoriseArea = true;
            break;
          }
        }
        if (!authoriseArea) {
          reason = 2; // No valid area
          logger.warn("Access denied: Invalid area for Employee group");
          return false;
        }

        reasonSchedule = schedule.canAccess(now);
        switch (reasonSchedule) {
          case 3:
            authoriseSchedule = false;
            reason = 3; // Invalid date
            logger.warn("Access denied: Invalid date for Employee group");
            break;
          case 4:
            authoriseSchedule = false;
            reason = 4; // Invalid day of week
            logger.warn("Access denied: Invalid day of week for Employee group");
            break;
          case 5:
            authoriseSchedule = false;
            reason = 5; // Invalid hour
            logger.warn("Access denied: Invalid hour for Employee group");
            break;
          default:
            authoriseSchedule = true;
            break;
        }

        boolean result = authoriseAction && authoriseArea && authoriseSchedule;
        logger.debug("Employee access result: {}", result);
        return result;

      case "Manager":
        reasonSchedule = schedule.canAccess(now);
        switch (reasonSchedule) {
          case 3:
            authoriseSchedule = false;
            reason = 3; // Invalid date
            logger.warn("Access denied: Invalid date for Manager group");
            break;
          case 4:
            authoriseSchedule = false;
            reason = 4; // Invalid day of week
            logger.warn("Access denied: Invalid day of week for Manager group");
            break;
          case 5:
            authoriseSchedule = false;
            reason = 5; // Invalid hour
            logger.warn("Access denied: Invalid hour for Manager group");
            break;
          default:
            authoriseSchedule = true;
            break;
        }

        logger.debug("Manager access result: {}", authoriseSchedule);
        return authoriseSchedule;

      case "Administrator":
        // Have all the permissions
        logger.debug("Administrator access granted: full permissions");
        return true;

      default:
        // User have no permissions
        reason = 6;
        logger.warn("Access denied: Unknown group {}", name);
        return false;
    }
  }

  public String getReasonMessage() {
    switch (reason) {
      case 1:
        return "Not allowed action for group " + name;
      case 2:
        return "Not allowed area for group " + name;
      case 3:
        return "Date not in schedule for group " + name;
      case 4:
        return "Day of week not in schedule for group " + name;
      case 5:
        return "Time not in schedule for group " + name;
      case 6:
        return "Unknown group " + name;
      default:
        return "Access granted for group " + name;
    }
  }

  public ArrayList<User> getUsers() {
    return user;
  }
}