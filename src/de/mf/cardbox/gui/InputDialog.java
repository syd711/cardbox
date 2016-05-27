package de.mf.cardbox.gui;

import de.mf.cardbox.ControllerCardBox;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class InputDialog extends JDialog {
  private BorderLayout borderLayout1 = new BorderLayout();
  private JTextArea inputTextArea = new JTextArea();
  private JTextField headlineTextField = new JTextField();
  private JButton saveButton = new JButton();
  private JButton cancelButton = new JButton();
  private JButton prevButton = new JButton();
  private JButton nextButton = new JButton();
  private TitledBorder titleborder = null;
  private TitledBorder textBorder = null;

  private LetterPanel letterPanel = null;
  private boolean update = false;
  private InputDialog dialog = null;
  private InputDialogKeyListener listener = null;


  /**
   * Method InputDialog.
   *
   * @param letterPanel
   */
  public InputDialog(LetterPanel letterPanel, String title, String text, boolean update) {
    this.setModal(true);
    this.dialog = this;
    this.update = update;
    this.letterPanel = letterPanel;
    this.listener = new InputDialogKeyListener(this);
    jbInit();


    if(title == null)
      title = "";
    if(text == null)
      text = "";
    setValues(title, text);
  }

  private void setValues(String title, String text) {
    headlineTextField.setText(title);
    inputTextArea.setText(text);
  }


  /**
   * Method jbInit.
   */
  private void jbInit() {

    titleborder = new TitledBorder("Titel");
    textBorder = new TitledBorder("Beschreibung");

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BorderLayout());
    textPanel.setBorder(textBorder);
    inputTextArea.addKeyListener(listener);
    textPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);


    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BorderLayout());
    titlePanel.setBorder(titleborder);

    // disable title if it is an update, because the title is the table-key
    headlineTextField.setEditable(!update);
    headlineTextField.addKeyListener(listener);
    titlePanel.add(headlineTextField, BorderLayout.CENTER);


    saveButton.setText("Speichern");
    saveButton.setEnabled(false);
    saveButton.setMnemonic('s');
    cancelButton.setText("Abbrechen");
    cancelButton.setMnemonic('c');
    prevButton.setText("<");
    nextButton.setText(">");
    JPanel buttonPanel = new JPanel();

    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    buttonPanel.add(prevButton);
    buttonPanel.add(nextButton);


    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    this.getContentPane().add(textPanel, BorderLayout.CENTER);
    this.getContentPane().add(titlePanel, BorderLayout.NORTH);


    //speichern
    saveButton.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) {
                                     saveEntry();
                                   }
                                 }
    );

    // cancel
    cancelButton.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent e) {
                                       if(saveButton.isEnabled()) {
                                         leaveInputDialog();
                                       }
                                       else
                                         setVisible(false);
                                     }
                                   }
    );

    prevButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });

    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
  }


  /**
   * Method enableButtons.
   *
   * @param enable
   */
  public void enableSaveButton(boolean enable) {
    this.saveButton.setEnabled(enable);
  }

  public boolean isSaveButtonEnabled() {
    return this.saveButton.isEnabled();
  }


  /**
   * Returns the inputTextArea.
   *
   * @return JTextArea
   */
  public JTextArea getInputTextArea() {
    return inputTextArea;
  }

  /**
   * Method saveEntry.
   */
  public void saveEntry() {
    String letter = letterPanel.getLetter();
    String database = ControllerCardBox.getInstance().getSelectedDatabase();
    String title = headlineTextField.getText();
    String text = inputTextArea.getText();

    //TODO
    if(update) {
      letterPanel.getMainWindow().getDloCardBox().updateEntry(letter, title, text, database);
    }
    else {
      try {
        letterPanel.getMainWindow().getDloCardBox().insertEntry(letter, title, text, database, "");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    letterPanel.getMainWindow().updateTab(letter);
    letterPanel.getEntryList().updateUI();

    this.enableSaveButton(false);
  }


  /**
   * Method leaveInputDialog.
   */
  public void leaveInputDialog() {
    int option = JOptionPane.showConfirmDialog(letterPanel.getMainWindow(), "Änderungen speichern?", "Achtung", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.CANCEL_OPTION, null);
    if(option == JOptionPane.YES_OPTION) {
      saveEntry();
      dialog.dispose();
    }
    else if(option == JOptionPane.NO_OPTION) {
      dialog.dispose();
    }
    else if(option == JOptionPane.CANCEL_OPTION) {
      dialog.dispose();
    }
  }

}