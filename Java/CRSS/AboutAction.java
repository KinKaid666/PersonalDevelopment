//$Id: AboutAction.java,v 1.5 2000/05/08 01:44:50 p361-45a Exp $
//Author:        Dan Cooley
//Contributors:
//Description:   about popup menu
//$Log: AboutAction.java,v $
//Revision 1.5  2000/05/08 01:44:50  p361-45a
//fixed format
//
//Revision 1.4  2000/05/08 01:04:38  p361-45a
//added ok button and set size
//
//Revision 1.3  2000/05/03 16:34:13  p361-45a
//AboutAction compiles
//
//Revision 1.2  2000/05/02 02:39:25  p361-45a
//made frame with one ok button and about message
//
//Revision 1.1  2000/05/01 17:48:13  p361-45a
//Initial revision
//
import java.io.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.AbstractButton;
import javax.swing.text.*;
import java.lang.*;
import java.awt.Dimension;
import java.awt.Window;


public class AboutAction extends CRSSAction {
    private JFrame frame;
    private JPanel panel; 
    private JButton okButton = null;
    final String okButtonString = "Ok";
    final String frameLabel = "About CRSS";
    private JLabel aboutLabel1;
    private JLabel aboutLabel2;
    private JLabel aboutLabel3;
    private JLabel aboutLabel4;
    private String aboutMsg1;
    private String aboutMsg2;
    private String aboutMsg3;
    private String aboutMsg4;
    private String version = "1.0";
    private String releaseDate = "May 10";
    private Dimension dimension = new Dimension(370, 200);
    private Dimension buttonDimension = new Dimension(50,40);
    protected int size = 20;
    private String fontName = "SansSerif";
    public Font font1 = new Font(fontName, 2, size);
   
    /**
     * constructor to create an about window object
     *
     * @param myGUI a value of type 'GUI'
     */
    public AboutAction(GUI myGUI) {
	
	_gooey = myGUI;

	go();
    }
    /**
     * method that opens about window
     *
     */
    public void go() {
    	
	//message to appear in window
	          
	aboutMsg1 = "                   Course Room";
	aboutMsg2 = "                Scheduling System";
	aboutMsg3 = "                     version: " + version;
	aboutMsg4 = "               Release Date:" + releaseDate;
 
	//initialize about messages
	aboutLabel1 = new JLabel(aboutMsg1);
	aboutLabel2 = new JLabel(aboutMsg2);
	aboutLabel3 = new JLabel(aboutMsg3);
	aboutLabel4 = new JLabel(aboutMsg4);
	//frame to display message
	
	frame = new JFrame( frameLabel );
	
	frame.setResizable( true );
	panel = new JPanel(new GridLayout(5,0));
	panel.setSize(dimension);
	frame.setSize(dimension);

	//set font
	aboutLabel1.setFont(font1);
	aboutLabel2.setFont(font1);
	aboutLabel3.setFont(font1);
	aboutLabel4.setFont(font1);
	
	//add labels to panel
	panel.add(aboutLabel1);
	panel.add(aboutLabel2);
	panel.add(aboutLabel3);
	panel.add(aboutLabel4);
	
	// button to close about window
        okButton = new JButton( okButtonString );
	okButton.setSize( buttonDimension );
	panel.add(okButton);

	frame.getContentPane().add( panel, BorderLayout.CENTER );

	//add listener for the button
        okButton.addActionListener( new ActionListener() {
	    public void actionPerformed( ActionEvent e ) {
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.dispose();
	    }
	});	

	//add panel to frame
	frame.getContentPane().add( panel, BorderLayout.CENTER );
	frame.setVisible( true );
    }

}
