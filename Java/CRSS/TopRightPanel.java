/**
 * This panel holds the section info table.  Selection is allowed. 
 *
 *
 * @version	$Id: TopRightPanel.java,v 1.33 2000/05/09 02:31:26 aec1324 Exp $
 *
 * @author	Adam E Chipalowsky 
 *
 * Revisions:
 *		$Log: TopRightPanel.java,v $
 *		Revision 1.33  2000/05/09 02:31:26  aec1324
 *		commented out the assign all on the right
 *		click menu
 *
 *		Revision 1.32  2000/05/08 18:02:45  p361-45a
 *		fixed formatting
 *
 *		Revision 1.31  2000/05/05 17:52:56  pel2367
 *		added a copy selected option.
 *
 *		Revision 1.30  2000/05/04 16:51:48  aec1324
 *		added many comments to make code followable
 *
 *		Revision 1.29  2000/05/03 17:35:54  aec1324
 *		made a minor change to the orer of when the
 *		number is sorted in the header of the
 *		table.
 *
 *		Revision 1.28  2000/05/03 16:10:03  aec1324
 *		added sort calls to the operator when the table
 *		headers are clicked
 *
 *		Revision 1.27  2000/05/03 05:19:44  pel2367
 *		wrote hiLight()
 *
 *		Revision 1.26  2000/05/03 01:50:02  pel2367
 *		added the sortStyle data field, accessors,
 *		mutators
 *
 *		Revision 1.25  2000/04/30 20:25:18  pel2367
 *		changed right-click to be "assign all"
 *
 *		Revision 1.24  2000/04/28 20:39:59  pel2367
 *		added unhighlight or whatever the heck
 *		i called it.
 *
 *		Revision 1.23  2000/04/28 01:24:51  etf2954
 *		Got rid of yet another null pointer exception
 *		this one occuring when you try to delete the last
 *		section, and the java table tries to make the next one high
 *		lighted
 *
 *		Revision 1.22  2000/04/26 13:22:23  etf2954
 *		FIxed the size of the colummns
 *
 *		Revision 1.21  2000/04/25 21:15:29  aec1324
 *		added a new column to the section table that
 *		shows the times the section meets
 *		/.
 *
 *		Revision 1.20  2000/04/25 01:42:40  p361-45a
 *		finished method headers
 *
 *		Revision 1.19  2000/04/24 23:33:17  aec1324
 *		added the days column to the table.  Now the days
 *		are listed in a visable place
 *
 *		Revision 1.18  2000/04/24 04:41:36  etf2954
 *		FIxed the attributes things
 *
 *		Revision 1.17  2000/04/24 01:07:26  aec1324
 *		fixed the problem with null pointers
 *		when selecting a blank field
 *
 *		Revision 1.16  2000/04/24 00:28:20  pel2367
 *		reworked getHighlighted().
 *
 *		Revision 1.15  2000/04/23 21:40:39  pel2367
 *		the columns are no longer moveable
 *
 *		Revision 1.14  2000/04/22 16:23:00  aec1324
 *		added a call to the operator that calls the
 *		hilite cation
 *
 *		Revision 1.13  2000/04/19 14:28:38  etf2954
 *		Added functionality for Add Section
 *
 *		Revision 1.12  2000/04/18 03:25:34  aec1324
 *		updated the interface to the operator
 *
 *		Revision 1.11  2000/04/15 21:15:59  aec1324
 *		added another column
 *
 *		Revision 1.10  2000/04/13 02:22:57  aec1324
 *		added the ability to popup an new add section
 *		window
 *
 *		Revision 1.9  2000/04/09 20:31:38  aec1324
 *		changed the right click edit command to send
 *		notice to the operator
 *
 *		Revision 1.8  2000/04/08 22:47:28  aec1324
 *		added the right click menu and edit section call
 *
 *		Revision 1.7  2000/04/07 16:48:08  aec1324
 *		added more options on right click
 *
 *		Revision 1.6  2000/04/07 13:10:24  etf2954
 *		Added the Operator class
 *
 *		Revision 1.5  2000/04/06 05:49:10  etf2954
 *		Gave the class access to the GUIinterface
 *
 *		Revision 1.4  2000/04/02 18:56:23  aec1324
 *		corrected probles with resizing.  Now the table is
 *		updated by having the class handed a new data
 *		Object[][]
 *
 *		Revision 1.3  2000/04/01 22:05:13  aec1324
 *		added the ability to update a given row via an
 *		update method.  Also changed it so when a user
 *		clicks on a row, a GUIevent object is created
 *
 *		
 *
 */


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import javax.swing.JScrollPane;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

/**
 * class to create section list window
 */
