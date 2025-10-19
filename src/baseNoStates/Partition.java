package baseNoStates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Composite class of the Composite pattern
public class Partition extends Area {
  private final ArrayList<Area> spaceList = new ArrayList<>();

  private final String name;

  public Partition(String s) {
    name = s;
  }

  public void add(Area area) {
    spaceList.add(area);
  }

  public ArrayList<Area> getAreas() {
    return spaceList;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ArrayList<Door> getDoorsGivingAccess() {
    ArrayList<Door> listapuertas = new ArrayList<>();

    for (Area area : spaceList) {
      listapuertas.addAll(area.getDoorsGivingAccess());
    }
    return listapuertas;
  }

  @Override
  public Area findAreaById(String Name) {
    return this;
  }

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