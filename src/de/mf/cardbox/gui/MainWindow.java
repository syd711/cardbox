package de.mf.cardbox.gui;

import de.mf.cardbox.ControllerCardBox;
import de.mf.cardbox.db.DLOCardBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;


/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class MainWindow extends JFrame {

  public static int H_SIZE = 20;
  public static int V_SIZE = 20;

  private JTabbedPane tabPane = null;
  private MainMenuBar mainMenuBar = null;
  private DLOCardBox dloCardBox = null;

  private JPanel buttonPanel = null;

  private JButton deleteButton = new JButton();
  private JButton newButton = new JButton();
  private JButton showButton = new JButton();
  private JButton nextButton = new JButton();
  private JButton prevButton = new JButton();

  private TabChangeListener changeListener = null;
  private ArrayList alphabetList = new ArrayList();

  public MainWindow() {
    this.dloCardBox = new DLOCardBox();
    this.buttonPanel = this.createButtonPanel();
    this.changeListener = new TabChangeListener(this);


    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    // build basic layout
    getContentPane().setLayout(new BorderLayout());

    tabPane = new JTabbedPane(JTabbedPane.TOP);
    tabPane.addChangeListener(changeListener);

    JPanel letterPanel = new JPanel();

    String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    for(int i = 0; i < alphabet.length; i++) {
      tabPane.add(alphabet[i], new LetterPanel(alphabet[i], this));
      alphabetList.add(i, alphabet[i]);
    }

    this.getContentPane().add(tabPane);

    // adding the menubar
    mainMenuBar = new MainMenuBar(this);
    this.setJMenuBar(mainMenuBar);

    // various settings
    this.setButtonsEnabled(false);


    pack();

    ((LetterPanel) this.tabPane.getSelectedComponent()).setButtonPanel(this.getButtonPanel());
    this.setSize(780, 600);
    this.centerWindow(this);
    setResizable(true);

    setVisible(true);
  }

  /**
   * Method getLetter.
   *
   * @return String
   */
  public String getLetter() {
    return tabPane.getTitleAt(tabPane.getSelectedIndex());
  }


  public void setButtonsEnabled(boolean enable) {
    this.newButton.setEnabled(enable);
    this.deleteButton.setEnabled(enable);
    this.showButton.setEnabled(enable);
    this.nextButton.setEnabled(enable);
    this.prevButton.setEnabled(enable);
  }


  /**
   * Method centerWindow.
   *
   * @param frame
   */
  public Component centerWindow(Component frame) {
    // das Fenster in des Mittelpunkt des Screens setzen
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int sizeX = dimension.width;
    int sizeY = dimension.height;
    frame.setLocation(dimension.width / 2 - (frame.getSize().width / 2), dimension.height / 2 - (frame.getSize().height / 2));
    return frame;
  }


  /**
   * Method reloadTabs.
   */
  public void reloadTabs() {
    int tabCount = tabPane.getTabCount();

    for(int i = 0; i < tabCount; i++) {
      LetterPanel letterPanel = (LetterPanel) tabPane.getComponentAt(i);
      ((DefaultListModel) letterPanel.getEntryList().getModel()).removeAllElements();
      letterPanel.setEntriesForLetter();
    }
  }


  /**
   * Method createButtonPanel.
   *
   * @return JPanel
   */
  private JPanel createButtonPanel() {
    this.buttonPanel = new JPanel();
    // set Button labels
    nextButton.setText(">>");
    prevButton.setText("<<");
    deleteButton.setText("Löschen");
    deleteButton.setMnemonic('l');
    showButton.setText("Anzeigen");
    showButton.setMnemonic('a');
    newButton.setText("Neu");
    newButton.setMnemonic('n');

    ButtonPanelActionListener buttonActionListener = new ButtonPanelActionListener(this);

    // add buttons
    buttonPanel.add(prevButton, null);
    buttonPanel.add(newButton, null);
    buttonPanel.add(showButton, null);
    buttonPanel.add(deleteButton, null);
    buttonPanel.add(nextButton, null);

    nextButton.setActionCommand(ButtonPanelActionListener.ACTION_COMMAND_NEXT);
    nextButton.addActionListener(buttonActionListener);

    prevButton.setActionCommand(ButtonPanelActionListener.ACTION_COMMAND_PREV);
    prevButton.addActionListener(buttonActionListener);

    showButton.setActionCommand(ButtonPanelActionListener.ACTION_COMMAND_VIEW);
    showButton.addActionListener(buttonActionListener);

    newButton.setActionCommand(ButtonPanelActionListener.ACTION_COMMAND_NEW);
    newButton.addActionListener(buttonActionListener);

    deleteButton.setActionCommand(ButtonPanelActionListener.ACTION_COMMAND_DELETE);
    deleteButton.addActionListener(buttonActionListener);

    return buttonPanel;
  }

  /**
   * Method getButtonPanel.
   *
   * @return JPanel
   */
  public JPanel getButtonPanel() {
    return this.buttonPanel;
  }


  /**
   * Returns the dloCardBox.
   *
   * @return DLOCardBox
   */
  public DLOCardBox getDloCardBox() {
    return dloCardBox;
  }

  /**
   * Method getSelectedLetterPanel.
   */
  public LetterPanel getSelectedLetterPanel() {
    return (LetterPanel) this.tabPane.getSelectedComponent();
  }

  /**
   * Method getSelectedLetterPanel.
   */
  public String getSelectedLetter() {
    return this.tabPane.getSelectedComponent().getName();
  }

  /**
   * Method updateTab.
   *
   * @param letter
   */
  public void updateTab(String letter) {
    LetterPanel letterPanel = (LetterPanel) tabPane.getComponentAt(new Integer(alphabetList.indexOf(letter)).intValue());
    ((DefaultListModel) letterPanel.getEntryList().getModel()).removeAllElements();
    letterPanel.setEntriesForLetter();
  }

  /**
   * Returns the tabPane.
   *
   * @return JTabbedPane
   */
  public JTabbedPane getTabPane() {
    return tabPane;
  }


  public void openEntry() {
    LetterPanel letterPanel = this.getSelectedLetterPanel();
    JList entryList = letterPanel.getEntryList();

    if(entryList.getSelectedIndex() != -1) {
      String title = letterPanel.getEntryList().getSelectedValue().toString();
      String text = letterPanel.getMainWindow().getDloCardBox().getEntry(letterPanel.getLetter(), title, ControllerCardBox.getInstance().getSelectedDatabase());

      InputDialog entry = new InputDialog(letterPanel, title, text, true);
      entry.setSize(500, 450);
      letterPanel.getMainWindow().centerWindow(entry);
      entry.setTitle("Eintrag '" + letterPanel.getEntryList().getSelectedValue() + "'");
      entry.setVisible(true);
    }
  }
}

