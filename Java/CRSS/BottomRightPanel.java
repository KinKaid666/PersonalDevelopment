/**
  * This is the Bottom right panel of the main panel
  *
  * Author: Adam Chipalowsky
  *
  * @version    $Id: BottomRightPanel.java,v 1.10 2000/04/28 17:05:39 pel2367 Exp $
  *
  * @author     
  *
  * Revisions:
  *     $Log: BottomRightPanel.java,v $
  *     Revision 1.10  2000/04/28 17:05:39  pel2367
  *     changed the constructor to accept an operator.
  *
  *     Revision 1.9  2000/04/24 16:06:27  p361-45a
  *     finished method headers
  *
  *     Revision 1.8  2000/04/07 13:10:24  etf2954
  *     Added the Operator class
  *
  *     Revision 1.7  2000/04/06 05:47:29  etf2954
  *     Gave the class access to GUIinterface
  *
  *     Revision 1.6  2000/04/05 17:42:38  aec1324
  *     added an interface to the classes this class creates
  *
  *     Revision 1.5  2000/03/30 00:26:31  aec1324
  *     switched color back to default gray
  *
  *     Revision 1.4  2000/03/29 02:05:20  aec1324
  *     broke this panel up into two more sub panels
  *
  */



import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class BottomRightPanel extends JPanel {
    private Dimension dimen = new Dimension( 400, 300 );
    
    private BottomRightUpperPanel upperPanel;
    private BottomRightLowerPanel lowerPanel;
	private static Operator operator;

    /**
     * constructor to create an object of type BottomRightPanel
     *
     * @param oPerator a value of type 'Operator'
     */
    public BottomRightPanel( Operator oPerator ) {
	operator = oPerator;
	this.setPreferredSize( dimen );
	//this.setBackground( Color.blue );
	upperPanel = new BottomRightUpperPanel( operator );
	lowerPanel = new BottomRightLowerPanel();

	super.add( upperPanel );
	super.add( lowerPanel );
    }

    /**
     * method to allow access to the room attributes table withing
     * the sub pannels
     *
     * @param newData a value of type 'Object[]'
     */
    public void updateRoomAttributes(Object[] newData){
	lowerPanel.updateRoomAttributes(newData);
    }

    /**
     * method to allow access to the section attributes table withing 
     * the sub pannels
     *
     * @param newData a value of type 'Object[]'
     */
    public void updateSectionAttributes(Object[] newData){
	lowerPanel.updateSectionAttributes(newData);
    }              
    
}
