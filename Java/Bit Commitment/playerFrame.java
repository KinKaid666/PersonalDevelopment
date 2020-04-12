//*********************
//Group D
//playerFrame
//*********************

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Hashtable;

/**
 * playerFrame allows the user to select a 
 * player and start a new coin guessing game.
 *
 * @author  Jeremy Dahlgren
 */
public class playerFrame extends JFrame implements ActionListener {

	// Component Variables.
  JButton startGame;
  JComboBox playerChoices;
  JPanel playerChoicePanel;
  
  // Used to store available players.
  Hashtable playerSet;
  
  // Unihandle reference for the client.
  CoinGuesser myHandle = null;
  
  // Name of the guesser
  String myName = null;
  
  // The player selected by the user.
  CoinFlipper theSelectedPlayer = null;
  
  CoinGuesserImpl myParent = null;
  
  /**
   * Constructor.  Sets up frame components.
   *
   * @param myTitle  String used for the title of this frame.  
   */
  public playerFrame(String myTitle) {
	super(myTitle);
	playerSet = new Hashtable();
	playerChoices = new JComboBox();
	playerChoices.addActionListener(this);
			 
	playerChoicePanel = new JPanel();
	playerChoicePanel.setBorder(BorderFactory.createCompoundBorder(
	                 BorderFactory.createTitledBorder("Available Players"),
	                 BorderFactory.createEmptyBorder(5,5,5,5)));
	playerChoicePanel.add(playerChoices);
			 
	startGame = new JButton("                                  " + 
	 								"Start Game" +
	 								"                                  ");
	startGame.addActionListener(this);
			 
	this.getContentPane().add(playerChoicePanel, "Center");
	this.getContentPane().add(startGame, "South");
	this.pack();
  }
  
  /**
   * actionPerformed.  Sets the selected reporter if the event
   * was the choicebox.  If it was the report button, then the report
   * is sent back to the parent GUI monitor.  
   *
   * @param e  The ActionEvent.  
   */
  public void actionPerformed(ActionEvent e) {
	  
	  // Update the selected reporter.
	  if(e.getSource() == playerChoices) {
		   String thePlayerChoice = (String) playerChoices.getSelectedItem();
			theSelectedPlayer = (CoinFlipper) playerSet.get(thePlayerChoice);
			myParent.setPlayer(theSelectedPlayer, thePlayerChoice);
	  }
	  
	  // Start the report.
	  if(e.getSource() == startGame) {
		  if(theSelectedPlayer != null) {
			theSelectedPlayer.startRound(myHandle, myName, myParent.getM(), myParent.getN());
		  }
		  else
			  JOptionPane.showMessageDialog(null, "Must select a player.");
		  this.setVisible(false);
	  }
  }
  
  /**
   * addPlayer is called if an available coin flipper in the m2mi layer
   * has sent back a response.  The set of choices is updated for the user.  
   *
   * @param theHandle  The new available coin flipper.  
   */
  public void addPlayer(CoinFlipper theHandle, String theName) {
	  
	  // If we don't already have this player, add it to the set.
	  if(!playerSet.contains(theHandle)) {
		  playerSet.put(theName, theHandle);
		  playerChoices.addItem(theName);
	  }
  }
	
  /**
   * reset is called when this frame has been hidden and then displayed again.  
   * The data structures are reset.
   *
   */
  public void reset() {
		playerSet = new Hashtable();
		playerChoices.removeAllItems();
  }
  
  /**
   * setReference is used to set the handle reference and name
   * of the application using this frame.  
   *
   * @param theParent  The parent application reference.
   * @param theHandle  The unihandle for the coin guesser.
   * @param theName  The string name of the coin guesser.
   */
  public void setReference( CoinGuesserImpl theParent, CoinGuesser theHandle, String theName) {
	  myHandle = theHandle;
	  myName = theName;
	  myParent = theParent;
  }
}
