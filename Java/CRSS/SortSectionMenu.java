//File:	$Id: SortSectionMenu.java,v 1.4 2000/05/08 17:49:50 p361-45a Exp $
//Author:	Eric Ferguson
//Contrib:	None
//Descript:	Think about it later
//Revision:	
//		$Log: SortSectionMenu.java,v $
//		Revision 1.4  2000/05/08 17:49:50  p361-45a
//		fixed formatting
//
//		Revision 1.3  2000/05/03 01:50:02  pel2367
//		added the reverse selections.
//
//		Revision 1.2  2000/04/29 03:39:02  pel2367
//		placed the calls to the operator in.
//
//		Revision 1.1  2000/04/14 02:51:42  etf2954
//		Initial revision
//
//		Revision 1.5  2000/04/07 13:25:12  etf2954
//		Added the Operator class
//
//		Revision 1.4  2000/04/07 13:10:24  etf2954
//		Added the Operator class
//
//		Revision 1.3  2000/04/06 07:00:54  etf2954
//		Getting the GUIinterface to work
//
//		Revision 1.2  2000/03/28 03:15:25  p361-45a
//		Skeleton
//
//		Revision 1.1  2000/03/28 03:08:25  p361-45a
//		Initial revision
//

import javax.swing.*;
import java.awt.event.*;
import javax.swing.AbstractAction;

public class SortSectionMenu extends JMenu {
	
    private SectionNameItem sectionNameItem = 
	new SectionNameItem( new SectionNameAction() );
	
    private SectionNumberItem sectionNumberItem = 
	new SectionNumberItem( new SectionNumberAction() );
	
    private EnrollmentItem enrollmentItem = 
	new EnrollmentItem( new EnrollmentAction() );
    
    private AssignmentItem assignmentItem = 
	new AssignmentItem( new AssignmentAction() );

    private ReverseNameItem reverseNameItem = 
	new ReverseNameItem( new ReverseNameAction() );
    
    private ReverseNumberItem reverseNumberItem =
	new ReverseNumberItem( new ReverseNumberAction() );
    
    private ReverseEnrollmentItem reverseEnrollmentItem = 
	new ReverseEnrollmentItem( new ReverseEnrollmentAction() );
    
    private ReverseAssignmentItem reverseAssignmentItem =
	new ReverseAssignmentItem( new ReverseAssignmentAction() );

    private static Operator operator;
    
    private GUI gui;

    /**
     * constructor to create a sort section menu 
     *
     * @param ourGUI a value of type 'GUI'
     * @param oPerator a value of type 'Operator'
     */
    public SortSectionMenu( GUI ourGUI, Operator oPerator ) {
	
	operator = oPerator;
        gui = ourGUI;
        
	this.setText( "Sort Section by" );

	sectionNumberItem.setMnemonic( 'N' );
	this.add( sectionNumberItem );
	enrollmentItem.setMnemonic( 'E' );
	this.add( enrollmentItem );
        assignmentItem.setMnemonic( 'A' );
        this.add( assignmentItem );
	sectionNameItem.setMnemonic( 'O' );
	this.add( sectionNameItem );
        
        this.addSeparator();
        
        reverseNumberItem.setMnemonic( 'R' );
        this.add( reverseNumberItem );
        reverseEnrollmentItem.setMnemonic( 'T' );
        this.add( reverseEnrollmentItem );
        reverseAssignmentItem.setMnemonic( 'S' );
        this.add( reverseAssignmentItem );
        
        reverseNameItem.setMnemonic( 'M' );
        this.add( reverseNameItem );
        
	}
    
    class SectionNameItem extends JMenuItem {
	
	public SectionNameItem( Action act ) {
	    this.setText( "Ascending Section Name" );
	    this.addActionListener( act );
	}
    }

    class SectionNameAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( 4 );
            operator.sortSections();
	}
    }

    class SectionNumberItem extends JMenuItem {
	
	public SectionNumberItem( Action act ) {
	    this.setText( "Ascending Section Number" );
	    this.addActionListener( act );
	}
    }
	
    class SectionNumberAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( 1 );
            operator.sortSections();
	}
    }    
    
    class EnrollmentItem extends JMenuItem {
	
	public EnrollmentItem( Action act ) {
	    this.setText( "Ascending Enrollment" );
	    this.addActionListener( act );
	}
    }

    class EnrollmentAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( 2 );
            operator.sortSections();
	}
    }
    
    class AssignmentItem extends JMenuItem {
        
	public AssignmentItem( Action act ) {
            this.setText( "Ascending Assignment" );
            this.addActionListener( act );
        }
    }
    
    class AssignmentAction extends AbstractAction {
    
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( 3 );
            operator.sortSections();
        }
    }
    class ReverseNameItem extends JMenuItem {
	
	public ReverseNameItem( Action act ) {
	    this.setText( "Descending Section Name" );
	    this.addActionListener( act );
	}
    }
    
    class ReverseNameAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( -4 );
            operator.sortSections();
	}
    }

    class ReverseNumberItem extends JMenuItem {
	
	public ReverseNumberItem( Action act ) {
	    this.setText( "Descending Section Number" );
	    this.addActionListener( act );
	}
    }

    class ReverseNumberAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( -1 );
            operator.sortSections();
	}
    }
    
    class ReverseEnrollmentItem extends JMenuItem {
	
	public ReverseEnrollmentItem( Action act ) {
	    this.setText( "Descending Enrollment" );
	    this.addActionListener( act );
	}
    }

    class ReverseEnrollmentAction extends AbstractAction {
	
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( -2 );
            operator.sortSections();
	}
    }
    
    class ReverseAssignmentItem extends JMenuItem {
    
	public ReverseAssignmentItem( Action act ) {
            this.setText( "Descending Assignment" );
            this.addActionListener( act );
        }
    }
    
    class ReverseAssignmentAction extends AbstractAction {
    
	public void actionPerformed( ActionEvent event ) {
            gui.setSectionSortStyle( -3 );
            operator.sortSections();
        }
    }
}
