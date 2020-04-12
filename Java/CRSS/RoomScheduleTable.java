
/**
 * 
 * This is the table used to display information about rooms
 *
 * @version	$Id: RoomScheduleTable.java,v 1.5 2000/04/24 17:40:04 p361-45a Exp $
 *
 * @author	Adam E Chipalowsky 
 *
 * Revisions:
 *		$Log: RoomScheduleTable.java,v $
 *		Revision 1.5  2000/04/24 17:40:04  p361-45a
 *		finished method headers
 *
 *		Revision 1.4  2000/03/30 19:39:54  aec1324
 *		possibly thinking about getting rid of this class.  Seems that
 *		everything can be done in the LeftPanel class
 *
 *		Revision 1.3  2000/03/30 01:50:46  aec1324
 *		set up this table to display time and days of the
 *		week
 *
 *		
 *
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * class to create a table for a room's schedule
 */
public class RoomScheduleTable extends AbstractTableModel {
    private Object[][] data = {
	{"            ", " Monday", " Tuesday", " Wednesday", 
	 " Thursday", " Friday"},
	{"  8 AM- 9 AM ", "   ", "   ", "   ", "   ", "   "},
	{"  9 AM-10 AM ", "   ", "   ", "   ", "   ", "   "},
	{" 10 AM-11 AM ", "   ", "   ", "   ", "   ", "   "},
	{" 11 AM-12 PM ", "   ", "   ", "   ", "   ", "   "},
	{" 12 PM- 1 PM ", "   ", "   ", "   ", "   ", "   "},
	{"  1 PM- 2 PM ", "   ", "   ", "   ", "   ", "   "},
	{"  2 PM- 3 PM ", "   ", "   ", "   ", "   ", "   "},
	{"  4 PM- 5 PM ", "   ", "   ", "   ", "   ", "   "},
	{"  5 PM- 6 PM ", "   ", "   ", "   ", "   ", "   "},
	{"  7 PM- 8 PM ", "   ", "   ", "   ", "   ", "   "},
	{"  8 PM- 9 PM ", "   ", "   ", "   ", "   ", "   "},
	{" 10 PM-10 PM ", "   ", "   ", "   ", "   ", "   "}
    };

    private String[] columnNames = {" Time", "Monday", "Tuesday", "Wednesday",
                                  "Thursday", "Friday"};

    //maybe remove the "table" and the constructor later, don't really need it
    private JTable table;
    /**
     * creates a new JTable object
     *
     */
    public void RoomScheduleTable(){
	table = new JTable(data, columnNames);
	
    }

    public int getColumnCount() {
	return columnNames.length;
    }
    
    public int getRowCount() {
	return data.length;
    }
    
    public String getColumnName(int col) {
	return columnNames[col];
    }
    
    public Object getValueAt(int row, int col) {
	return data[row][col];
    }

    public void setValueAt(Object value, int row, int col) {
	
	if (data[0][col] instanceof Integer                        
	    && !(value instanceof Integer)) {                  
	    //With JFC/Swing 1.1 and JDK 1.2, we need to create    
	    //an Integer from the value; otherwise, the column     
	    //switches to contain Strings.  Starting with v 1.3,   
	    //the table automatically converts value to an Integer,
	    //so you only need the code in the 'else' part of this 
	    //'if' block.                                          
	    //XXX: See TableEditDemo.java for a better solution!!!
	    try {
		data[row][col] = new Integer(value.toString());
		fireTableCellUpdated(row, col);
	    } catch (NumberFormatException e) { }
	    
	    
	} else {
	    data[row][col] = value;
	    fireTableCellUpdated(row, col);
	}
	
	
    }




}
