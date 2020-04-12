package SecureGroupCommunication ;
import SecureGroupCommunication.Group;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GroupWhiteBoard extends JFrame implements ActionListener, ItemListener {

    // GUI components.
    JPanel contentPane;
    JPanel drawingPanel;
    JPanel optionPanel;
    JPanel toolPanel;
    JPanel messagePanel;
    JPanel messageInputPanel;

    JTextArea messages;
    JTextField messageInput;
    JScrollPane messagePane;
    JScrollBar scrollBar;

    JButton sendMessage;
    JButton clearMessages;
    JButton clearArea;

    DrawCanvas canvas;

    JCheckBox messaging;

    ButtonGroup group;
    JRadioButton pen;
    JRadioButton eraser;

    JComboBox colorChoice;

    private String[] colorStrings = { "Black", "Green", "Blue", "Red",
                                      "Yellow", "Orange", "Gray", "Magenta",
                                      "Cyan", "Pink" };

    // Menus used.
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu File = new JMenu("File");
    JMenuItem FileExit = new JMenuItem("Exit");
    JMenu JoinGroup = new JMenu("Join Group");
    JMenuItem Join = new JMenuItem("Join");

    // Group Object
    Group myGroup = null;
    String groupName;
    String messageName;

    // Constructor
    public GroupWhiteBoard(String theName, String screenName) {

        // Assign the group name, and messaging name.
        groupName = theName;
        messageName = screenName;

        // Main panel.
        contentPane = (JPanel) this.getContentPane();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new BorderLayout());
        this.setTitle("Group White Board");
        this.setJMenuBar(jMenuBar1);
        this.setSize(new Dimension(500, 200));
        this.setResizable(false);

        // Menus.
        FileExit.addActionListener(this);
        Join.addActionListener(this);
        File.add(FileExit);
        JoinGroup.add(Join);
        jMenuBar1.add(File);
        jMenuBar1.add(JoinGroup);

        // Set up the drawing panel.
        drawingPanel = new JPanel();
        drawingPanel.setBorder(BorderFactory.createCompoundBorder
                           (BorderFactory.createTitledBorder("White Board"),
                            BorderFactory.createEmptyBorder(3,3,3,3)));
        drawingPanel.setLayout(new BorderLayout());

        // Set up the button panel.
        optionPanel = new JPanel();
        optionPanel.setBorder(BorderFactory.createCompoundBorder
                           (BorderFactory.createTitledBorder("Options"),
                            BorderFactory.createEmptyBorder(3,3,3,3)));
        optionPanel.setLayout(new BorderLayout());

        // Tool panel.
        toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(1, 5, 2, 2));

        // Message panel.
        messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createCompoundBorder
                               (BorderFactory.createTitledBorder("Messages"),
                                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        messagePanel.setLayout(new BorderLayout());

        // Message Input Panel.
        messageInputPanel = new JPanel();
        messageInputPanel.setLayout(new BorderLayout());

        // Canvas.
        canvas = new DrawCanvas(this);

        // Buttons.
        clearArea = new JButton("Clear Board");
        clearArea.addActionListener(this);
        pen = new JRadioButton("Marker");
        pen.setSelected(true);
        pen.addActionListener(this);
        eraser = new JRadioButton("Eraser");
        eraser.addActionListener(this);

        // Radio Button group.
        group = new ButtonGroup();
        group.add(pen);
        group.add(eraser);
        canvas.penOn();
        canvas.eraseOff();

        // Color chooser.
        colorChoice = new JComboBox();
        for(int i = 0; i < colorStrings.length; i++)
            colorChoice.addItem(colorStrings[i]);
        colorChoice.addActionListener(this);

        // Message box.
        messages = new JTextArea(5, 30);
        messages.setEditable(false);

        // ScrollPane with adjustable scroll bar.
        messagePane = new JScrollPane(messages);
        scrollBar = new JScrollBar();
        messagePane.setVerticalScrollBar(scrollBar);

        // Send Message button.
        sendMessage = new JButton("Send");
        sendMessage.addActionListener(this);

        // Clear Message button.
        clearMessages = new JButton("Clear Messages");
        clearMessages.addActionListener(this);

        // Message Input Field
        messageInput = new JTextField(20);
        messageInput.addActionListener(this);

        // Messaging Choicebox
        messaging = new JCheckBox("Messaging");
        messaging.addItemListener(this);
        messaging.setSelected(true);

        // Add message components.
        messageInputPanel.add(messageInput, "Center");
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2, 3, 3));
        p.add(sendMessage);
        p.add(clearMessages);
        messageInputPanel.add(p, "East");
        messagePanel.add(messagePane, "Center");
        messagePanel.add(messageInputPanel, "South");
        messagePanel.setVisible(true);

        // Add tools to tool panel.
        toolPanel.add(colorChoice);
        toolPanel.add(pen);
        toolPanel.add(eraser);
        toolPanel.add(clearArea);
        toolPanel.add(messaging);

        // Add Messaging and tools to option panel.
        optionPanel.add(toolPanel, "North");
        optionPanel.add(messagePanel, "Center");

        // Add the canvas and option panel to the main panel.
        drawingPanel.add(optionPanel, "South");
        drawingPanel.add(canvas, "Center");

        // Add the event panel to the main panel.
        contentPane.add(drawingPanel, "Center");

        // Setup the Group Protocol object.
        myGroup = new Group(groupName, new CanvasCallback(canvas, this));

        // Make this frame visible.
        contentPane.setVisible(true);
    }

    // Send an eraser mark to the rest of the group.
    public void sendErase( int x, int y ) {
        myGroup.sendSecureMessage("ERASE " + x + " " + y);
    }

    // Send a line to the rest of the group.
    public void sendLine( int a, int b, int c, int d, int color) {
        myGroup.sendSecureMessage("LINE " + a + " " + b + " " + c +
                          " " + d + " " + color);
    }

    // Display a message from a group member.
    public void displayMessage(String s) {
        messages.setText(messages.getText() + "\n" + s);
        scrollBar.setValue(scrollBar.getMaximum());
    }

    // Action Events
    public void actionPerformed(ActionEvent e) {

        // Exit the system.
        if(e.getSource() == FileExit) {
            if(myGroup != null)
                myGroup.leave();
            System.exit(0);
        }

        // Check the send button and a 'RETURN' press.
        if(e.getSource() == sendMessage || e.getSource() == messageInput) {
            String concat = new String("MESSAGE " + messageName + " " +
                                       messageInput.getText());
            myGroup.sendSecureMessage(concat);
            messages.setText(messages.getText() + "\n" +
                             messageName + ": " + messageInput.getText());
            scrollBar.setValue(scrollBar.getMaximum());
            messageInput.setText("");
        }

        // Change Groups.
        if(e.getSource() == Join) {
            String s = JOptionPane.showInputDialog("Enter Group Name To " +
                                                   "Join:");
            /* s will be null if cancel was pressed */
            if( s != null )
            {
                if(myGroup != null)
                    myGroup.leave();
                groupName = s;
                myGroup = new Group(groupName,
                                    new CanvasCallback(canvas, this));
            }
        }

        // Change colors.
        if(e.getSource() == colorChoice) {
            canvas.setColor(colorChoice.getSelectedIndex());
        }

        // Clear the messages.
        if(e.getSource() == clearMessages) { messages.setText(""); }

        // Clear the drawing board.
        if(e.getSource() == clearArea) {
            canvas.eraseAll();
        }

        // Enable the pen.
        if(e.getSource() == pen) {
            canvas.penOn();
            canvas.eraseOff();
        }

        // Enable the eraser.
        if(e.getSource() == eraser) {
            canvas.eraseOn();
            canvas.penOff();
        }
    }

    // Checkbox state change.  Update messaging components.
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            messagePanel.setVisible(false);
        }
        else {
            messagePanel.setVisible(true);
        }
    }

    // Main method.
    public static void main(String args[]) {

        GroupWhiteBoard wb = null;
        String s = new String("ANONYMOUS");

        if(args.length == 0 || args.length > 2) {
            System.out.println("USAGE: java GroupWhiteBoard <group_name> " +
                               "[messaging_name]");
            System.exit(-1);
        }

        if(args.length == 1)
            wb = new GroupWhiteBoard(args[0], s);
        else if(args.length == 2)
            wb = new GroupWhiteBoard(args[0], args[1]);

        wb.pack();
        wb.setVisible(true);
    }
}
