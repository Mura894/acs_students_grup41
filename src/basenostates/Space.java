package basenostates;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Leaf in the Composite pattern
public class Space extends Area {
  private final ArrayList<Door> doorList = new ArrayList<>();
  private final String name;
  private static final Logger logger = LoggerFactory.getLogger(Space.class);

  // Constructor for Space with name
  public Space(String s) {
    name = s;
  }

  // Get the name of this space
  @Override
  public String getName() {
    return name;
  }

  // Add a door to this space by door ID
  public void addDoor(String id) {
    Door door = DirectoryDoors.getInstance().findDoorById(id);
    if (door != null) {
      doorList.add(door);
    } else {
      logger.warn("Cannot add door {} to space {}: door not found", id, name);
    }
  }

  // Get all doors that give access to this space
  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    return doorList;
  }

  // Find area by name within this space
  @Override
  public Area findAreaById(String name) {
    return this;
  }

  // Convert space to JSON representation with specified depth
  public JSONObject toJson(int depth) {
    JSONObject json = new JSONObject();
    json.put("class", "space");
    json.put("id", name);
    JSONArray jsonDoors = new JSONArray();
    for (Door d : getDoorsGivingAccess()) {
      jsonDoors.put(d.toJson());
    }
    json.put("access_doors", jsonDoors);
    return json;
  }
}