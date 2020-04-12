/**
 * This is the Bottom left panel that holds the Room table 
 *
 *
 * @version	$Id: BottomLeftPanel.java,v 1.20 2000/05/08 03:30:54 p361-45a Exp $
 *
 * @author	Adam E Chipalowsky 
 *
 * Revisions:
 *		$Log: BottomLeftPanel.java,v $
 *		Revision 1.20  2000/05/08 03:30:54  p361-45a
 *		fixed formatting
 *
 *		Revision 1.19  2000/05/06 15:12:33  aec1324
 *		fixed an off my 1 error with selection in the table
 *
 *		Revision 1.18  2000/05/05 20:27:20  aec1324
 *		finished and tested the right click for
 *		unassigning a section from the room
 *
 *		Revision 1.17  2000/05/05 18:38:52  aec1324
 *		started adding a right click menu
 *
 *		Revision 1.16  2000/05/03 05:19:44  pel2367
 *		added a mouse listener so clicking on a section
 *		number in this table would highlight it in th
 *		section window.
 *
 *		Revision 1.15  2000/04/28 17:19:12  etf2954
 *		Fixed the Font
 *
 *		Revision 1.14  2000/04/25 22:53:34  aec1324
 *		tried to add colors to outline sections that are
 *		assigned to rooms
 *
 *		Revision 1.13  2000/04/25 04:51:08  pel2367
 *		fixed the refresh routine, the iteration
 *		is a little tricky.
 *
 *		Revision 1.12  2000/04/23 21:44:29  pel2367
 *		the columns are no longer moveable.
 *
 *		Revision 1.11  2000/04/22 00:51:39  aec1324
 *		fixed the interface to the updating room
 *		attributes
 *
 *		Revision 1.10  2000/04/20 16:46:59  p361-45a
 *		finished method headers
 *
 *		Revision 1.9  2000/04/07 13:10:24  etf2954
 *		Added the Operator class
 *
 *		Revision 1.8  2000/04/06 05:47:29  etf2954
 *		Gave the class access to GUIinterface
 *
 *		Revision 1.7  2000/04/05 17:40:42  aec1324
 *		fixed the times so they now go from 8am to 10pm
 *
 *		Revision 1.6  2000/04/05 00:19:23  etf2954
 *		Fixed errant table size
 *
 *		Revision 1.5  2000/03/30 23:21:49  aec1324
 *		added an abstract table class to create the table
 *		that goes in this panel
 *
 *		Revision 1.4  2000/03/30 19:39:54  aec1324
 *		worked on table, got rid of data inside it.
 *		Having this class create the table
 *
 *		Revision 1.3  2000/03/30 01:50:46  aec1324
 *		added a real table to this panel
 *
 *		
 *
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import javax.swing.event.TableModelListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

public class BottomLeftPanel extends JPanel
    implements ActionListener, ItemListener{

    private Dimension dimen = new Dimension( 210, 320 );
    private JLabel label = new JLabel( "   Room Occupancy Table   " );
    private Font font;
    private BorderLayout borderLayout;
    private TableColumn timeTableColumn;
    private Border loweredBevel;
    private MyTableModel myModel;
    private static Operator operator;
    private JTable table;
    private GUI _myGUI;

    private JPopupMenu rightClickPopup;
    private JMenuItem menuItem;
    
    /**
     * constructor to create an object of type BottomLeftPanel
     *
     * @param myGUI a value of type 'GUI'
     * @param oPerator a value of type 'Operator'
     */
    public BottomLeftPanel( GUI myGUI, Operator oPerator ) {

	_myGUI = myGUI;
	operator = oPerator;

	borderLayout = new BorderLayout( );
	this.setPreferredSize( dimen );
	
	//set up and display the title
	font = new Font( null, Font.PLAIN, 25 );
	label.setFont( font );
	this.add( label, BorderLayout.NORTH );

	//create the table
	myModel = new MyTableModel();
        table = new JTable(myModel);
        table.setPreferredScrollableViewportSize(new Dimension(480, 310));
	
	//Create the scroll pane and add the table to it. 
        JScrollPane scrollPane = new JScrollPane(table);
	table.setCellSelectionEnabled( true );
	table.setSelectionBackground( Color.white);
	table.setRowHeight( 21 );
    
	table.getTableHeader().setReorderingAllowed( false );
	
	//resize the time column to better fit everything
	timeTableColumn = table.getColumnModel().getColumn(0);
	timeTableColumn.setPreferredWidth(85);

	//set up a border for the table
	loweredBevel = BorderFactory.createLoweredBevelBorder();

	//add the border to the table
	table.setBorder(loweredBevel);

        //Add the scroll pane to this window.
        this.add(scrollPane);

	//Create the right click popup menu.
	rightClickPopup = new JPopupMenu();

	//add a menu option to the right click menu
	menuItem = new JMenuItem( "Unassign section from room" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );

	//Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
	table.addMouseListener(popupListener);
    }
    
    /**
     * firures out what the event is and alerts the operator about it
     *
     * @param e a value of type 'ActionEvent'
     */
    public void actionPerformed(ActionEvent e) {
	
	//figure out where the event came from
	JMenuItem source = (JMenuItem)(e.getSource());

	//get a description of the event
	String event = source.getText();
       
	//match the description with the what they could be
	if ( event == "Unassign section from room" ){
	    int row = table.getSelectedRow();
	    int col = table.getSelectedColumn();

	    //alert the operator if there is actually a section there
	    if( row >= 0 && col != -1 ){

		//grab the string and see if there is a section there
		String stringSelected = (String) table.getValueAt( row, col );

		if( !stringSelected.equals( "     " ) ){
		    //if there is then tell the operator to unassign it
		    operator.unassign();
		}//inner if
	    }//middle if
	}//outer if
    }//actionPerformed
   
    /**
     * method to change the state of an item.  DOes nothing but needs
     * to be in here cause we implement from ActionListener
     *
     * @param e a value of type 'ItemEvent'
     */
    public void itemStateChanged(ItemEvent e) {
	
    }//itemStateChanged
    
    /**
     * this method lets someone from outside edit the table
     *
     * @param newData a value of type 'Object[][]'
     */
    public void setTableValue( Object[][] newData ){

	myModel.resetDataVector( newData );

    }
    
    class MyTableModel extends AbstractTableModel {
	final String[] columnNames = {" Time", "Monday", "Tuesday", 
				      "Wednesday","Thursday", "Friday"};
	
	private Object[][] data = {
	    {"  8 AM- 9 AM ", "     ", "     ", "     ", "     ", "     "},
	    {"  9 AM-10 AM ", "     ", "     ", "     ", "     ", "     "},
	    {" 10 AM-11 AM ", "     ", "     ", "     ", "     ", "     "},
	    {" 11 AM-12 PM ", "     ", "     ", "     ", "     ", "     "},
	    {" 12 PM- 1 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  1 PM- 2 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  2 PM- 3 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  3 PM- 4 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  4 PM- 5 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  5 PM- 6 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  6 PM- 7 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  7 PM- 8 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  8 PM- 9 PM ", "     ", "     ", "     ", "     ", "     "},
	    {"  9 PM-10 PM ", "     ", "     ", "     ", "     ", "     "}
	};
        /**
	 * returns Column Count
	 *
	 * @return a value of type 'int'
	 */
	public int getColumnCount() {
            return columnNames.length;
        }
        
        /**
	 * returns Row Count
	 *
	 * @return a value of type 'int'
	 */
	public int getRowCount() {
            return 14;
        }

        /**
	 * returns Column Name
	 *
	 * @param col a value of type 'int'
	 * @return a value of type 'String'
	 */
	public String getColumnName(int col) {
            return columnNames[col];
        }

        /**
	 * gets the value at the specified spot
	 *
	 * @param row a value of type 'int'
	 * @param col a value of type 'int'
	 * @return a value of type 'Object'
	 */
	public Object getValueAt(int row, int col) {
            return data[row][col];
        }

	/**
	 * returns whether row selection is allowed
	 *
	 * @return a value of type 'boolean'
	 */
	public boolean getCellSelectionAllowed() {
        return true;
	}

        /**
	 * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
	 *
	 * @param c a value of type 'int'
	 * @return a value of type 'Class'
	 */
	public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
	
         /*
         * Don't need to implement this method unless your table's
         * editable. For right now, nothing is editable.
         */
        public boolean isCellEditable(int row, int col) {
          
	    return false;
	}
	
	/**
	 * sets new data to the vector
	 *
	 * @param newData a value of type 'Object[][]'
	 */
	public void resetDataVector( Object[][] newData ){

	    //need to update the table but leave the time right where it is
		for( int day = 0 ; day < 5 ; day++ ) {
	
		    for( int time = 0; time < 14; time++ ) {
			data[time][day +1] = newData[day][time]; 
		    }
		}
	    //send to big brother that the table is ready for an update
	    fireTableDataChanged();
	
	}
   
    }//MyTableModel
    
    class PopupListener extends MouseAdapter {
        /**
	 * method to perform reactions to mouse click 
	 * e.g highlight a section
	 *
	 * @param e a value of type 'MouseEvent'
	 */
	public void mousePressed(MouseEvent e) {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        
	if( row >= 0 && col != -1 ){
             String stringSelected = (String) table.getValueAt( row, col );
	     
             if( !stringSelected.equals( "     " ) ){
      	         operator.roomOccClicked( stringSelected );
             }
        }
	//check and bring up the right click menu if the
	//user clicked the right click
	maybeShowPopup(e);
	
	}
        /**
	 * method to perform reactions to mouse being released
	 *
	 * @param e a value of type 'MouseEvent'
	 */
	public void mouseReleased(MouseEvent e) {
	    
            //call mayeb show to figure out if it was a right click
	    //if it isn't, we don't care about showing a menu
	    maybeShowPopup(e);   
        }
	
	private boolean maybeShowPopup(MouseEvent e) {
	    boolean temp = false;
	    //if the right button was clicked, show the menu
	    if (e.isPopupTrigger()) {
                rightClickPopup.show(e.getComponent(), e.getX(), e.getY());
		temp = true;
            }
	    return temp;
	}//maybeShowPopup
    
    }//PopupListener

}//BottomLeftPanel
