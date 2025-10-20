package baseNoStates;

import java.util.ArrayList;
import java.util.Arrays;

public class DirectoryAreas {
  private static DirectoryAreas instance;
  private static ArrayList<Area> allAreas;
  private static Partition building;
  private static Area rootArea;

  DirectoryAreas() {
    makeAreas();
  }

  public static DirectoryAreas getInstance() {
    if (instance == null) {
      instance = new DirectoryAreas();
    }
    return instance;
  }

  public static void makeAreas() {
    Space parking = new Space("parking");
    Space hall = new Space("hall");
    Space room1 = new Space("room1");
    Space room2 = new Space("room2");
    Space room3 = new Space("room3");
    Space corridor = new Space("corridor");
    Space IT = new Space("IT");
    Space stairs = new Space("stairs");
    Space exterior = new Space("exterior");

    Partition basement = new Partition("basement");
    Partition ground_floor = new Partition("ground_floor");
    Partition floor1 = new Partition("floor1");
    building = new Partition("building");

    parking.addDoor("D1");
    parking.addDoor("D2");

    hall.addDoor("D3");
    hall.addDoor("D4");

    room1.addDoor("D5");

    room2.addDoor("D6");

    corridor.addDoor("D7");

    room3.addDoor("D8");

    IT.addDoor("D9");

    exterior.addDoor("D1");
    exterior.addDoor("D3");

    basement.add(parking);

    ground_floor.add(hall);
    ground_floor.add(room1);
    ground_floor.add(room2);

    floor1.add(room3);
    floor1.add(corridor);
    floor1.add(IT);

    building.add(basement);
    building.add(ground_floor);
    building.add(floor1);
    building.add(stairs);
    building.add(exterior);

    allAreas = new ArrayList<>(Arrays.asList(parking, hall, room1, room2, room3, corridor, IT, stairs,
        exterior, basement, ground_floor, floor1, building));
  }

  public static Area findAreaById(String name) {
    if (name.equals("building")) {
      rootArea = building;
      return rootArea;
    } else {
      for (Area area : allAreas) {
        if (area.getName().equals(name)) {
          return area.findAreaById(name);
        }
      }
    }

    System.out.println("Area with name " + name + " not found");
    return null;
  }
}