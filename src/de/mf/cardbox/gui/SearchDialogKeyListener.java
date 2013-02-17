package de.mf.cardbox.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * @author Syd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SearchDialogKeyListener implements KeyListener
{
	private SearchDialog searchDialog			 = null;
	
	
	/**
	 * Constructor for SearchDialogKeyListener.
	 */
	public SearchDialogKeyListener( SearchDialog searchDialog )
	{
		this.searchDialog = searchDialog;
	}

	/**
	 * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0)
	{
	}

	/**
	 * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
	 */
	public void keyPressed(KeyEvent arg0)
	{
		if( arg0.getSource() instanceof JTextField && arg0.getKeyChar() == KeyEvent.VK_ENTER )				
		{
			searchDialog.startSearch();
		}		
		else if( arg0.getKeyChar() == KeyEvent.VK_ESCAPE )
		{
			searchDialog.dispose();
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0)
	{
	}

}
