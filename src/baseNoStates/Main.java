package baseNoStates;

// Before executing enable assertions :
// https://se-education.org/guides/tutorials/intellijUsefulSettings.html

public class Main {
  public static void main(String[] args) {
    DirectoryDoors.makeDoors(); //We create the Doors here
    DirectoryAreas.getInstance(); //We make de Doors part of Areas with calling the constructor
    DirectoryUsers.makeUsers();
    new WebServer();
  }
}
