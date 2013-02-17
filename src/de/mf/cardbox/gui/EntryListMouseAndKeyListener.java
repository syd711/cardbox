package de.mf.cardbox.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;

/**
 * @author Syd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class EntryListMouseAndKeyListener implements KeyListener, MouseListener
{
	private LetterPanel letterPanel			 = null;

	public EntryListMouseAndKeyListener( LetterPanel letterPanel )
	{
		this.letterPanel = letterPanel;	
	}

	/**
	 * Constructor for EntryListMouseAndKeyListener.
	 */
	public EntryListMouseAndKeyListener()
	{
		super();
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
		if( arg0.getKeyCode() == 10 )
			letterPanel.getMainWindow().openEntry();
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0)
	{
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0)
	{
		if( arg0.getClickCount() == 2 )
		{
			letterPanel.getMainWindow().openEntry();
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0)
	{
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0)
	{
	}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0)
	{
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0)
	{
	}

}
