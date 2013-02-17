package de.mf.cardbox.gui;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Syd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TabChangeListener implements ChangeListener
{
	private MainWindow mainWindow			= null;
	
	/**
	 * Constructor for TabChangeListener.
	 */
	public TabChangeListener( MainWindow mainWindow )
	{
		this.mainWindow = mainWindow;
	}

	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(ChangeEvent)
	 */
	public void stateChanged(ChangeEvent arg0)
	{
		LetterPanel letterPanel = (LetterPanel)((JTabbedPane)arg0.getSource()).getSelectedComponent();
		letterPanel.setButtonPanel( mainWindow.getButtonPanel() );
	}

}
