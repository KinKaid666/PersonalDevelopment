/**
 * This is an internal edit section window. It is shown when the user tries to
 * edit a section by selecting edit room via the toolbar, right mouse click or
 * from the main menu
 *
 * @version	$Id: EditSectionWindow.java,v 1.5 2000/04/26 20:22:26 aec1324 Exp $
 *
 * @author	Adam E Chipalowsky 
 *
 * Revisions:
 *		$Log: EditSectionWindow.java,v $
 *		Revision 1.5  2000/04/26 20:22:26  aec1324
 *		started looking at possable revisions for the
 *		fields
 *
 *		Revision 1.4  2000/04/24 16:30:55  p361-45a
 *		finished method headers
 *
 *		Revision 1.3  2000/04/09 20:31:38  aec1324
 *		added a gridbag layout tool to this class
 *
 *		Revision 1.2  2000/04/09 00:08:10  aec1324
 *		set permissions to group
 *
 *		Revision 1.1  2000/04/08 22:47:28  aec1324
 *		Initial revision
 *
 *		
 *
 */


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JTextField;
import java.io.*;
import java.awt.Window;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * class to make a window to edit a section
 */
public class EditSectionWindow extends JFrame {

    private static Operator operator;
    
    private JFrame frame;
    private JPanel upperPanel, panel, middlePanel, tablePanel, tempPanel;
    private JLabel emptyLabel;
    private Dimension dimen = new Dimension( 625, 195 );
    private BorderLayout borderLayout;
    private JButton okButton = null;
    private JButton cancelButton = null;
    private TableColumn tableColumn;
    private Border loweredBevel;
    private MyTableModel myModel;

    final String btnString1 = "Enter";
    final String btnString2 = "Cancel";
    private JOptionPane optionPane;


    /**
     * constructor to create an object of type EditSectionWindow
     *
     * @param oPerator a value of type 'Operator'
     */
    public EditSectionWindow( Operator oPerator ) {
	
	operator = oPerator;
	
	//create a text field and add it to the bottom panel
	final JTextField capacityText = new JTextField(4);
	final JTextField nameText = new JTextField(18);
	final JTextField numberText = new JTextField(10);
	
	String sectionNumber = " Please enter new section number:   ";
        String sectionName =   " Please enter new section name:     ";
	String sectionCap =    " Please enter new section capacity: ";
	String tableText =   "Attributes Table";
	String space = "    ";


	JLabel label1 = new JLabel( sectionNumber );
        JLabel label2 = new JLabel( sectionName );
	JLabel label3 = new JLabel( sectionCap );
	JLabel tableHeader = new JLabel( tableText );
	JLabel spacer = new JLabel( space );

	//set up the layout tool
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();


	frame = new JFrame("Edit Section Options");
	emptyLabel = new JLabel("");

	emptyLabel.setPreferredSize(new Dimension(425, 250));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
	

	//create and add the check box table for the room attributes
	myModel = new MyTableModel();
        JTable table = new JTable(myModel);
	
	//add a border around the table
	loweredBevel = BorderFactory.createLoweredBevelBorder();
	table.setBorder(loweredBevel);
	
	//set up the column widths
	tableColumn = table.getColumnModel().getColumn(0);
	tableColumn.setPreferredWidth(150);

	tableColumn = table.getColumnModel().getColumn(1);
	tableColumn.setPreferredWidth(50);
	
	panel = new JPanel();
	panel.setPreferredSize( dimen );
	panel.setLayout(gridbag);
	//panel.setBackground( Color.yellow );
	// create the buttons
	okButton = new JButton( "Enter" );
	cancelButton = new JButton( "Cancel" );

	//set the constrants on the gridbag
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 1;
	c.anchor = c.WEST;
	gridbag.setConstraints(label1, c);
	
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 1;
	c.gridy = 1;
	c.gridwidth = 2;
	c.anchor = c.WEST;
	gridbag.setConstraints(numberText, c);

	c.weightx = 0.5;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 2;
	c.gridwidth = 1;
	c.anchor = c.WEST;
	gridbag.setConstraints(label2, c);

	c.weightx = 0.5;
	c.weighty = 1.0;
	c.gridx = 1;
	c.gridy = 2;
	c.gridwidth = 3;
	c.anchor = c.WEST;
	gridbag.setConstraints(nameText, c);

	c.weightx = 0.5;
	c.weighty = 1.0;
	c.gridx = 0;
	c.gridy = 3;
	c.gridwidth = 1;
	c.anchor = c.WEST;
	gridbag.setConstraints(label3, c);

	c.weightx = 0.5;
	c.weighty = 1.0;
	c.gridx = 1;
	c.gridy = 3;
	c.anchor = c.WEST;
	gridbag.setConstraints(capacityText, c);

	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 5;
	c.gridy = 1;
	c.gridheight = 3;
	c.gridwidth = 1;
	c.anchor = c.CENTER;
	gridbag.setConstraints(table, c);

	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 3;
	c.gridy = 4;
	gridbag.setConstraints(okButton, c);
	
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 5;
	c.gridy = 4;
	gridbag.setConstraints(cancelButton, c);

	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 5;
	c.gridy = 0;
	c.anchor = c.CENTER;
	c.gridheight = 1;
	c.gridwidth = 1;
	gridbag.setConstraints(tableHeader, c);
	
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridx = 4;
	c.gridy = 0;
	c.anchor = c.CENTER;
	c.gridheight = 1;
	c.gridwidth = 1;
	gridbag.setConstraints(spacer, c);


	//create the buttons
	panel.add( tableHeader );
	panel.add( label1 );
	panel.add( numberText );
	panel.add( label2 );
	panel.add( nameText );
	panel.add( label3 );
	panel.add( capacityText );
	panel.add( table );
	panel.add( spacer );

	panel.add( okButton );
	panel.add( cancelButton );

	//allow the user to cancel the editing operation
  	cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		CloseWindowViaCancel();
	    }
	});

	okButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		CloseWindowViaOk();
	    }
	});

	frame.getContentPane().add( panel, BorderLayout.CENTER);

	frame.pack();
        frame.setVisible(true);
    }
    
     private void CloseWindowViaCancel(){
	frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
	frame.dispose();
	

    }

    private void CloseWindowViaOk(){
	
	//this should update the database and everything else...implement
	//at another time.  For now it just closes the window
	
	frame.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE  );
	frame.dispose();
	
    }

    /**
     * class to access and set datamembers
     */
    class MyTableModel extends AbstractTableModel {
	
	private String[] columnNames = {"Section Extras", " "};
	
	private Object[][] data = {
	    {" Overhead Projector",  new Boolean(false)},
	    {" Digitial Projector", new Boolean(false)},
	    {" Ethernet Connections", new Boolean(false)},
	    {" Wireless Ethernet", new Boolean(false)},
	    {" Speaker System", new Boolean(false)},
	    {" Network Printer", new Boolean(false)},
	    {" DVD player", new Boolean(false)}
	};
	    
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
	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class getColumnClass(int c) {
	    return getValueAt(0, c).getClass();
	}
	

	public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 1) { 
                return true;
            } else {
                return false;
            }
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
    





}//EditSectionWindow
