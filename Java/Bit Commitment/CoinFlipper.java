//*********************
//Group D
//CoinFlipper Interface
//*********************

import java.math.BigInteger;

/**
 * Interface CoinFlipper.
 *
 * @authors  Jeremy Balmos, Shawn Chasse, Eric Ferguson, Jeremy Dahlgren.
 */
public interface CoinFlipper {

        /**
         * Receive the name and unihandle of a coin guessing participant.
         * 
         * @param theHandle  The unihandle for the participant.
         * @param theName       String name of the participant.
         */
        public void discoverPlayer( CoinGuesser theHandle );
         
        /**
         * Receive the guess of a coin flip.
         * 
         * @param theGuess  (1 == heads, 0 == tails).
         * @param theHandle  The unihandle for the response to be sent to.
         */
        public void receiveGuess( int theGuess, CoinGuesser theHandle );
         
        /**
         * Start a coin flipping round.  We send the blob encoding
         * of the flip result and the value x in the blob y.
         * 
         * @param theHandle  The unihandle for the participant.
         * @param theName       String name of the participant.
         * @param m   The BigInt m value to use for this session.
         * @param n   The BigInt n value to use for this session.
         */
        public void startRound(CoinGuesser theHandle, String theName, BigInteger m, BigInteger n);
}
