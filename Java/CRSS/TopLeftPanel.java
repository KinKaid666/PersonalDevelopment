/**
 * This panel holds the room list table.  Selection is allowed. 
 *
 *
 * @version	$Id: TopLeftPanel.java,v 1.41 2000/05/08 17:56:34 p361-45a Exp $
 *
 * @author	Adam E Chipalowsky; Eric T. Ferguson
 * @author  Phil Light
 *
 * Revisions:
 *		$Log: TopLeftPanel.java,v $
 *		Revision 1.41  2000/05/08 17:56:34  p361-45a
 *		fixed formatting
 *
 *		Revision 1.40  2000/05/04 17:13:59  aec1324
 *		added comments so the code is readable
 *
 *		Revision 1.39  2000/05/03 17:35:54  aec1324
 *		made a minor change to the orer of when the
 *		number is sorted in the header of the
 *		table.
 *
 *		Revision 1.38  2000/05/03 16:10:03  aec1324
 *		added sort calls to the operator when the table
 *		headers are clicked
 *
 *		Revision 1.37  2000/05/03 13:26:37  etf2954
 *		Took out my System.out's
 *
 *		Revision 1.36  2000/05/03 05:19:44  pel2367
 *		i think all i did here was remove some old code
 *		that wasn't being used anymore... something
 *		like a right-click option?
 *
 *		Revision 1.35  2000/05/03 02:41:08  pel2367
 *		added a data field to try and make a
 *		SortRoomMenu bump off of this right-click
 *
 *		Revision 1.34  2000/05/03 01:56:57  pel2367
 *		fixed a typo by eric
 *
 *		Revision 1.33  2000/05/03 01:50:02  pel2367
 *		added the sortStyle data field, accessors,
 *		mutators
 *
 *		Revision 1.32  2000/05/03 01:04:58  etf2954
 *		did nothing, just tried about 2323 different
 *		to get something to work
 *
 *		Revision 1.31  2000/05/02 21:44:27  etf2954
 *		Tried to get the scrollbar to scroll down
 *
 *		Revision 1.30  2000/05/02 17:35:36  etf2954
 *		added functionality so that the room that is highlighted
 *		before a filter, is also highlighted after the filter
 *
 *		Revision 1.29  2000/04/29 03:39:02  pel2367
 *		made a call to the operator on 'show all
 *		rooms'
 *
 *		Revision 1.28  2000/04/28 20:39:59  pel2367
 *		added unhighlight or whatever the hell
 *		i called it.
 *
 *		Revision 1.27  2000/04/27 19:10:39  etf2954
 *		Deleted debugging msg
 *
 *		Revision 1.26  2000/04/27 03:52:55  etf2954
 *		fixed getHighlighted call
 *
 *		Revision 1.25  2000/04/27 02:22:19  etf2954
 *		Added a check in the getHighlighted() function to make sure
 *		that the highlighted room exists
 *
 *		Revision 1.24  2000/04/26 15:03:10  etf2954
 *		did nothing
 *
 *		Revision 1.23  2000/04/25 04:51:08  pel2367
 *		tooled around with the right-click menu to make it
 *		match the one in TopRightPanel.
 *
 *		Revision 1.22  2000/04/25 01:29:26  p361-45a
 *		finished method headers
 *
 *		Revision 1.21  2000/04/24 23:04:29  pel2367
 *		added the assignment option to the right-click
 *		menu.
 *
 *		Revision 1.20  2000/04/24 04:09:40  etf2954
 *		I was busy with Adam's mother, don't remember
 *
 *		Revision 1.19  2000/04/24 00:28:20  pel2367
 *		rewrote getHighlighted() to remove
 *		multiple return statements.
 *
 *		Revision 1.18  2000/04/23 22:32:28  pel2367
 *		damn it adam!  table needs to be a class
 *		variable, not local to the constructor!
 *		you have no idea how many hours i've been
 *		trying to figure out why this class isn't
 *		like TopRightPanel... AAAAARGH!
 *
 *		Revision 1.17  2000/04/23 21:41:54  pel2367
 *		the columns are no longer moveable
 *
 *		Revision 1.16  2000/04/23 18:16:31  pel2367
 *		still trying to figure this class out.  that is,
 *		i'm trying to get and set the highlighted room number.
 *
 *		Revision 1.15  2000/04/22 22:05:36  pel2367
 *		wrote getHighlighted()
 *
 *		Revision 1.14  2000/04/22 21:38:00  pel2367
 *		added getHighlighted()
 *
 *		Revision 1.13  2000/04/18 21:42:05  etf2954
 *		Added New room to the popup menu
 *
 *		Revision 1.12  2000/04/18 03:25:34  aec1324
 *		updated the interface to the operator
 *
 *		Revision 1.11  2000/04/15 21:15:59  aec1324
 *		added another column in the table
 *
 *		Revision 1.10  2000/04/09 20:31:38  aec1324
 *		changed the right click edit command to send
 *		notice to the operator
 *
 *		Revision 1.9  2000/04/08 22:47:28  aec1324
 *		updated the way this class calls edit room
 *
 *		Revision 1.8  2000/04/07 16:48:08  aec1324
 *		added more options on right click
 *
 *		Revision 1.7  2000/04/07 02:24:56  aec1324
 *		added right click
 *
 *		Revision 1.6  2000/04/06 05:49:10  etf2954
 *		Gave the class access to the GUIinterface
 *
 *		Revision 1.5  2000/04/02 18:56:23  aec1324
 *		got the size of the table correct as well as adding
 *		the ability to update the table data
 *
 *		Revision 1.4  2000/04/01 22:05:13  aec1324
 *		selections are working and added the capability
 *		to be able to select rows
 *
 *		Revision 1.3  2000/03/31 19:40:12  aec1324
 *		added a table to this panel
 *
 *
 *		
 *
 */


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