public class TopRightPanel extends JPanel
    implements ActionListener, ItemListener {
    
    private String[] columnNames = {" Section Number", "Capacity", 
				    "Section Name"};

    private Dimension dimen = new Dimension( 650, 300 );
    private JLabel label = new JLabel( "Section List" );
    private JScrollPane scrollPane = new JScrollPane();
    private Font font;
    private BorderLayout borderLayout;

    private MyTableModel myModel;
    private TableColumn tableColumn;
    private Border compoundBorder, raisedBevel, loweredBevel;
    private boolean ALLOW_ROW_SELECTION = true;
    private JTable table;
    private static Operator operator;
    private int rowSelected;

    private JPopupMenu rightClickPopup;
    private JMenuItem menuItem;
    private SortSectionMenu sortMenu;
    private GUI _myGUI;
    
    private int sortStyle = 1;
    private int AoDo = 0;

    /**
     * constructor to create an object of type TopRightPanel
     *
     * @param myGUI a value of type 'GUI'
     * @param oPerator a value of type 'Operator'
     */
    public TopRightPanel( GUI myGUI, Operator oPerator ) {
	
	_myGUI = myGUI;
	operator = oPerator;
	rowSelected = 0;
	
	//this.setBackground( Color.yellow );
	
	//borderLayout = new BorderLayout( 2, 1 );
	//this.setLayout( borderLayout );
	this.setPreferredSize( dimen );
	
	font = new Font( "Section List", Font.PLAIN, 25 );
	label.setFont( font );
	this.add( label, BorderLayout.NORTH );

	//set up and add the table
	myModel = new MyTableModel();
	table = new JTable(myModel);
        table.setPreferredScrollableViewportSize(new Dimension(480, 272));

	table.getTableHeader().setReorderingAllowed( false );

	//resize some of the rows in the table
	tableColumn = table.getColumnModel().getColumn(1);
	tableColumn.setPreferredWidth(45);

	tableColumn = table.getColumnModel().getColumn(2);
	tableColumn.setPreferredWidth(50);


	tableColumn = table.getColumnModel().getColumn(3);
	tableColumn.setPreferredWidth(162);

	tableColumn = table.getColumnModel().getColumn(4);
	tableColumn.setPreferredWidth(45);

	//allow the user to only select one row at a time
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	//add the mouse listener to the table headers so we can
	//sort by simple clicking the header
	addMouseListenerToHeaderInTable( table );

	//alert if a row is selected
	if (ALLOW_ROW_SELECTION) { // true by default
	    ListSelectionModel rowSM = table.getSelectionModel();
	    rowSM.addListSelectionListener(new ListSelectionListener() {
		
		/**
		 * changes value if neccessary
		 *
		 * @param e a value of type 'ListSelectionEvent'
		 */
		public void valueChanged(ListSelectionEvent e) {
		    ListSelectionModel lsm = 
			(ListSelectionModel)e.getSource();
		    
		    //get the selected row if there is a non-blank one
		    //selected
		    if (! (lsm.isSelectionEmpty()) ) {
			rowSelected = lsm.getMinSelectionIndex();
		    }
		
		}//valueChanged
	    });
	    
	} 
	
	else {
	    //set so the user does not select an entry
	    table.setRowSelectionAllowed(false);
	}//else

	//add a border around the table
	loweredBevel = BorderFactory.createLoweredBevelBorder();
	table.setBorder(loweredBevel);
	
	//Create the scroll pane and add the table to it. 
        JScrollPane scrollPane = new JScrollPane(table);
	this.add( scrollPane );
	
	//Create the right click popup menu.
	rightClickPopup = new JPopupMenu();
	
	menuItem = new JMenuItem( "Assign section to room" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
	
	menuItem = new JMenuItem( "Add new section" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
    
	menuItem = new JMenuItem( "Copy selected" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );

        menuItem = new JMenuItem( "Edit section" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
        
	menuItem = new JMenuItem( "Remove section" );
        menuItem.addActionListener( this );
        rightClickPopup.add( menuItem );
	
	rightClickPopup.addSeparator();

	sortMenu = new SortSectionMenu( _myGUI, operator );
	rightClickPopup.add( sortMenu );

	//rightClickPopup.addSeparator();
	
	//menuItem = new JMenuItem( "Assign all sections" );
        //menuItem.addActionListener( this );
        //rightClickPopup.add( menuItem );
	
	//Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
	table.addMouseListener(popupListener);
	
    }//TopRightPanel
    
    /**
     * method to perform actions based on click of the mouse
     *
     * @param e a mouse click action evernt
     */
    public void actionPerformed(ActionEvent e) {
	
	//figure out where the event came from
	JMenuItem source = (JMenuItem)(e.getSource());
	
	//get a description of the event
	String event = source.getText();
       
	//match the description with the what they could be
	if ( event == "Edit section" ){
	    //alert the operator
	    operator.modifySection();
	}
	
	else if ( event == "Remove section" ){
	    //alert the operator
	    operator.removeSection();
	}
	
	else if ( event == "Assign section to room" ){
	    //alert the operator
	    operator.assign();
	}
	
	else if ( event == "Add new section" ){
	    //alert the operator with a code of 0 to indicate that
	    //this is a add a single section
	    operator.addSection( 0 );
	}
	
	else if( event == "Copy selected"  ){
	    operator.addSection( 2 );
	}
	
	else if ( event == "Assign all sections" ){
	    //alert the operator
	    operator.assignAll();
	}	

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
     * returns the selected row
     *
     * @return a value of type 'int'
     */
    public int getSelectedRow(){
	
	return rowSelected;
    }//getSelectedRow
 
    /**
     * class to handle mouse click
     */
    class PopupListener extends MouseAdapter {
        
	/**
	 * method to perform reactions to mouse click 
	 * e.g highlight a section. inner method from class PopupListener
	 *
	 * @param e a mouse event that says a mouse button was clicked
	 */
	public void mousePressed(MouseEvent e) {

	    //if it was a row, tell the operator to hilight it
            if( !maybeShowPopup(e) ){
		operator.hiLightSection();
	    }

        }//mousePressed

        /**
	 * method to perform reactions to mouse being released
	 *
	 * @param e a value of type 'MouseEvent'
	 */
	public void mouseReleased(MouseEvent e) {
            
	    //call mayeb show to figure out if it was a right click
	    //if it isn't, we don't care about showing a menu
	    maybeShowPopup(e);
        }//mouseReleased

        /**
	 * returns whether a pop up window should be displayed
	 *
	 * @param e a value of type 'MouseEvent'
	 * @return boolean to say weather or not to show the menu
	 */
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

  
    /** 
     * method to let someone from outside edit the table
     *
     * @param newData a value of type 'Object'
     */
    public void setTableValue( Object newData ){
	
	//goto the model and tell it to update its data vector
	myModel.resetDataVector( newData );

    }//setTableValue

    /**
     * when called, this method will return the section number that 
     * the user has highlighted in string form.  The "-" will be omited 
     * becuase of their lack of value in the true section number
     *
     * @return a string representation of the number
     * @return null if nothing is selected
     */
    public String getHighlighted(){
	
        //get from the table what is currentlny selected
        int selectedRow = table.getSelectedRow();
	
	//set the sectionNumber to null so it is defaulted if needs be
        String sectionNumber = null;

        //make sure the user has selected somthing, if not, return null
        if( (selectedRow != -1) ){
	
	    if( selectedRow < table.getRowCount() ) {
            	sectionNumber = (String)table.getValueAt(selectedRow, 0);
	    } // if
        }//outer if
	
        return sectionNumber;
    }//getHighlightedRow
    
    /**
     * hilights a row given a section number
     *
     * @param sectNum A string of the section number
     */
    public void hiLight( String sectNum ){
	
	//firlst, clean all hilights
        table.clearSelection();
	
	//go through the table looking at section numbers and figure out
	//where the section is that needs to be hilighted. Hilight that
	//one
        for( int i = 0; i < table.getRowCount() ; i++ ){
        
	    if( sectNum.equals((String) table.getValueAt( i, 0 ) ) ){
                table.setRowSelectionInterval( i , i );
            }
        }//for
    } // hiLight
    
    /**
     * clears all hilights
     *
     */
    public void unHighlight(){
        table.clearSelection();
    } // unHighlight
    
    /**
     * keeps track of the current sort style
     *
     * @param newStyle a value of type 'int'
     */
    public void setSort( int newStyle ){
        sortStyle = newStyle;
    }//setSort
    
    /**
     * returns the sort style
     *
     * @return returns an int that is the sort style
     */
    public int getSort(){
        return sortStyle;
    }//getSort
    
    /**
     * This is a table model that is represented in the window
     * It is set up in such a way the data can easily be updated by
     * calling a routine in it.  It sets up the table but does not
     * disply it in the window, whoever uses this model should do that
     */
    class MyTableModel extends AbstractTableModel {

	//set the head names for the table
	private String[] columnNames = {" Section #", "Enrol #", "Room", 
					"Section Name","days","Time"};
	
	//set the default table to fill the table panel
	private Object[][] data = {
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "},
	    {"  ", "  ", "  ", "  ", "  ", "  "}

	};
	
	/**
	 * returns the column count
	 *
	 * @return int representing the length
	 */
	public int getColumnCount() {
	 
	    return columnNames.length;
	}//getColumnCount
	
	/**
	 * returns the row count
	 *
	 * @return int representing the row count
	 */
	public int getRowCount() {
	    
	    return data.length;
	}//getRowCount
	
	/**
	 * returns the column count
	 *
	 * @param col int representing the column to examine
	 * @return String to represent the column name
	 */
	public String getColumnName(int col) {
	    
	    return columnNames[col];
	}//getColumnNames
	
	/**
	 * returns the value at the specified spot
	 *
	 * @param row the row as an int
	 * @param col the column as an int
	 * @return an Object that represents what in the given position
	 */
	public Object getValueAt(int row, int col) {
	    
	    return data[row][col];
	}//getValueAt
	
	/**
	 * returns whether a row can be selected
	 *
	 * @return true saying that a user can always select a row
	 */
	public boolean getRowSelectionAllowed() {
	    
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

        /*
         * Don't need to implement this method unless your table's
         * editable. For right now, nothing is editable.
         */
        
	/**
	 * tells weather or not the cell is editable
	 *
	 * @param row the row
	 * @param col the column
	 * @return is its editable
	 */
	public boolean isCellEditable(int row, int col) {
       
	    //cells are never editable
	    return false;
	}//isCellEditable

	/**
	 * sets the value at the specified spot
	 *
	 * @param value a value of type 'Object'
	 * @param row the row
	 * @param colthe column
	 */
	public void setValueAt(Object value, int row, int col) {
	    
	    //do a check to make sure we are not out of bounds on the array
	    if ( col < 3 ){
		
		//reset the correct position in the data vector to the
		//new data
		data[row][col] = value;
		
		//update the cell to reflect the changes
		fireTableCellUpdated(row, col);
	    }//if
	}//setValueAt
	
	/**
	 * replaces the data in the table and updates it
	 *
	 * @param newData a value of type 'Object'
	 */
	public void resetDataVector( Object newData ){
	    
	    data = (Object[][])newData;
	    fireTableDataChanged();
	}

    }//resetDataVector

    /**
     * Add a mouse listener to the Table to trigger a table sort 
     * when a column heading is clicked in the JTable. 
     *
     * @param table the table in which to add the sorts to the headers
     */
    public void addMouseListenerToHeaderInTable(JTable table) { 
	
	//set the table to a new table so we can chenge its state
	//more easily
        final JTable tableView = table; 
        
	//turn off column selecting
	tableView.setColumnSelectionAllowed(false); 

	//set it up so we can "hear" where the mouse has clicked
        MouseAdapter listMouseListener = new MouseAdapter() {

            /**
	     * registers a mouse event	 
	     *
	     * @param e an event that represents a mouse click
	     */
	    public void mouseClicked(MouseEvent e) {
	
		//set locals to information about the table so we can more
		//easily manipulate it
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX()); 
                int column = tableView.convertColumnIndexToModel(viewColumn); 
                
		//make sure the user didn't by some means click off into 
		//another column
		if (e.getClickCount() == 1 && column != -1) {
                    
		    //if we are here we want to figure out which column we 
		    //want to sort by and then pass that information 
		    //off to the operator and do a sort
		    if( AoDo == 0 ){
			AoDo = 1;

			if( column == 0 ){	
			    //sort assending section number
			    _myGUI.setSectionSortStyle( -1 );
			}
		
			else if( column == 1 ){
			    //sort assending enrollment 
			    _myGUI.setSectionSortStyle( 2 );
			}
			
			else if( column == 2 ){
			    //sort assending assignment
			    _myGUI.setSectionSortStyle( 3 );
			}
			
			else if( column == 3 ){
			    //sort assending section name
			    _myGUI.setSectionSortStyle( 4 );
			}
		    }//outer if
		    
		    else{
			AoDo = 0;
			
			if( column == 0 ){
			    //sort decending number
			   _myGUI.setSectionSortStyle( 1 );
			}
			
			else if( column == 1 ){
			    //sort decending enrollment
			    _myGUI.setSectionSortStyle( -2 );
			}
			
			else if( column == 2 ){
			    //sort decending assignment
			    _myGUI.setSectionSortStyle( -3 );
			}
			
			else if( column == 3 ){
			    //sort decending section name
			    _myGUI.setSectionSortStyle( -4 );
			}
			
		    }//outer else
		    
		    //make the call to sort
		    operator.sortSections();
		    
		}//if
            }//mouseClicked
        };
	
        JTableHeader th = tableView.getTableHeader(); 
        
	th.addMouseListener(listMouseListener); 
    }//addMouseListenerToHeaderInTable

}//TopRightPanel
