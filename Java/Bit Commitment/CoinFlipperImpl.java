//**********************
//Group D
//CoinFlipperImpl
//**********************

import java.io.*;
import edu.rit.m2mi.M2MI;import edu.rit.m2mi.Handle;
import edu.rit.m2mi.SynthesisException;

import java.util.Random;import java.math.BigInteger;

/**
 * CoinServer waits to play coin guessing games
 * with a CoinClient.
 *
 * @authors  Jeremy Balmos, Shawn Chasse, Eric Ferguson, Jeremy Dahlgren.
 */
public class CoinFlipperImpl implements CoinFlipper {
  
        // The unihandle reference for this object.
        private CoinFlipper myHandle;
        private String myName = null;
      // Bit commitment variables.
        private BigInteger myBlob = null;
        private BigInteger theX = null;
        private int theBit = -1;
  
        /**
         * Constructor.        * Sets the name and initializes the M2MI layer.
         * 
         */
        public CoinFlipperImpl(String theName) {              
            // Set the name.
            myName = theName;
         
            // Initialize the M2MI layer.
            M2MI.initialize (1234L);  }
   
        /**
         * Receive the name and unihandle of a coin guessing participant.
         * 
         * @param theHandle  The unihandle for the participant.
         * @param theName       String name of the participant.
         */
        public void discoverPlayer( CoinGuesser theHandle ) {
                System.out.println( "We are been discovered by " + ((Handle)theHandle).getEoid().toString() ) ;               // Send back the handle for this object.              theHandle.presentPlayer(myHandle, myName);
                System.out.println( "We have been discovered" ) ;     }
         
        /**
         * Receive the guess of a coin flip.
         * 
         * @param theGuess  (1 == heads, 0 == tails).
         * @param theHandle  The unihandle for the response to be sent to.
         */
        public void receiveGuess( int theGuess, CoinGuesser theHandle ) {                           // Send back the actual result and the x value used.          theHandle.receiveResult(theBit, theX);  
        }
         
        /**
         * Start a coin flipping round.  We send the blob encoding
         * of the flip result and the value x in the blob y.
         * 
         * @param theHandle  The unihandle for the participant.
         * @param theName       String name of the participant.        * @param m  The BigInt m value.       * @param n  The BigInt n value.
         */
        public void startRound(CoinGuesser theHandle, String theName, BigInteger m, BigInteger n) {           System.out.println("Flipping coin for " + theName + ".\n");
                
                // Flip the coin.             if(Math.random() < 0.5)
                        theBit = 0;           else
                        theBit = 1;
                
                // Generate the x.            theX = BigInteger.probablePrime(512, new Random());           
            // Calculate the blob.
            myBlob = ((m.pow(theBit)).multiply(theX.pow(2))).mod(n);
                
            System.out.println("The flip = " + theBit + " the blob = " + myBlob.toString() + ".\n");
                               // Send the blob.
            theHandle.receiveBlob(myBlob);
        }
        
        /**
         * Set the handle reference for this coin flipper
         * 
         */
        private void setReference() {                       // Get a reference for this object from the M2MI layer.
                try {
                        myHandle = (CoinFlipper) M2MI.getUnihandle ((CoinFlipper) this, CoinFlipper.class);
                }
                catch(SynthesisException se){
                        System.out.println(se.toString());
                }      System.out.println( "I am "  + ((Handle)myHandle).getEoid().toString() ) ;

        }
        
        /**
         * main method.  Creates a new coin flipper.
         * 
         * @param args[]  Array of string arguments.
         */
        public static void main(String args[]) {              
           // Create the class instance.              CoinFlipperImpl cfi = new CoinFlipperImpl(args[0]);
           
                // Set the handle reference.          cfi.setReference();                         // Stop the main program from exiting.
                try {
                        Thread.currentThread().join();
                }
                catch(InterruptedException ie) {
                        System.out.println(ie.toString());
                }
        }}
