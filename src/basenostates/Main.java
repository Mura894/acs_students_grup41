package basenostates;

// Before executing enable assertions :
// https://se-education.org/guides/tutorials/intellijUsefulSettings.html

public class Main {
  // Main entry point of the application
  public static void main(String[] args) {
    DirectoryDoors.getInstance(); // We create the Doors here
    DirectoryAreas.getInstance(); // We make the Doors part of Areas with calling the constructor
    DirectoryUsers.getInstance(); // We create the Users and Groups here
    new WebServer(); // Start the web server to handle requests
  }
}