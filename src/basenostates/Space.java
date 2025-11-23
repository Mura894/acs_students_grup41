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

  public Space(String s) {
    name = s;
  }

  @Override
  public String getName() {
    return name;
  }

  public void addDoor(String id) {
    Door door = DirectoryDoors.getInstance().findDoorById(id);
    if (door != null) {
      doorList.add(door);
    } else {
      logger.warn("Cannot add door {} to space {}: door not found", id, name);
    }
  }

  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    return doorList;
  }

  @Override
  public Area findAreaById(String name) {
    return this;
  }

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