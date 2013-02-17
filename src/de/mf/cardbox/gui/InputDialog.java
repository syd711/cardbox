package de.mf.cardbox.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.TitledBorder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class InputDialog extends JDialog
{
  private BorderLayout borderLayout1 		= new BorderLayout();
  private JTextArea inputTextArea 			= new JTextArea();
  private JTextField headlineTextField 	= new JTextField();
  private JButton saveButton 				= new JButton();
  private JButton cancelButton				= new JButton();
  private TitledBorder titleborder			= null;
  private TitledBorder textBorder			= null;

  private LetterPanel letterPanel 			= null;
  private boolean update					= false;
  private InputDialog dialog				= null;
  private InputDialogKeyListener listener	= null;


	/**
	 * Method InputDialog.
	 * @param letterPanel
	 */
  public InputDialog( LetterPanel letterPanel, String title, String text, boolean update )
  {
  	this.setModal( true );
  	this.dialog		 = this;
  	this.update	 	 = update;
   	this.letterPanel	 = letterPanel;
   	this.listener		 = new InputDialogKeyListener( this );
   	if( title == null )
   		title = "";
   	if( text == null )
   		text = "";
    jbInit( title, text );    
  }




/**
 * Method jbInit.
 */
  private void jbInit( String title, String text )
  {
    titleborder 		= new TitledBorder("Titel");
    textBorder 			= new TitledBorder("Beschreibung");
    
	JPanel textPanel = new JPanel();
    textPanel.setLayout( new BorderLayout() );
    textPanel.setBorder(textBorder);
    inputTextArea.setText( text );
    inputTextArea.addKeyListener( listener );
    textPanel.add( new JScrollPane( inputTextArea ), BorderLayout.CENTER );    
    
    
        
    JPanel titlePanel = new JPanel();
    titlePanel.setLayout( new BorderLayout() );
    titlePanel.setBorder(titleborder);
    headlineTextField.setText( title );
    // disable title if it is an update, because the title is the table-key
    headlineTextField.setEditable( !update );
    headlineTextField.addKeyListener( listener );
    titlePanel.add( headlineTextField, BorderLayout.CENTER );



    saveButton.setText("Speichern");
    saveButton.setEnabled( false );
    saveButton.setMnemonic( 's' );
    cancelButton.setText("Abbrechen");
    cancelButton.setMnemonic( 'c' );
    JPanel buttonPanel = new JPanel();
    
    buttonPanel.add( saveButton );
    buttonPanel.add( cancelButton );
    
    
	this.getContentPane().setLayout(borderLayout1);    
    this.getContentPane().add( buttonPanel, BorderLayout.SOUTH);
    this.getContentPane().add( textPanel, BorderLayout.CENTER);
    this.getContentPane().add( titlePanel, BorderLayout.NORTH);




    //speichern
    saveButton.addActionListener( new ActionListener ()
      {
        public void actionPerformed(ActionEvent e)
        {
          saveEntry(); 	
		}
      }
     );

	// cancel
    cancelButton.addActionListener( new ActionListener ()
      {
        public void actionPerformed(ActionEvent e)
        {
        	if( saveButton.isEnabled() )
        	{
        		leaveInputDialog();        		
        	}
          	else
          		setVisible( false );
		}
      }
     );

  }


	/**
	 * Method enableButtons.
	 * @param enable
	 */
	public void enableSaveButton( boolean enable )
	{
		this.saveButton.setEnabled( enable );
	}
	
	public boolean isSaveButtonEnabled()
	{
		return this.saveButton.isEnabled();
	}	
	


	/**
	 * Returns the inputTextArea.
	 * @return JTextArea
	 */
	public JTextArea getInputTextArea()
	{
		return inputTextArea;
	}

	/**
	 * Method saveEntry.
	 */
	public void saveEntry()
	{
			String letter 		= letterPanel.getLetter();
            String database		= letterPanel.getMainWindow().getSelectedDatabaseName();
            String title		= headlineTextField.getText();
            String text			= inputTextArea.getText();
            
            //TODO
            if( update )
	            letterPanel.getMainWindow().getDloCardBox().updateEntry( letter, title, text, database );
	        else
	        	letterPanel.getMainWindow().getDloCardBox().insertEntry( letter, title, text, database );
         	letterPanel.getMainWindow().updateTab( letter );
         	letterPanel.getEntryList().updateUI();	
         	
         	this.enableSaveButton( false );
	}


	/**
	 * Method leaveInputDialog.
	 */
	public void leaveInputDialog()
	{
		int option = JOptionPane.showConfirmDialog( letterPanel.getMainWindow(), "Änderungen speichern?", "Achtung", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.CANCEL_OPTION, null);
		if( option == JOptionPane.YES_OPTION )
		{
			saveEntry();
			dialog.dispose();
		}
		else if( option == JOptionPane.NO_OPTION )
		{
			dialog.dispose();
		}
		else if( option == JOptionPane.CANCEL_OPTION )
		{
			dialog.dispose();
		}		
	}
	
}