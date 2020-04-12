/**
  * This is the upper pannel in the Lower right panel.  It contains two other
  * panels that hold a menubar and a title for the Attributes table below it.
  *
  * Author: Adam Chipalowsky
  *
  * @version    $Id: BottomRightUpperPanel.java,v 1.7 2000/04/28 17:05:39 pel2367 Exp $
  *
  * @author     
  *
  * Revisions:
  *     $Log: BottomRightUpperPanel.java,v $
  *     Revision 1.7  2000/04/28 17:05:39  pel2367
  *     changed the constructor to accept an operator.
  *
  *     Revision 1.6  2000/04/24 15:59:50  p361-45a
  *     finished method headers
  *
  *     Revision 1.5  2000/04/17 23:03:46  aec1324
  *     got rid of commented
  *     out code
  *
  *     Revision 1.4  2000/03/30 00:26:31  aec1324
  *     resized a little smaller so the panel below had e
  *     gh room for its table
  *
  *     Revision 1.3  2000/03/29 21:45:49  aec1324
  *     resized to fit nicer with the frames around it
  *
  *     Revision 1.2  2000/03/29 02:05:20  aec1324
  *     added color to panel so it is easy to see on the GUI
  *
  */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class BottomRightUpperPanel extends JPanel {
    private Dimension dimen = new Dimension( 490, 145 );

    private BottomRightUpperUpperPanel upperPanel;
    private BottomRightLowerLowerPanel lowerPanel;
    private Operator _operator;

    /**
     * Constructor to create an object of type BottomRightUpperPanel
     *
     */
    public BottomRightUpperPanel( Operator oPerator ) {
        _operator = oPerator;
	//this.setBackground( Color.green );
	this.setPreferredSize( dimen );

	lowerPanel = new BottomRightLowerLowerPanel();
	upperPanel = new BottomRightUpperUpperPanel( _operator );
	
	super.add( upperPanel, BorderLayout.WEST );
	super.add( lowerPanel, BorderLayout.CENTER );
     

    }
}
