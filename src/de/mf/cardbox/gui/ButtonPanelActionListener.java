package de.mf.cardbox.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * @author Syd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ButtonPanelActionListener implements ActionListener
{
	public final static String ACTION_COMMAND_NEW	 	= "newEntry";
	public final static String ACTION_COMMAND_VIEW		= "viewEntry";
	public final static String ACTION_COMMAND_DELETE		= "deleteEntry";
	
	public final static String ACTION_COMMAND_NEXT		= "next";
	public final static String ACTION_COMMAND_PREV		= "prev";

	private MainWindow mainWindow							= null;
	private InputDialog inputDialog						= null; 

	/**
	 * Constructor for LetterPanelActionListener.
	 */
	public ButtonPanelActionListener( MainWindow mainWindow )
	{
		this.mainWindow = mainWindow;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		String actionCommand 		= arg0.getActionCommand();
		LetterPanel letterPanel		= mainWindow.getSelectedLetterPanel();
		
		/*
		 * New Entry
		 */
		if( actionCommand.equalsIgnoreCase( ACTION_COMMAND_NEW ) )
		{        
		  if( inputDialog != null && inputDialog.isVisible() )
		  	inputDialog.dispose();
		  	 
          inputDialog = new InputDialog( letterPanel, null, null, false );
          inputDialog.setSize(500, 450);
          mainWindow.centerWindow( inputDialog );
          inputDialog.setTitle("Neuer Eintrag");
          inputDialog.show();			
		}
		
		
		/*
		 * View Entry
		 */
		else if( actionCommand.equalsIgnoreCase( ACTION_COMMAND_VIEW ) )
		{
	      if( inputDialog != null && inputDialog.isVisible() )
			  	inputDialog.dispose();			
			  	
          if( letterPanel.getEntryList().getSelectedIndex() != -1 )
          {
           	mainWindow.openEntry();
          }			
			
		}
		
		
		/*
		 * Delete entry
		 */
		else if( actionCommand.equalsIgnoreCase( ACTION_COMMAND_DELETE ) )
		{
          if( letterPanel.getEntryList().getSelectedIndex() != -1 )
          {
            int option = JOptionPane.showConfirmDialog( mainWindow, "Eintrag '" + letterPanel.getEntryList().getSelectedValue() + "' wirklich löschen?", "Achtung", JOptionPane.CANCEL_OPTION, JOptionPane.CANCEL_OPTION, null);
            if( option == 0 )
            {
             	int index 		= letterPanel.getEntryList().getSelectedIndex();
                String entry 	= letterPanel.getEntryList().getSelectedValue().toString();
				letterPanel.getMainWindow().getDloCardBox().deleteEntry( letterPanel.getLetter(), mainWindow.getSelectedDatabaseName(), entry );
				( (DefaultListModel) letterPanel.getEntryList().getModel() ).remove( index );
				letterPanel.getEntryList().updateUI();
            }			
          }
		}
		
		/*
		 * next entry
		 */
		else if( actionCommand.equalsIgnoreCase( ACTION_COMMAND_NEXT ) )
		{
		  int index = mainWindow.getTabPane().getSelectedIndex();
          if( index <= 24 ) mainWindow.getTabPane().setSelectedIndex( index+1 );			
		}
		
		/*
		 * prev entry
		 */
		else if( actionCommand.equalsIgnoreCase( ACTION_COMMAND_PREV ) )
		{
          int index = mainWindow.getTabPane().getSelectedIndex();
          if( index > 0 ) mainWindow.getTabPane().setSelectedIndex( index-1 );			
		}
	}

}