public class TopLeftPanel extends JPanel 
    implements ActionListener, ItemListener {

    private Dimension dimen = new Dimension( 400, 300 );
    private JLabel label = new JLabel( "Room List" );
    private JScrollPane scrollPane = new JScrollPane();
    private Font font;
    private BorderLayout borderLayout;
    private MyTableModel myModel;
    private TableColumn tableColumn;
    private Border compoundBorder, raisedBevel, loweredBevel;
    private boolean ALLOW_ROW_SELECTION = true;
    private static Operator operator;
    private JPopupMenu rightClickPopup;
    private JMenuItem menuItem;
    private JTable table;
    private int sortStyle;
    private SortRoomMenu sortMenu;
    private GUI _myGUI;
    private int AoDo = 0;

    /**
     * constructor to create an object of type TopLeftPanel
     * creates a window to hold the room list
     *
     * @param myGUI a value of type 'GUI'
     * @param oPerator a value of type 'Operator'
     */
    public TopLeftPanel( GUI myGUI, Operator oPerator ) {

	//set the operator and GUI
	operator = oPerator;
	_myGUI = myGUI;
	
	//come up with the title and fonts for it
	font = new Font( "Room List", Font.PLAIN, 25 );
	label.setFont( font );
	scrollPane.setOpaque( true );
	scrollPane.setBackground( Color.gray );
	this.add( label, BorderLayout.NORTH );
	
	//set up and add the table
	myModel = new MyTableModel();
	table = new JTable(myModel);
	
	//set the size
        table.setPreferredScrollableViewportSize(new Dimension(400, 272));
	
	//don't let the user drag aroung the columns
	table.getTableHeader().setReorderingAllowed( false );

	//resize the various columns so they all fit
	tableColumn = table.getColumnModel().getColumn(0);
	tableColumn = table.getColumnModel().getColumn(1);
	tableColumn.setPreferredWidth(50);
	tableColumn = table.getColumnModel().getColumn(2);
	tableColumn.setPreferredWidth(200);
	
	//allow the user to only select one row at a time
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
	//set the headers to sort when clicked
	addMouseListenerToHeaderInTable(table);
	
	if (ALLOW_ROW_SELECTION) { // true by default
	    ListSelectionModel rowSM = table.getSelectionModel();
	    
	    // add a listener so to know when something is selected
	    rowSM.addListSelectionListener(new ListSelectionListener() {
		
		/**
		 * will implement for the selection listener
		 *
		 * @param e a value of type 'ListSelectionEvent'
		 */
		public void valueChanged(ListSelectionEvent e) {
		    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		
		    //only select if there is something to select
		    if ( !(lsm.isSelectionEmpty())) {
			int selectedRow = lsm.getMinSelectionIndex();
		    }//if
		    
		}//valueChanged
	    });
	} 
	
	else {
	    //set so no selection
	    table.setRowSelectionAllowed(false);
	}//else
	
	//add a border around the table
	loweredBevel = BorderFactory.createLoweredBevelBorder();
	table.setBorder(loweredBevel);

	//Create the scroll pane and add the table to it. 
        JScrollPane scrollPane = new JScrollPane(table);
	this.add(scrollPane);
    
	//Create the right click popup menu.
	rightClickPopup = new JPopupMenu();
    
	menuItem = new JMenuItem( "Assign Room to Section" );
    	menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
    
	menuItem = new JMenuItem( "Add Room" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );    

        menuItem = new JMenuItem( "Edit Room" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
        
	menuItem = new JMenuItem( "Remove Room" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );

	rightClickPopup.addSeparator();
	
	sortMenu = new SortRoomMenu( _myGUI, operator );
	rightClickPopup.add( sortMenu );


	rightClickPopup.addSeparator();
	
	menuItem = new JMenuItem( "Show all rooms" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
	
	//Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
	table.addMouseListener(popupListener);

    }

    /**
     * the method to perform a right click action
     *
     * @param e a mouse click event
     */
    public void actionPerformed(ActionEvent e) {
	
	//get the source and a description of the event
	JMenuItem source = (JMenuItem)(e.getSource());
	String event = source.getText();
	
	//go through and match up the description and one of the
	//possable selections
	if ( event == "Assign Room to Section" ){
	    //alert the operator
	    operator.assign();
    
	}
	
	else if ( event == "Edit Room" ){
	    //alert the operator
	    operator.modifyRoom();
	    
	}
	
	else if ( event == "Add Room" ){
	    //alert the operator
	    operator.addRoom( 0 );
	}
	
	else if ( event == "Remove Room" ){
	    //alert the operator
	    operator.removeRoom();
	}
	
	else if ( event == "Show all rooms" ) {
	    //alert the operator
	    operator.showAllRooms();
	}
    
    }//actionPerformed

    
    /**
     * method to change the state of an item
     *
     * @param e a value of type 'ItemEvent'
     */
    public void itemStateChanged(ItemEvent e) {
	
    }
   
    /**
     * class to handle mouse clicks
     *
     */
    class PopupListener extends MouseAdapter {
        
	/**
	 * method highlight and other reactions to mouse click
	 *
	 * @param e a value of type 'MouseEvent'
	 */
	public void mousePressed(MouseEvent e) {
	
	    //if a click is detected, we need to make sure that it was the
	    //right mouse button.  Call maybeShow to deturmine that
	    if( !maybeShowPopup( e ) ) {
		operator.hiLightRoom();
	    } // if
        } // mouse Pressed

        /**
	 * method to perform reactions to mouse being released 
	 *     e.g. popup window
	 *
	 * @param e a value of type 'MouseEvent'
	 */
	public void mouseReleased(MouseEvent e) {
        
	    maybeShowPopup(e);
        }//mouseReleased

        /**
	 * returns whether or not to show a popup window
	 *
	 * @param e a value of type 'MouseEvent'
	 * @return a value of type 'boolean'
	 */
	private boolean maybeShowPopup(MouseEvent e) {
            
	    boolean temp = false;
	    
	    if (e.isPopupTrigger()) {
                rightClickPopup.show(e.getComponent(), e.getX(), e.getY());
		temp = true;
            }//if
	    
	    return temp;
        }//maybeShowPopup

    }//PopupListener

    /**
     * lets someone from outside edit the table
     *
     * @param newData 2D array of data shown as a 1D array
     */
    public void setTableValue( Object newData ){

	myModel.resetDataVector( newData );

    }//setTableValue

    /**
     * ID's and gets what room is selected
     *
     * @return A string representing the room number
     */
    public String getHighlighted(){
	
	//get the row and set room number to null for a default if need be
    	int selectedRow = table.getSelectedRow();
        String roomNumber = null;
	
	//grab what is selected if soemthing is selected
        if( selectedRow != -1 ){
	
	    if( selectedRow < table.getRowCount() ) {
		
		//grab the room number
		roomNumber = (String)table.getValueAt(selectedRow, 0);
	    } // if
        } // if
	
	return roomNumber;
    }//getHighlighted
    
    /**
     * removes any highlighting
     *
     */
    public void unHighlight(){
 
       table.clearSelection();
    } // unHighlight
    
    /**
     * hilights the row where the given room is in
     *
     * @param roomNum a string representation of the room number
     */
    public void hiLight( String roomNum ){

	//get rid of anything hilighted
	table.clearSelection();

	//go throught and search the room for the given room number
	//then hilight it
	for( int i = 0 ; i < table.getRowCount() ; i++ ) {

	    if( roomNum.equals( (String)table.getValueAt( i, 0 ) ) ) {
		table.setRowSelectionInterval( i, i );	
	    }//if
	} // for
    } // setHighlight
    
    /**
     * sets the sort style of the table
     *
     * @param newStyle the new version of style
     */
    public void setSort( int newStyle ){

        sortStyle = newStyle;
    }//setSort
    
    /**
     * returns the current sort style
     *
     * @return int representing style
     */
    public int getSort(){

        return sortStyle;
    }//getSort
	

    /**
     * a Table model that outlines what the table in this panel will look like
     * and provides some easy to use updating methods to update the vector
     * data
     */
    class MyTableModel extends AbstractTableModel {
	final String[] columnNames = {" Room Number", "Capacity", 
				      "Room Description"};
	//set default dats
	private Object[][] data = {
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "},
	    {"  ", "  ", "  "} 
	};

        /**
	 * returns column count
	 *
	 * @return the count
	 */
	public int getColumnCount() {
         
	    return columnNames.length;
        }//getColumnCount
        
        /**
	 * returns row count
	 *
	 * @return the count
	 */
	public int getRowCount() {
        
	    return data.length;
        }//getRowCount

        /**
	 * returns column name
	 *
	 * @param col the column
	 * @return the name of the column
	 */
	public String getColumnName(int col) {
            
	    return columnNames[col];
        }//getColumnName

        /**
	 * returns the value at the specified spot
	 *
	 * @param row the row
	 * @param col the column
	 * @return what is in the position
	 */
	public Object getValueAt(int row, int col) {
            
	    return data[row][col];
        }//getValueAt

	/**
	 * returns whether or not the row can be selected
	 *
	 * @return a value of type 'boolean'
	 */
	public boolean getRowSelectionAllowed() {
	  
	    //set as true, the user can always select a row
	    return true;
	}//getRowSelectionAllowed


	/**
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.
	 *
	 * @param c a value of type 'int'
	 * @return a value of type 'Class'
	 */
	public Class getColumnClass(int c) {
            
	    return getValueAt(0, c).getClass();
        }//getColumnClass

        /**
	 * returns whether or not the cell is editable
	 *
	 * @param row the row
	 * @param col the column
	 * @return if its editable
	 */
	public boolean isCellEditable(int row, int col) {
	    
	    //the user can not edit a cell
	    return false;
	}//isCellEditable

	/**
	 * method to update the room list
	 *
	 * @param newData the new data
	 */
	public void resetDataVector( Object newData ){
	    
	    //set the new data and update the table
	    data = (Object[][])newData;
	    fireTableDataChanged();
	}//resetDataVector

    }//MyTableModel
    
    /**
     * Add a mouse listener to the Table to trigger a table sort 
     * when a column heading is clicked in the JTable. 
     *
     * @param table the table in which to add the sorts to the headers
     */
    public void addMouseListenerToHeaderInTable(JTable table) { 
        
        final JTable tableView = table; 
        tableView.setColumnSelectionAllowed(false); 
	
	//look for a mouse click, then sees where it is.  If its on a
	//table header, it will do the sort bassed on what header
	//we selected
        MouseAdapter listMouseListener = new MouseAdapter() {
	    
	    /**
	     * ID's a mouse click
	     *
	     * @param e a value of type 'MouseEvent'
	     */
	    public void mouseClicked(MouseEvent e) {
                
		TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX()); 
                int column = tableView.convertColumnIndexToModel(viewColumn); 
                
		if (e.getClickCount() == 1 && column != -1) {
                    
		    //if we are here we want to figure out which column we 
		    //want to sort by and then pass that information off to 
		    //the operator and do a sort
		    if( AoDo == 0 ){
			AoDo = 1;

			if( column == 0 ){	
			    //sort assending room number
			    _myGUI.setRoomSortStyle( -1 );
			}
			
			else if( column == 1 ){
			    //sort assending capacity 
			    _myGUI.setRoomSortStyle( 2 );
			}
			
			else if( column == 2 ){
			    //sort assending section name
			    _myGUI.setRoomSortStyle( 3 );
			}
		    }//outer if
		    
		    else{
			AoDo = 0;
			
			if( column == 0 ){
			    //sort decending number
			   _myGUI.setRoomSortStyle( 1 );
			}
			
			else if( column == 1 ){
			    //sort decending capacity
			    _myGUI.setRoomSortStyle( -2 );
			}
			
			else if( column == 2 ){
			    //sort decending room name
			    _myGUI.setRoomSortStyle( -3 );
			}
			
		    }//outer else
		    
		    //make the call to sort
		    operator.sortRooms();
		
		}//if
            }
        };

        JTableHeader th = tableView.getTableHeader(); 
        th.addMouseListener(listMouseListener); 
    }//addMouseListenerToHeaderInTable
    
}//TopLeftPanel
