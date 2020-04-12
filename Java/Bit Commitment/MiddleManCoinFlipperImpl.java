//**********************
//Group D
//MiddleManCoinFlipperImpl
//**********************

import java.io.*;
import edu.rit.m2mi.M2MI;
import edu.rit.m2mi.SynthesisException;

import java.util.Random;import edu.rit.m2mi.Handle ;
import edu.rit.m2mi.Unihandle;import java.math.BigInteger;import java.util.Hashtable;

/**
 * CoinServer waits to play coin guessing games
 * with a CoinClient.
 *
 * @authors  Jeremy Balmos, Shawn Chasse, Eric Ferguson, Jeremy Dahlgren.
 */public class MiddleManCoinFlipperImpl implements CoinFlipper, CoinGuesser {
  
	// The unihandle reference for this object.
	private Unihandle myHandle;	private CoinGuesser myHandle1;	private CoinFlipper myFlipperHandle;	private CoinGuesser theGuesser;
	private String myName = null;
    
	private Hashtable playerSet = null;
	private String myFlipper = null;
	
	private BigInteger myBlob = null; 
	/**
	 * Constructor.  	 * Sets the name and initializes the M2MI layer.
	 * 
	 */
	public MiddleManCoinFlipperImpl(String theName) {		
	    // Set the name.
	    myName = theName;
	 
	    // Initialize the M2MI layer.
	    M2MI.initialize (1234L);		 
		 playerSet = new Hashtable();
	}
   
	/**
	 * Receive the name and unihandle of a coin guessing participant.
	 * 
	 * @param theHandle  The unihandle for the participant.
	 * @param theName	String name of the participant.
	 */
	public void discoverPlayer( CoinGuesser theHandle ) {		System.out.println( "We are been discovered by " + ((Handle)theHandle).getEoid().toString() ) ;		// Send back the handle for this object.		theHandle.presentPlayer((CoinFlipper) myHandle, myName);		System.out.println( "We have being discovered" ) ;
	}
	 
	/**
	 * Receive the guess of a coin flip.
	 * 
	 * @param theGuess  (1 == heads, 0 == tails).
	 * @param theHandle  The unihandle for the response to be sent to.
	 */
	public void receiveGuess( int theGuess, CoinGuesser theHandle ) {		
		myFlipperHandle.receiveGuess( theGuess, myHandle1 );
	}
	 
	/**
	 * Start a coin flipping round.  We send the blob encoding
	 * of the flip result and the value x in the blob y.
	 * 
	 * @param theHandle  The unihandle for the participant.
	 * @param theName	String name of the participant.	 * @param m  The BigInt m value.	 * @param n  The BigInt n value.
	 */
	public void startRound(CoinGuesser theHandle, String theName, BigInteger m, BigInteger n) { 		 System.out.println("Flipping coin for " + theName + ".\n");
				 		 // Start up a new game and pick a player.
		try {			
						CoinFlipper allFlippers = (CoinFlipper) M2MI.getOmnihandle (CoinFlipper.class);
			allFlippers.discoverPlayer((CoinGuesser) myHandle);
						
	    }
	    catch(SynthesisException se){
			System.out.print( se.toString() );
	    }		 while( playerSet.isEmpty() )		 {	
			 // do nothing, we are blocking.			 try			 {
				Thread.currentThread().sleep(1);
			 }
			 catch( InterruptedException IE )
			 {
				System.out.print( IE.toString() );
			 }		 }
				 myFlipperHandle = (CoinFlipper)playerSet.get(myFlipper);		 theGuesser = theHandle;		 myFlipperHandle.startRound(myHandle1, myName, m,n );
	}
	
	/**
	 * Set the handle reference for this coin flipper
	 * 
	 */
	private void setReference() {		System.out.println("Setting references.");		// Get a reference for this object from the M2MI layer.
		try {			Class[] classes = {CoinGuesser.class, CoinFlipper.class};			System.out.println("before handle");
			myHandle = M2MI.getUnihandle(this, classes);			System.out.println("after handle");
		}
		catch(SynthesisException se){
			System.out.println(se.toString());
		}
		catch(Exception e) {
			System.out.println(e.toString());	
		}			 System.out.println( "I am (CoinFlipper)"  + myHandle.getEoid().toString() ) ;

	}
	
	/**
	 * main method.  Creates a new coin flipper.
	 * 
	 * @param args[]  Array of string arguments.
	 */
	public static void main(String args[]) {		
	   // Create the class instance. 		MiddleManCoinFlipperImpl cfi = new MiddleManCoinFlipperImpl(args[0]);
	   
		// Set the handle reference.		cfi.setReference();				// Stop the main program from exiting.
		try {
			Thread.currentThread().join();
		}
		catch(InterruptedException ie) {
			System.out.println(ie.toString());
		}
	}
	
	 
	/**
	* Discover a new player.  A unihandle and name will be given
	* as arguments.
	* 
	* @param theHandle  The unihandle for the discovered player.
	* @param theName  The string name of the player
	*/
	public void presentPlayer(CoinFlipper theHandle, String theName) {
		System.out.println( "presentPlayer(... beginning ...)" ) ;		if( !theName.equals( myName ) )
		{
					System.out.println( "presentPlayer(... the name is not me ...) Name = " + theName ) ;				// If we don't already have this player, add it to the set.
			if(!playerSet.contains(theHandle)) {
				  playerSet.put(theName, theHandle);
				  myFlipper = theName;
			}		}		System.out.println( "presentPlayer(... end ...)" ) ;
	}
	
	/**
	* Receive the blob encoding
	* of the flip result and the value x in the blob y.
	* 
	* @param theBlob  The encoded x and bit values ('y' == theBlob).
	*/
	public void receiveBlob( BigInteger theBlob ) {
		myBlob = theBlob;		
		String input = null;
				 // Send the blob.
	    theGuesser.receiveBlob(myBlob);
		
	}
	
	/**
	* Receive the answer and the x encoding value.  This will 
	* allow the guesser to verify that the result is true.
	* 
	* @param flipResult  The actual coin flip result.
	* @param x  The x encoding value.
	*/
	public void receiveResult( int flipResult, BigInteger x) {
		String result = null;
		String verified = null;
		
		// Send back the actual result and the x value used.		theGuesser.receiveResult( flipResult, x);  		
	}}
