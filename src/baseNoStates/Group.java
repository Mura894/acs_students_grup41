package baseNoStates;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Group {
  private final String name;
  private final ArrayList<String> actions;
  private final ArrayList<User> user;
  private final ArrayList<Area> areas;
  private final Schedule schedule;
  private int reason;
  // private static final Logger logger = LoggerFactory.getLogger(Group.class); // ← Comentado

  public Group(String name, ArrayList<String> actions, ArrayList<User> user, ArrayList<Area> areas, Schedule schedule) {
    this.name = name;
    this.actions = actions;
    this.user = user;
    this.areas = areas;
    this.schedule = schedule;
    this.reason = 0;
  }

  public String getName() { // ← DESCOMENTADO y corregido
    return name;
  }

  public void addUser(User user) {
    user.setGroup(this);
    this.user.add(user);
    System.out.println("User " + user.getUsername() + " added to group " + name); // ← Añadido para debugging
  }

  public int getReason() {
    return reason;
  }

  public void setReason(int reason) {
    this.reason = reason;
  }

  public boolean canAccess(Area area, String action, LocalDateTime now) {
    System.out.println("Checking access for group: " + name + ", area: " + area.getName() + ", action: " + action + ", time: " + now); // ← Añadido

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
          System.out.println("Access denied: Invalid action for Employee group"); // ← Añadido
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
          System.out.println("Access denied: Invalid area for Employee group"); // ← Añadido
          return false;
        }

        reasonSchedule = schedule.canAccess(now);
        switch (reasonSchedule) {
          case 3:
            authoriseSchedule = false;
            reason = 3; // Invalid date
            System.out.println("Access denied: Invalid date for Employee group"); // ← Añadido
            break;
          case 4:
            authoriseSchedule = false;
            reason = 4; // Invalid day of week
            System.out.println("Access denied: Invalid day of week for Employee group"); // ← Añadido
            break;
          case 5:
            authoriseSchedule = false;
            reason = 5; // Invalid hour
            System.out.println("Access denied: Invalid hour for Employee group"); // ← Añadido
            break;
          default:
            authoriseSchedule = true;
            break;
        }

        boolean result = authoriseAction && authoriseArea && authoriseSchedule;
        System.out.println("Employee access result: " + result); // ← Añadido
        return result;

      case "Manager":
        reasonSchedule = schedule.canAccess(now);
        switch (reasonSchedule) {
          case 3:
            authoriseSchedule = false;
            reason = 3; // Invalid date
            System.out.println("Access denied: Invalid date for Manager group"); // ← Añadido
            break;
          case 4:
            authoriseSchedule = false;
            reason = 4; // Invalid day of week
            System.out.println("Access denied: Invalid day of week for Manager group"); // ← Añadido
            break;
          case 5:
            authoriseSchedule = false;
            reason = 5; // Invalid hour
            System.out.println("Access denied: Invalid hour for Manager group"); // ← Añadido
            break;
          default:
            authoriseSchedule = true;
            break;
        }

        System.out.println("Manager access result: " + authoriseSchedule); // ← Añadido
        return authoriseSchedule;

      case "Administrator":
        // Have all the permissions
        System.out.println("Administrator access granted: full permissions"); // ← Añadido
        return true;

      default:
        // User have no permissions
        reason = 6;
        System.out.println("Access denied: Unknown group " + name); // ← Añadido
        return false;
    }
  }

  public String getReasonMessage() {
    switch (reason) {
      case 1: return "Not allowed action for group " + name;
      case 2: return "Not allowed area for group " + name;
      case 3: return "Date not in schedule for group " + name;
      case 4: return "Day of week not in schedule for group " + name;
      case 5: return "Time not in schedule for group " + name;
      case 6: return "Unknown group " + name;
      default: return "Access granted for group " + name;
    }
  }
  public ArrayList<User> getUsers() {
    return user;
  }
}