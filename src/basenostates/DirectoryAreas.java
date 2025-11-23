package basenostates;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryAreas {
  private static DirectoryAreas instance;
  private static ArrayList<Area> allAreas;
  private static Partition building;
  private static Area rootArea;
  private static final Logger logger = LoggerFactory.getLogger(DirectoryAreas.class);

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
    final Space parking = new Space("parking");
    final Space hall = new Space("hall");
    final Space room1 = new Space("room1");
    final Space room2 = new Space("room2");
    final Space room3 = new Space("room3");
    final Space corridor = new Space("corridor");
    final Space itRoom = new Space("IT");
    final Space stairs = new Space("stairs");
    final Space exterior = new Space("exterior");

    final Partition basement = new Partition("basement");
    final Partition groundFloor = new Partition("ground_floor");
    final Partition floor1 = new Partition("floor1");
    building = new Partition("building");

    parking.addDoor("D1");
    parking.addDoor("D2");

    hall.addDoor("D3");
    hall.addDoor("D4");

    room1.addDoor("D5");

    room2.addDoor("D6");

    corridor.addDoor("D7");

    room3.addDoor("D8");

    itRoom.addDoor("D9");

    exterior.addDoor("D1");
    exterior.addDoor("D3");

    basement.add(parking);

    groundFloor.add(hall);
    groundFloor.add(room1);
    groundFloor.add(room2);

    floor1.add(room3);
    floor1.add(corridor);
    floor1.add(itRoom);

    building.add(basement);
    building.add(groundFloor);
    building.add(floor1);
    building.add(stairs);
    building.add(exterior);

    allAreas = new ArrayList<>(Arrays.asList(
        parking, hall, room1, room2, room3, corridor, itRoom, stairs,
        exterior, basement, groundFloor, floor1, building));
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

    logger.warn("Area with name {} not found", name);
    return null;
  }
}