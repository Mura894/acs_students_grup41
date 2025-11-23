package baseNoStates;

import java.util.ArrayList;
import org.json.JSONObject;

//Class to be able to make the Composite pattern and be able to have areas that contain other areas
public abstract class Area {
  public abstract String getName();

  //Function to obtain all the doors that give access to an area
  public abstract ArrayList<Door> getDoorsGivingAccess();

  public abstract Area findAreaById(String Name);

  public abstract JSONObject toJson(int depth);
}
