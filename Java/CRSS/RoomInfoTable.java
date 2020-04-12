//import javabook.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class RoomInfoTable extends JTable{ 

    private JTable roomTable;
    private Dimension tableSize = new Dimension( 500, 70 );
    private int rows = 5;
    private int col = 14;

    public JTable RoomInfoTable(){
	
	// Create the table
	Object[][] data = {
            {"Mary", "Campione", 
             "Snowboarding", new Integer(5), new Boolean(false)},
            {"Alison", "Huml", 
             "Rowing", new Integer(3), new Boolean(true)},
            {"Kathy", "Walrath",
             "Chasing toddlers", new Integer(2), new Boolean(false)},
            {"Mark", "Andrews",
             "Speed reading", new Integer(20), new Boolean(true)},
            {"Angela", "Lih",
             "Teaching high school", new Integer(4), new Boolean(false)}
        };

        String[] columnNames = {"First Name", 
                                "Last Name",
                                "Sport",
                                "# of Years",
                                "Vegetarian"};

	roomTable = new JTable( data, columnNames );
	roomTable.setShowHorizontalLines( true );
	roomTable.setShowVerticalLines( true );
	
	roomTable.setPreferredScrollableViewportSize(tableSize);
       
	return roomTable;
    }

}

