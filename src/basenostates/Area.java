package basenostates;

import java.util.ArrayList;
import org.json.JSONObject;

//Class to be able to make the Composite pattern and be able to have areas that contain other areas
public abstract class Area {
  // Get the name of the area
  public abstract String getName();

  //Function to obtain all the doors that give access to an area
  public abstract ArrayList<Door> getDoorsGivingAccess();

  // Find an area by its ID within the area hierarchy
  public abstract Area findAreaById(String name);

  // Convert the area to JSON representation with specified depth
  public abstract JSONObject toJson(int depth);
}