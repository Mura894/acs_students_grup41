package baseNoStates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Leaf in the Composite pattern
public class Space extends Area {
  private final ArrayList<Door> doorList = new ArrayList<>();
  private final String name;

  public Space(String s) {
    name = s;
  }

  @Override
  public String getName() {
    return name;
  }

  public void addDoor(String id) {
    Door door = DirectoryDoors.findDoorById(id);
    if (door != null) {
      doorList.add(door);
    } else {
      System.out.println("Cannot add door " + id + " to space " + name + ": door not found");
    }
  }

  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    return doorList;
  }

  @Override
  public Area findAreaById(String Name) {
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