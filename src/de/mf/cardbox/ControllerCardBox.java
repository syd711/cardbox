package de.mf.cardbox;

import de.mf.cardbox.gui.MainWindow;

/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class ControllerCardBox {
  private static ControllerCardBox controllerInstance = null;

  private MainWindow mainWindow = null;
  private String selectedDatabase = null;

  /**
   * Constructor for ControllerCardBox.
   */
  private ControllerCardBox() {
  }

  /**
   * Method getInstance.
   *
   * @return ControllerCardBox
   */
  public static ControllerCardBox getInstance() {
    if(controllerInstance == null) {
      controllerInstance = new ControllerCardBox();
      return controllerInstance;
    }
    else
      return controllerInstance;
  }

  /**
   * Method initController.
   */
  private void initController() {
    MainWindow mainWindow = new MainWindow();
    this.mainWindow = mainWindow;

    java.util.List<String> databases = this.mainWindow.getDloCardBox().getDatabases();
    if(!databases.isEmpty()) {
      ControllerCardBox.getInstance().selectDatabase(databases.get(0));
    }
  }


  /**
   * Method selectDatabase.
   *
   * @param database
   */
  public void selectDatabase(String database) {
    if(database != null && (this.selectedDatabase == null || !database.equals(this.selectedDatabase))) {
      this.selectedDatabase = database;
      mainWindow.reloadTabs();
      mainWindow.setButtonsEnabled(true);
    }
    mainWindow.setTitle("Karteikasten: " + database);
  }

  /**
   * Method main.
   *
   * @param args
   */
  public static void main(String[] args) {
    ControllerCardBox controller = ControllerCardBox.getInstance();
    controller.initController();
  }


  /**
   * Returns the mainWindow.
   *
   * @return MainWindow
   */
  public MainWindow getMainWindow() {
    return mainWindow;
  }

  public String getSelectedDatabase() {
    return selectedDatabase;
  }

  public void setSelectedDatabase(String selectedDatabase) {
    this.selectedDatabase = selectedDatabase;
  }
}
