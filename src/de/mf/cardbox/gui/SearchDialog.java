package de.mf.cardbox.gui;

import de.mf.cardbox.ControllerCardBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class SearchDialog extends JDialog {
  private MainWindow mainWindow = null;
  private DefaultListModel model = null;

  private JCheckBox textCheckBox = null;
  private JCheckBox titleCheckBox = null;
  private JTextField searchTextField = null;

  private JButton searchButton = null;
  private JButton cancelButton = null;

  private JList resultList = null;
  private JTextArea resultField = null;

  private Hashtable result = null;
  private SearchDialogKeyListener keyListener = null;
  private SearchDialogListSelectionListener selectionListener = null;


  /**
   * Constructor for SearchDialog.
   *
   * @throws HeadlessException
   */
  public SearchDialog(MainWindow mainWindow) throws HeadlessException {
    super(mainWindow, "Suche", true);
    this.model = new DefaultListModel();
    this.keyListener = new SearchDialogKeyListener(this);
    this.selectionListener = new SearchDialogListSelectionListener(this);
    this.mainWindow = mainWindow;
    this.addKeyListener(keyListener);
    init();
  }

  private void init() {
    this.getContentPane().setLayout(new BorderLayout());
    JPanel rootPanel = new JPanel();
    rootPanel.setLayout(new GridLayout(1, 2));

    JPanel searchOptionsPanel = new JPanel();
    JPanel resultPanel = new JPanel();

    rootPanel.add(this.createSearchOptionsPanel(searchOptionsPanel));
    rootPanel.add(this.createResultPanel(resultPanel));

    this.getContentPane().add(rootPanel, BorderLayout.CENTER);
    this.setSize(550, 400);
  }

  /**
   * Method createResultPanel.
   *
   * @param resultPanel
   * @return JPanel
   */
  private JPanel createResultPanel(JPanel resultPanel) {
    resultPanel.setLayout(new GridLayout(2, 1));
    resultPanel.setBorder(BorderFactory.createTitledBorder("Suchergebnisse"));

    // build list
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BorderLayout());
    JPanel textfieldPanel = new JPanel();
    textfieldPanel.setLayout(new BorderLayout());

    this.resultList = new JList();
    this.resultList.addListSelectionListener(selectionListener);
    this.resultList.setModel(model);
    JScrollPane scrollPaneList = new JScrollPane(resultList);
    listPanel.add(scrollPaneList, BorderLayout.CENTER);

    // build resultfield
    this.resultField = new JTextArea();
    this.resultField.setEditable(false);
    this.resultField.setBorder(BorderFactory.createLoweredBevelBorder());
    JScrollPane scrollPaneField = new JScrollPane(resultField);
    textfieldPanel.add(scrollPaneField);

    resultPanel.add(listPanel);
    resultPanel.add(textfieldPanel, BorderLayout.CENTER);

    return resultPanel;
  }

  /**
   * Method createSearchOptionsPanel.
   *
   * @param searchPanel
   * @return JPanel
   */
  private JPanel createSearchOptionsPanel(JPanel searchPanel) {
    searchPanel.setLayout(new BorderLayout());
    JPanel checkBoxPanel = new JPanel();
    checkBoxPanel.setLayout(new GridLayout(7, 1));
    checkBoxPanel.setBorder(BorderFactory.createTitledBorder("Suchoptionen"));

    this.textCheckBox = new JCheckBox("Inhalt");
    this.titleCheckBox = new JCheckBox("�berschriften              ");
    this.titleCheckBox.setSelected(true);
    this.searchTextField = new JTextField();
    this.searchTextField.addKeyListener(keyListener);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2));
    this.searchButton = new JButton("Suchen");
    this.searchButton.setMnemonic('s');
    this.cancelButton = new JButton("Abbrechen");
    this.cancelButton.setMnemonic('c');
    buttonPanel.add(searchButton);
    buttonPanel.add(cancelButton);

    checkBoxPanel.add(new JLabel("Suchen in:"));
    checkBoxPanel.add(textCheckBox);
    checkBoxPanel.add(titleCheckBox);
    checkBoxPanel.add(new JLabel("Suchtext:"));
    checkBoxPanel.add(searchTextField);
    checkBoxPanel.add(new JLabel(""));
    checkBoxPanel.add(buttonPanel);

    searchPanel.add(checkBoxPanel, BorderLayout.NORTH);


    // searchButton
    searchButton.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent e) {
                                       startSearch();
                                     }
                                   }
    );


    // cancelButton
    cancelButton.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent e) {
                                       dispose();
                                     }
                                   }
    );
    return searchPanel;
  }


  /**
   * Method startSearch.
   */
  public void startSearch() {
    if(searchTextField.getText() != null && searchTextField.getText().length() > 0) {
      this.resultField.setText("");
      result = mainWindow.getDloCardBox().findEntries(searchTextField.getText(), ControllerCardBox.getInstance().getSelectedDatabase(),
          titleCheckBox.isSelected(), textCheckBox.isSelected());
      Iterator it = result.keySet().iterator();

      model.removeAllElements();
      while(it.hasNext()) {
        String title = (String) it.next();
        model.addElement(title);
      }
      resultList.updateUI();
    }
  }


  /**
   * Method showSelectedEntry.
   *
   * @param title
   */
  public void showSelectedEntry() {
    String title = (String) resultList.getSelectedValue();

    if(title != null) {
      String explanation = (String) result.get(title);
      this.resultField.setText(explanation);
    }
  }
}
