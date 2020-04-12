/**
  * This is the lower  pannel in the upper right panel of the Bottom Right 
  * pannel.  It contains two other panels that hold a menubar and a title 
  * for the Attributes table below it.
  *
  * Author: Adam Chipalowsky
  *
  * @version    $Id: BottomRightLowerLowerPanel.java,v 1.6 2000/05/08 03:32:51 p361-45a Exp $
  *
  * @author     
  *
  * Revisions:
  *     $Log: BottomRightLowerLowerPanel.java,v $
  *     Revision 1.6  2000/05/08 03:32:51  p361-45a
  *     fixed formatting
  *
  *     Revision 1.5  2000/04/24 16:13:46  p361-45a
  *     finished method headers
  *
  *     Revision 1.4  2000/04/03 17:44:04  aec1324
  *     added another column to the check boxes and anoher label
  *
  *     Revision 1.3  2000/03/30 00:26:31  aec1324
  *     Added border and sized to fit
  *
  *     Revision 1.2  2000/03/29 21:45:49  aec1324
  *     resized to fit nicer with the frames around it
  *
  *     Revision 1.1  2000/03/29 02:05:20  aec1324
  *     Initial revision
  *
  */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BottomRightLowerLowerPanel extends JPanel {
    private Dimension dimen = new Dimension( 275, 40 );
    private JLabel label = new JLabel( "Section and Room Attributes" );
    
    private Font font, headerFont;
    private Border etched;
    
    /**
     * constructor to create an object of type BottomRightLowerLowerPanel
     *
     */
    public BottomRightLowerLowerPanel() {
	//this.setBackground( Color.white );
	this.setPreferredSize( dimen );
	font = new Font( "Attributes in Room", Font.PLAIN, 20 );

	label.setFont( font );
	
	etched = BorderFactory.createEtchedBorder();
	this.setBorder( etched );
	this.add( label, BorderLayout.NORTH );
	//this.setBorder( etched );
    }
}
