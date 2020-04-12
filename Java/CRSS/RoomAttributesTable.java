/**
  * This is the Room Attributes Table.  It displays what the room may have
  * associated with it.  For example maybe a room has a overhead projectr.
  *
  * Author: Adam Chipalowsky
  *
  * @version    $Id: RoomAttributesTable.java,v 1.5 2000/05/08 17:39:24 p361-45a Exp $
  *
  * @author     
  *
  * Revisions:
  *     $Log: RoomAttributesTable.java,v $
  *     Revision 1.5  2000/05/08 17:39:24  p361-45a
  *     fixed formatting
  *
  *     Revision 1.4  2000/04/24 17:34:22  p361-45a
  *     finished method headers
  *
  *     Revision 1.3  2000/03/30 00:26:31  aec1324
  *     added border, increased column height for easier
  *     reading.
  *
  *     Revision 1.2  2000/03/29 02:05:20  aec1324
  *     set the column size so no test is cut off
  *
  */

import java.awt.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * class to access and set data members of the attributes window
 */
public class RoomAttributesTable extends AbstractTableModel {
  
    final String[] columnNames = {"Room Extras", 
				  "Avalable"};
    final Object[][] data = {
	{" Overhead Projector",  new Boolean(false)},
	{" Digitial Projector", new Boolean(false)},
	{" Ethernet Connections", new Boolean(false)},
	{" Wireless Ethernet", new Boolean(false)},
	{" Speaker System", new Boolean(false)},
	{" Network Printer", new Boolean(false)},
	{" DVD player", new Boolean(false)}
    };
    
    /**
     * returns column count
     *
     * @return a value of type 'int'
     */
    public int getColumnCount() {

	return columnNames.length;
    }
    
    /**
     * returns row count
     *
     * @return a value of type 'int'
     */
    public int getRowCount() {

	return data.length;
    }
    
    /**
     * returns column name
     *
     * @param col a value of type 'int'
     * @return a value of type 'String'
     */
    public String getColumnName(int col) {
	
	return columnNames[col];
    }
    
    /**
     * returns value at specified spot
     *
     * @param row a value of type 'int'
     * @param col a value of type 'int'
     * @return a value of type 'Object'
     */
    public Object getValueAt(int row, int col) {
	
	return data[row][col];
    }
    
    /**
     *JTable uses this method to determine the default renderer/
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
    
    /**
     * returns whether a cell is editable or not
     *
     * @param row a value of type 'int'
     * @param col a value of type 'int'
     * @return a value of type 'boolean'
     */
    public boolean isCellEditable(int row, int col) {
	//Note that the data/cell address is constant,
	//no matter where the cell appears onscreen.
	
	if (col < 1) { 
	    return false;
	} 
	
	else {
	    return true;
	}
    }
    
    /**
     * sets the value at the specified spot.
     *
     * @param value a value of type 'Object'
     * @param row a value of type 'int'
     * @param col a value of type 'int'
     */
    public void setValueAt(Object value, int row, int col) {
	
	if (data[0][col] instanceof Integer                        
	    && !(value instanceof Integer)) {                  
	    
	    try {
		data[row][col] = new Integer(value.toString());
		fireTableCellUpdated(row, col);
	    } 

	    catch (NumberFormatException e) { }
	    
	} 
	
	else {
	    data[row][col] = value;
	    fireTableCellUpdated(row, col);
	}	
    }
}
