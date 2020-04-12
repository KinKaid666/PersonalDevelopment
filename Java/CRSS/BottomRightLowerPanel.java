/**
  * This is the lower right panel of the Bottom right panel.  It holds the
  * Attributes table in it.
  *
  * Author: Adam Chipalowsky
  *
  * @version    $Id: BottomRightLowerPanel.java,v 1.10 2000/04/24 16:12:02 p361-45a Exp $
  *
  * @author    
  *
  * Revisions:
  *     $Log: BottomRightLowerPanel.java,v $
  *     Revision 1.10  2000/04/24 16:12:02  p361-45a
  *     finished method headers
  *
  *     Revision 1.9  2000/04/24 04:23:14  etf2954
  *     Fixed the same things as last time for the Section this time
  *     /
  *
  *     Revision 1.8  2000/04/24 04:09:40  etf2954
  *     Added the correct....(cought adam) functionality to the
  *     comparing two strings in the resetRoomAttributes()
  *
  *     Revision 1.7  2000/04/05 20:40:48  aec1324
  *     set the table so the user cannot select
  *     a row
  *
  *     Revision 1.6  2000/04/05 17:40:42  aec1324
  *     added the ability to edit the check boxes
  *
  *     Revision 1.5  2000/04/03 17:44:04  aec1324
  *     increased the size of the border to make it look nicer
  *
  *     Revision 1.4  2000/04/01 22:05:13  aec1324
  *     added the table class to this class
  *
  *     Revision 1.3  2000/03/30 00:26:31  aec1324
  *     resized so table doesn't need a scroll bar
  *
  *     Revision 1.2  2000/03/29 02:05:20  aec1324
  *     added the table to the panel and positioned it in the center.  Also am
  *     using colors to see where the panels are actually going on the GUI
  *
  */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import javax.swing.table.AbstractTableModel;

public class BottomRightLowerPanel extends JPanel {
    
    private Dimension dimen = new Dimension( 400, 190);
    private Dimension tableDimen = new Dimension ( 240, 170 );
    private Font font;
    private MyTableModel myModel;
    private TableColumn tableColumn;
    private TableColumn checkBoxesColumn;
    private Border loweredBevel;
    private JLabel label = new JLabel( "      Attribute                            Room      Section" );
    
    /**
     * constructor to create an object of type BottomRightLowerPanel
     * sets up and displays the Attributes table
     *
     */
    public BottomRightLowerPanel() {

	this.setPreferredSize( dimen );
	//this.setBackground( Color.yellow );

	this.add( label, BorderLayout.NORTH );
	
	//create the table that goes in the panel
	myModel = new MyTableModel();
        JTable table = new JTable(myModel);
	//table.setPreferredScrollableViewportSize(new Dimension(300, 200));

	table.setRowHeight( 22 );

	//set up the column widths
	tableColumn = table.getColumnModel().getColumn(0);
	tableColumn.setPreferredWidth(150);

	checkBoxesColumn = table.getColumnModel().getColumn(1);
	checkBoxesColumn.setPreferredWidth(50);

	checkBoxesColumn = table.getColumnModel().getColumn(2);
	checkBoxesColumn.setPreferredWidth(50);
	
	//create borders for the table and panel
	loweredBevel = BorderFactory.createLoweredBevelBorder();
	table.setBorder( loweredBevel );

	table.setCellSelectionEnabled(false);
	table.setSelectionBackground( Color.white);
	
	//add the table to the panel as centered
	this.add( table, BorderLayout.CENTER );
	
    }
    /**
     * method to update Room attributes
     *
     * @param newData a value of type 'Object[]'
     */
    public void updateRoomAttributes( Object[] newData ){
	myModel.resetDataVectorRoom(newData);
    }

    /**
     * method to update Section Attributes
     *
     * @param newData a value of type 'Object[]'
     */
    public void updateSectionAttributes( Object[] newData ){
	myModel.resetDataVectorSection(newData);
    }

    /**
     * This is the abstract table model class that will be used for the room
     * and section information tabl
     */
    class MyTableModel extends AbstractTableModel {
	
	private String[] columnNames = {"Room Extras", "Room", "Section"};
	
	private Object[][] data = {
	    {" Overhead Projector",  new Boolean(false), new Boolean(false) },
	    {" Digitial Projector", new Boolean(false), new Boolean(false)},
	    {" Ethernet Connections", new Boolean(false), new Boolean(false)},
	    {" Wireless Ethernet", new Boolean(false), new Boolean(false)},
	    {" Speaker System", new Boolean(false), new Boolean(false)},
	    {" Network Printer", new Boolean(false), new Boolean(false)},
	    {" DVD player", new Boolean(false), new Boolean(false)}
	};
	    
	/**
	 * method to get Column Count
	 *
	 * @return a value of type 'int'
	 */
	public int getColumnCount() {
	    return columnNames.length;
	}
	
	/**
	 * method to get Row Count
	 *
	 * @return a value of type 'int'
	 */
	public int getRowCount() {
	    return data.length;
	}
	
	/**
	 * method to get Column Name
	 *
	 * @param col a value of type 'int'
	 * @return a value of type 'String'
	 */
	public String getColumnName(int col) {
	    return columnNames[col];
	}
	
	/**
	 * method to get Value at the specified spot
	 *
	 * @param row a value of type 'int'
	 * @param col a value of type 'int'
	 * @return a value of type 'Object'
	 */
	public Object getValueAt(int row, int col) {
	    return data[row][col];
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
	
	/**********************************************************
	 *
	 * DO NOT DELETE THE FOLLOWING COMMENTED OUT CODE!!!!!!!!!
	 *
	 * going to need to copy it for the edit room window, need as 
	 * refrence casue my memory sucks and I con't remember how to do it
	 **********************************************************
	 */

	/*
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
	*/


	//the following methods will update the checkboxes in both columns
	public void resetDataVectorRoom( Object[] newData){
	
	    //need to update the data and fire an alert to update the table
	    for( int i=0; i<7; i++){
		if ( newData[i].equals( "1" ) ){
		    data[i][1] = new Boolean(true);
		}
		else{
		    data[i][1] = new Boolean(false);
		}
	    }
	    fireTableDataChanged();
	    
	}

	public void resetDataVectorSection( Object[] newData){
	    
	    for( int i=0; i<7; i++){
		if ( newData[i].equals( "1" ) ){
		    data[i][2] = new Boolean(true);
		}
		else{
		    data[i][2] = new Boolean(false);
		}
	    }
	    fireTableDataChanged();
	}

    }//class MyTableModel

}
