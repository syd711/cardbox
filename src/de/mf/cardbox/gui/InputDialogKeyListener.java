package de.mf.cardbox.gui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Syd
 *         <p>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class InputDialogKeyListener implements KeyListener {
  private InputDialog inputDialog = null;

  /**
   * Constructor for InputDialogKeyListener.
   */
  public InputDialogKeyListener(InputDialog inputDialog) {
    this.inputDialog = inputDialog;
  }

  /**
   * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
   */
  public void keyTyped(KeyEvent arg0) {

    if(arg0.getSource() instanceof JTextField && arg0.getKeyChar() == KeyEvent.VK_ENTER) {
      inputDialog.getInputTextArea().requestFocus();
    }
    else if(arg0.getKeyChar() == KeyEvent.VK_ESCAPE) {
      if(inputDialog.isSaveButtonEnabled()) {
        inputDialog.leaveInputDialog();
      }
      else
        inputDialog.setVisible(false);
    }
    inputDialog.enableSaveButton(true);
  }

  /**
   * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
   */
  public void keyPressed(KeyEvent arg0) {
  }

  /**
   * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
   */
  public void keyReleased(KeyEvent arg0) {
  }

}
