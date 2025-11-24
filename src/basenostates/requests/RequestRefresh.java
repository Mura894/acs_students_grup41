package basenostates.requests;

import basenostates.DirectoryDoors;
import basenostates.Door;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class RequestRefresh implements Request {
  private final ArrayList<JSONObject> jsonsDoors = new ArrayList<>();

  // Convert the refresh request response to JSON format
  @Override
  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("doors", new JSONArray(jsonsDoors));
    // jsonDoors has been set previously by process()
    return json;
  }

  // String representation of the refresh request
  @Override
  public String toString() {
    return "RequestRefresh{"
        + jsonsDoors
        + "}";
  }

  // Also this is used to paint the simulator when the page is loaded, and to display
  // doors and readers after passing from locked to propped or propped to locked,
  // pressing the Refresh Request button of the simulator.
  // Also, to quickly test if the partition requests sent by the client app in Flutter
  // works or not, retrieves the state of all the doors so that the simulator can
  // repaint the readers
  public void process() {
    // Get all doors from directory and add their JSON representation to the list
    for (Door door : DirectoryDoors.getInstance().getAllDoors()) {
      jsonsDoors.add(door.toJson());
    }
  }
}