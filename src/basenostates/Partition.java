package basenostates;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


//Composite class of the Composite pattern
public class Partition extends Area {
  private final ArrayList<Area> spaceList = new ArrayList<>();

  private final String name;

  // Constructor for Partition with name
  public Partition(String s) {
    name = s;
  }

  // Add an area to this partition
  public void add(Area area) {
    spaceList.add(area);
  }

  // Get all areas contained in this partition
  public ArrayList<Area> getAreas() {
    return spaceList;
  }

  // Get the name of this partition
  @Override
  public String getName() {
    return name;
  }

  // Get all doors that give access to this partition and its sub-areas
  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    ArrayList<Door> listapuertas = new ArrayList<>();

    for (Area area : spaceList) {
      listapuertas.addAll(area.getDoorsGivingAccess());
    }
    return listapuertas;
  }

  // Find area by name within this partition
  @Override
  public Area findAreaById(String name) {
    return this;
  }

  // Convert partition to JSON representation with specified depth
  public JSONObject toJson(int depth) {
    JSONObject json = new JSONObject();
    json.put("class", "partition");
    json.put("id", name);
    JSONArray jsonAreas = new JSONArray();
    if (depth > 0) {
      for (Area a : getAreas()) {
        jsonAreas.put(a.toJson(depth - 1));
      }
      json.put("areas", jsonAreas);
    }
    return json;
  }
}