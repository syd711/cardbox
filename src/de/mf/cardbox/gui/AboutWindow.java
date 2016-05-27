package de.mf.cardbox.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Shows a window with information about the game.
 */
public class AboutWindow extends JDialog {
  private MainWindow mainWindow = null;

  /**
   * Create a new AboutWindow-Object and display it
   *
   */
  AboutWindow(MainWindow mainWindow) {
    super(mainWindow, "About", true);

    // north, cold & icy
    JPanel onTop = new JPanel();
    onTop.add(new JLabel(new ImageIcon("de/mf/cardbox/gui/images/robotgame.gif")));
    this.getContentPane().add(onTop, BorderLayout.NORTH);

    JPanel content = new JPanel();
    content.setLayout(new GridLayout(4, 1));
    JLabel label1 = new JLabel("Der Karteikasten");
    JLabel label2 = new JLabel("Copyright 2003 by Matthias Faust");
    JLabel label3 = new JLabel("Contact: mail@matthias-faust.de");
    JLabel label4 = new JLabel("Version 2.1");

    content.add(label1);
    content.add(label2);
    content.add(label3);
    content.add(label4);
    onTop.add(content, BorderLayout.CENTER);

    // gone south for the winter
    JButton button = new JButton("OK");
    button.addActionListener(new ActionListener() {
                               public void
                               actionPerformed(ActionEvent e) {
                                 dispose();
                               }
                             }
    );
    this.getContentPane().add(button, BorderLayout.SOUTH);

    // ok, done. Now show it to the world
    this.setSize(230, 140);
  }
}
