package basenostates;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DirectoryDoors {
  private static ArrayList<Door> allDoors;
  private static final Logger logger = LoggerFactory.getLogger(DirectoryDoors.class);

  public static void makeDoors() {
    // basement
    Door d1 = new Door("D1", new Space("exterior"), new Space("parking")); // exterior, parking
    Door d2 = new Door("D2", new Space("stairs"), new Space("parking")); // stairs, parking

    // ground floor
    Door d3 = new Door("D3", new Space("exterior"), new Space("hall")); // exterior, hall
    Door d4 = new Door("D4", new Space("stairs"), new Space("hall")); // stairs, hall
    Door d5 = new Door("D5", new Space("hall"), new Space("room1")); // hall, room1
    Door d6 = new Door("D6", new Space("hall"), new Space("room2")); // hall, room2

    // first floor
    Door d7 = new Door("D7", new Space("stairs"), new Space("corridor")); // stairs, corridor
    Door d8 = new Door("D8", new Space("corridor"), new Space("room3")); // corridor, room3
    Door d9 = new Door("D9", new Space("corridor"), new Space("IT")); // corridor, IT

    allDoors = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8, d9));

    logger.info("Created {} doors", allDoors.size());
  }

  public static Door findDoorById(String id) {
    for (Door door : allDoors) {
      if (door.getId().equals(id)) {
        return door;
      }
    }

    logger.warn("Door with id {} not found", id);
    return null;
  }

  // This is needed by RequestRefresh
  public static ArrayList<Door> getAllDoors() {
    logger.debug("Retrieving all doors: {} doors total", allDoors.size());
    return allDoors;
  }
}