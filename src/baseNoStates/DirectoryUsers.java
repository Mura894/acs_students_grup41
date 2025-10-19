package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public final class DirectoryUsers {
  private static final ArrayList<User> users = new ArrayList<>();
  private static final ArrayList<Group> groups = new ArrayList<>();

  public static void makeUsers() {
    System.out.println("Creating users and groups...");

    // Schedule para Blank group (sin privilegios) - básicamente nunca tiene acceso
    Schedule blankSchedule = new Schedule(
        LocalDateTime.of(2100, 1, 1, 0, 0), // Fecha futura
        LocalDateTime.of(2100, 1, 2, 0, 0), // Solo 1 día en el futuro
        LocalTime.of(0, 0),
        LocalTime.of(0, 0), // Horario de 0:00 a 0:00 = nunca
        DayOfWeek.MONDAY,
        DayOfWeek.MONDAY // Solo lunes
    );

    // Schedule para Employee: Sept 1 2025 to Mar. 1 2026, week days 9:00 to 17:00
    Schedule employeeSchedule = new Schedule(
        LocalDateTime.of(2025, 9, 1, 0, 0),
        LocalDateTime.of(2026, 3, 1, 23, 59),
        LocalTime.of(9, 0),
        LocalTime.of(17, 0),
        DayOfWeek.MONDAY,  // from Monday
        DayOfWeek.FRIDAY   // to Friday
    );

    // Schedule para Manager: Sept 1 2025 to Mar. 1 2026, week days plus Saturday, 8:00 to 20:00
    Schedule managerSchedule = new Schedule(
        LocalDateTime.of(2025, 9, 1, 0, 0),
        LocalDateTime.of(2026, 3, 1, 23, 59),
        LocalTime.of(8, 0),
        LocalTime.of(20, 0),
        DayOfWeek.MONDAY,   // from Monday
        DayOfWeek.SATURDAY  // to Saturday
    );

    // Schedule para Administrator: siempre tiene acceso (Jan 1 2025 to 2100)
    Schedule adminSchedule = new Schedule(
        LocalDateTime.of(2025, 1, 1, 0, 0),
        LocalDateTime.of(2100, 12, 31, 23, 59),
        LocalTime.of(0, 0),
        LocalTime.of(23, 59),
        DayOfWeek.MONDAY,   // from Monday
        DayOfWeek.SUNDAY    // to Sunday (todos los días)
    );

    // Obtener las áreas de DirectoryAreas
    DirectoryAreas areas = DirectoryAreas.getInstance();

    // Definir las áreas permitidas para cada grupo
    ArrayList<Area> allAreas = new ArrayList<>(Arrays.asList(
        areas.findAreaById("parking"),
        areas.findAreaById("hall"),
        areas.findAreaById("room1"),
        areas.findAreaById("room2"),
        areas.findAreaById("room3"),
        areas.findAreaById("corridor"),
        areas.findAreaById("IT"),
        areas.findAreaById("stairs"),
        areas.findAreaById("exterior"),
        areas.findAreaById("basement"),
        areas.findAreaById("ground_floor"),
        areas.findAreaById("floor1"),
        areas.findAreaById("building")
    ));

    // Employee: everywhere but the parking
    ArrayList<Area> employeeAreas = new ArrayList<>(allAreas);
    employeeAreas.remove(areas.findAreaById("parking"));

    // Acciones permitidas para cada grupo
    ArrayList<String> employeeActions = new ArrayList<>(Arrays.asList(
        Actions.OPEN,
        Actions.CLOSE,
        Actions.UNLOCK_SHORTLY
    ));

    ArrayList<String> managerActions = new ArrayList<>(Arrays.asList(
        Actions.OPEN,
        Actions.CLOSE,
        Actions.LOCK,
        Actions.UNLOCK,
        Actions.UNLOCK_SHORTLY
    ));

    ArrayList<String> adminActions = new ArrayList<>(Arrays.asList(
        Actions.OPEN,
        Actions.CLOSE,
        Actions.LOCK,
        Actions.UNLOCK,
        Actions.UNLOCK_SHORTLY
    ));

    ArrayList<String> blankActions = new ArrayList<>();

    Group blankGroup = new Group("Blank", blankActions, new ArrayList<>(), new ArrayList<>(), blankSchedule);
    Group employeeGroup = new Group("Employee", employeeActions, new ArrayList<>(), employeeAreas, employeeSchedule);
    Group managerGroup = new Group("Manager", managerActions, new ArrayList<>(), allAreas, managerSchedule);
    Group adminGroup = new Group("Administrator", adminActions, new ArrayList<>(), allAreas, adminSchedule);

    groups.add(blankGroup);
    groups.add(employeeGroup);
    groups.add(managerGroup);
    groups.add(adminGroup);

    System.out.println("Groups created: Blank, Employee, Manager, Administrator");

    // Crear usuarios y asignarles grupos

    // Blank group: users without any privilege
    User bernat = new User("Bernat", "12345", blankGroup);
    User blai = new User("Blai", "77532", blankGroup);
    blankGroup.addUser(bernat);
    blankGroup.addUser(blai);
    users.add(bernat);
    users.add(blai);

    // Employee group:
    // Sep. 1 this year to Mar. 1 next year, week days 9-17h
    // open, close, unlock_shortly everywhere but the parking
    User ernest = new User("Ernest", "74984", employeeGroup);
    User eulalia = new User("Eulalia", "43295", employeeGroup);
    employeeGroup.addUser(ernest);
    employeeGroup.addUser(eulalia);
    users.add(ernest);
    users.add(eulalia);

    // Manager group:
    // Sep. 1 this year to Mar. 1 next year, week days + saturday, 8-20h
    // all actions, all spaces
    User manel = new User("Manel", "95783", managerGroup);
    User marta = new User("Marta", "05827", managerGroup);
    managerGroup.addUser(manel);
    managerGroup.addUser(marta);
    users.add(manel);
    users.add(marta);

    // Admin group:
    // always, all days, all actions, all spaces
    User ana = new User("Ana", "11343", adminGroup);
    adminGroup.addUser(ana);
    users.add(ana);

    System.out.println("Created " + users.size() + " users with their respective groups");

    // Mostrar resumen
    for (Group group : groups) {
      System.out.println("Group " + group.getName() + " has " +
          group.getUsers().size() + " users");
    }
  }

  public static User findUserByCredential(String credential) {
    for (User user : users) {
      if (user.getCredential().equals(credential)) {
        return user;
      }
    }
    System.out.println("User with credential " + credential + " not found");
    return null;
  }

}