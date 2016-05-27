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
public class VocableTrainerKeyListener implements KeyListener {
  private VocableTrainer trainer = null;


  /**
   * Constructor for VocableTrainerKeyListener.
   */
  public VocableTrainerKeyListener(VocableTrainer trainer) {
    this.trainer = trainer;
  }

  /**
   * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
   */
  public void keyTyped(KeyEvent arg0) {
  }

  /**
   * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
   */
  public void keyPressed(KeyEvent arg0) {
    if(arg0.getSource() instanceof JTextField && arg0.getKeyChar() == KeyEvent.VK_ENTER) {
      trainer.checkVocable();
    }
    else if(arg0.getKeyChar() == KeyEvent.VK_ESCAPE) {
      trainer.dispose();
    }
  }

  /**
   * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
   */
  public void keyReleased(KeyEvent arg0) {
  }

}
