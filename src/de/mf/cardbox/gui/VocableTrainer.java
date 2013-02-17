package de.mf.cardbox.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Syd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class VocableTrainer extends JDialog
{
	private MainWindow mainWindow			 = null;
	private JLabel vocableField		 = null;
	private JTextField translationField	 = null;
	
	private JLabel vocableLabel			 = null;
	private JLabel translationLabel		 = null;
	private JLabel correctLabel			 = null;
	
	private JButton nextButton				 = null;
	private JButton cancelButton			 = null;
	
	private Hashtable allEntries			 = null;
	
	private String correctTranslation		 = "";
	private String transatedVocable		 = "";
	private String userInput				 = "";
	
	private String nextVocable				 = "";
	
	private final String CORRECT_LABEL_TEXT 		= "   Korrekte Antwort:     ";
	private final String VOCABLE_LABEL_TEXT 		= "   Gefragt wurde:		";
	private final String TRANSLATION_LABEL_TEXT	= "   Übersetzt wurde:		";
	
	private VocableTrainerKeyListener listener = null;
	
	/**
	 * Constructor for VocableTrainer.
	 * @throws HeadlessException
	 */
	public VocableTrainer( MainWindow mainWindow ) 
	{
		super( mainWindow, "Vokabeltrainer", true );
		this.mainWindow 	= mainWindow;
		this.listener		= new VocableTrainerKeyListener( this );
		this.addKeyListener( listener );
		this.allEntries 	= mainWindow.getDloCardBox().getAllEntries( mainWindow.getSelectedDatabaseName() );
		this.nextVocable 	= this.getNextVocable();
		init();
	}


	private void init()
	{
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout( new BorderLayout() );
		
		// vocablelabel
		JPanel vocablePanel			= new JPanel();
		vocablePanel.setLayout( new GridLayout(3, 1) );
		
		JPanel firstLabelPanel	= new JPanel();
		firstLabelPanel.setLayout( new GridLayout( 1,2 ) );		
		JLabel firstLabel 		= new JLabel( VOCABLE_LABEL_TEXT );
		vocableLabel 			= new JLabel("");
		firstLabelPanel.add( firstLabel );		
		firstLabelPanel.add( vocableLabel );
		
		
		JPanel secondLabelPanel	= new JPanel();
		secondLabelPanel.setLayout( new GridLayout( 1,2 ) );
		JLabel secondLabel		= new JLabel( TRANSLATION_LABEL_TEXT );
		translationLabel		= new JLabel("");
		secondLabelPanel.add( secondLabel );
		secondLabelPanel.add( translationLabel );
		
		JPanel thirdLabelPanel	= new JPanel();
		thirdLabelPanel.setLayout( new GridLayout( 1,2 ) );
		JLabel thirdLabel		= new JLabel( CORRECT_LABEL_TEXT );
		correctLabel			= new JLabel("");
		thirdLabelPanel.add( thirdLabel );
		thirdLabelPanel.add( correctLabel );
		
		vocablePanel.add( firstLabelPanel );
		vocablePanel.add( secondLabelPanel );
		vocablePanel.add( thirdLabelPanel );
		vocablePanel.setBorder( BorderFactory.createTitledBorder( "Ergebnis " ));
		
		// translationfield
		JPanel midPanel = new JPanel();
		midPanel.setLayout( new GridLayout( 2,1 ) );
		
		JPanel translationPanel		= new JPanel();
		translationPanel.setLayout( new BorderLayout() );
		translationField = new JTextField();
		translationField.addKeyListener( listener );
		translationPanel.setBorder( BorderFactory.createTitledBorder( "Übersetzung?") );
		translationPanel.add( translationField, BorderLayout.CENTER );
		
		//vocableField
		JPanel vocableFieldPanel		= new JPanel();
		vocableFieldPanel.setLayout( new BorderLayout() );
		vocableField = new JLabel( "    " + nextVocable );
		vocableFieldPanel.setBorder( BorderFactory.createTitledBorder( "Vokabel") );
		vocableFieldPanel.add( vocableField );
		
		midPanel.add( vocableFieldPanel );
		midPanel.add( translationPanel );
		
		
		// Buttons
		JPanel buttonPanel	= new JPanel();
		buttonPanel.setLayout( new FlowLayout()  );
		nextButton			= new JButton("Weiter");
		nextButton.setMnemonic( 'w' );
		cancelButton 		= new JButton( "Abbrechen" );
		cancelButton.setMnemonic( 'c' );
		buttonPanel.add( nextButton );
		buttonPanel.add( cancelButton );
		
	    nextButton.addActionListener( new ActionListener ()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
				checkVocable();
			}
	      }
	     );		
	    cancelButton.addActionListener( new ActionListener ()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
				dispose();	        	
			}
	      }
	     );		     	
		
		rootPanel.add( vocablePanel, BorderLayout.NORTH );
		rootPanel.add( midPanel, BorderLayout.CENTER );
		rootPanel.add( buttonPanel, BorderLayout.SOUTH );
		
		this.getContentPane().add( rootPanel );	
		this.setTitle( "Vokabeltrainer" );
		this.setSize( 400, 250 );
	}

	/**
	 * Method checkVocable.
	 */
	public void checkVocable()
	{	
		String translatedText 	= translationField.getText();
		String correctText		= allEntries.get( nextVocable ).toString();
		this.correctLabel.setText( correctText );
		this.translationLabel.setText( translatedText );
		this.vocableLabel.setText( nextVocable );
		
		if( correctText.trim().indexOf( translatedText.trim() ) == -1 )
			translationLabel.setForeground( new Color( 255,0,0 ) );
		else
		 	translationLabel.setForeground( new Color( 0,0,0 ) );
		
		this.nextVocable = getNextVocable();	
		this.vocableField.setText( "   " + nextVocable );
		this.translationField.setText("");
	}
	
	/**
	 * Method getNextVocable.
	 * @return String
	 */
	private String getNextVocable()
	{
		Random r = new Random();
		String vocable = "";
		
		Iterator it = allEntries.keySet().iterator();
		int num = Math.abs(r.nextInt()) % allEntries.size();
	
		int counter = 0;
		while( counter != num )
		{
			vocable = (String)it.next();
			counter++;	
		}
		if( counter == 0 )
			return (String)it.next();
		else
			return vocable;
	}

}
