package de.mf.cardbox.gui;

import de.mf.cardbox.ControllerCardBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class MainMenuBar extends JMenuBar {
  private JMenuItem newConf = null;
  private MainWindow main = null;

  private JMenu databasesMenu = null;
  private JMenu editMenu = null;
  private JMenuItem searchEntryItem = null;
  private JMenuItem exitItem = null;

  private JMenu extrasMenu = null;
  private JMenuItem trainerMenuItem = null;
  private JMenuItem backupMenuItem = null;

  private JMenu aboutMenu = null;
  private JMenuItem aboutMenuItem = null;

  private VocableTrainer vocableTrainerWindow = null;
  private AboutWindow aboutWindow = null;
  private SearchDialog dialog = null;


  /**
   * Method MainMenuBar.
   *
   * @param main
   */
  public MainMenuBar(MainWindow main) {
    this.main = main;
    this.add(createDatabaseMenu());
    this.add(createEditMenu());
    this.add(createExtrasMenu());
    this.add(createAboutMenu());
  }

  /**
   * Method createAboutMenu.
   *
   * @return JMenu
   */
  private JMenu createAboutMenu() {
    aboutMenu = new JMenu("Hilfe");
    aboutMenu.setMnemonic('h');
    aboutMenuItem = new JMenuItem("Info");
    aboutMenuItem.setMnemonic('i');
    aboutMenuItem.addActionListener(new ActionListener() {
                                      public void actionPerformed(ActionEvent e) {
                                        if(aboutWindow != null && aboutWindow.isVisible())
                                          aboutWindow.dispose();

                                        aboutWindow = new AboutWindow(main);
                                        main.centerWindow(aboutWindow);
                                        aboutWindow.setVisible(true);
                                      }
                                    }
    );


    aboutMenu.add(aboutMenuItem);

    return aboutMenu;
  }

  /**
   * Method createExtrasMenu.
   *
   * @return JMenu
   */
  private JMenu createExtrasMenu() {
    extrasMenu = new JMenu("Extras");
    extrasMenu.setMnemonic('e');
    trainerMenuItem = new JMenuItem("Vokabeltrainer");
    trainerMenuItem.setMnemonic('v');

    trainerMenuItem.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                          if(ControllerCardBox.getInstance().getSelectedDatabase() != null) {
                                            if(vocableTrainerWindow != null && vocableTrainerWindow.isVisible())
                                              vocableTrainerWindow.dispose();

                                            vocableTrainerWindow = new VocableTrainer(main);
                                            main.centerWindow(vocableTrainerWindow);
                                            vocableTrainerWindow.setVisible(true);
                                          }
                                        }
                                      }
    );

    backupMenuItem = new JMenuItem("FTP-Backup");
    backupMenuItem.setEnabled(false);
    backupMenuItem.setMnemonic('b');

    extrasMenu.add(trainerMenuItem);
    extrasMenu.add(backupMenuItem);
    return extrasMenu;
  }


  /**
   * Method createEditMenu.
   *
   * @return JMenu
   */
  private JMenu createEditMenu() {
    editMenu = new JMenu("Bearbeiten");
    editMenu.setMnemonic('b');
    searchEntryItem = new JMenuItem("Suchen...");
    searchEntryItem.setMnemonic('s');

    searchEntryItem.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                          if(dialog != null && dialog.isVisible())
                                            dialog.dispose();

                                          dialog = new SearchDialog(main);
                                          main.centerWindow(dialog);
                                          dialog.setVisible(true);
                                        }
                                      }
    );

    editMenu.add(searchEntryItem);
    return editMenu;
  }


  /**
   * Method createGameMenu.
   *
   * @return JMenu
   */
  private JMenu createDatabaseMenu() {
    databasesMenu = new JMenu("Datenbank");
    databasesMenu.setMnemonic('d');

    newConf = new JMenuItem("Neu");
    newConf.setMnemonic('n');
    newConf.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                  String database = JOptionPane.showInputDialog(main,
                                      "Name der neuen Datenbank:",
                                      "Neue Datenbank", JOptionPane.CANCEL_OPTION);

                                  if(database != null && !database.equalsIgnoreCase("")) {
                                    ControllerCardBox.getInstance().selectDatabase(database);
                                    JMenuItem newItem = new JMenuItem(database);
                                    databasesMenu.add(newItem);
                                    databasesMenu.updateUI();
                                    main.reloadTabs();
                                    main.setButtonsEnabled(true);
                                  }
                                }
                              }
    );

    databasesMenu.add(newConf);
    databasesMenu.addSeparator();

    List<String> databases = main.getDloCardBox().getDatabases();

    for(String name : databases) {
      JMenuItem aDatabase = new JMenuItem(name);
      aDatabase.addActionListener(e -> {
            String database = ((JMenuItem) e.getSource()).getText();
            ControllerCardBox.getInstance().selectDatabase(database);
          }
      );
      databasesMenu.add(aDatabase);
    }


    databasesMenu.addSeparator();

    exitItem = new JMenuItem("Schliessen");
    databasesMenu.add(exitItem);

    exitItem.addActionListener(e -> System.exit(0));


    return databasesMenu;
  }
}

