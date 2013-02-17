package de.mf.cardbox.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import de.mf.cardbox.ControllerCardBox;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class LetterPanel extends JPanel
{
  private JPanel rootPanel				= new JPanel();
  
  private JList entryList 				= null;
  private String letter 				= null;
  private MainWindow mainWindow		= null;
  private DefaultListModel model		= null;
  
/**
 * Method LetterPanel.
 * @param letter
 * @param mainWindow
 */
  public LetterPanel( String letter, MainWindow mainWindow )
  {
  	  this.letter = letter;
      this.mainWindow = mainWindow;
      model = new DefaultListModel(); 

	  init();
  }



  /**
   *
   * @throws Exception
   */
  private void init() 
  {
  	this.setLayout( new BorderLayout() );
  	
	this.rootPanel = new JPanel();
	this.rootPanel.setBorder( BorderFactory.createLoweredBevelBorder() );
    rootPanel.setLayout( new BorderLayout() );

	this.entryList		= new JList();
	
	EntryListMouseAndKeyListener listener = new EntryListMouseAndKeyListener( this );
	
	this.entryList.addMouseListener( listener );
	this.entryList.addKeyListener( listener );
	this.entryList.setModel( model );
	entryList.setAutoscrolls( true );
	
	JScrollPane scrollPane = new JScrollPane(entryList);


	rootPanel.add( mainWindow.getButtonPanel(), BorderLayout.NORTH );
	rootPanel.add( scrollPane, BorderLayout.CENTER );
	//rootPanel.add( new JTextArea(), BorderLayout.SOUTH );
	
	this.add( rootPanel, BorderLayout.CENTER );
  }


	/**
	 * Method setEntriesForLetter.
	 * @param letter
	 */
  public void setEntriesForLetter()
  {
	String database 	= ControllerCardBox.getInstance().getMainWindow().getSelectedDatabaseName().toUpperCase();
	String entries[]	= mainWindow.getDloCardBox().getEntriesForLetter( letter, database );
	
	this.entryList.removeAll();
	
	for( int i=0; i<entries.length; i++ )
	{
		model.addElement( entries[i] );
	}
	this.entryList.updateUI();
  }


	/**
	 * Method setButtonPanel.
	 * @param buttonPanel
	 */
	public void setButtonPanel( JPanel buttonPanel )
	{
		this.add( buttonPanel, BorderLayout.NORTH );
	}


	/**
	 * Returns the entryList.
	 * @return JList
	 */
	public JList getEntryList()
	{
		return entryList;
	}

	/**
	 * Returns the letter.
	 * @return String
	 */
	public String getLetter()
	{
		return letter;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

}
