//*********************
//Group D
//CoinGuesser Interface
//*********************

import java.math.BigInteger;

/**
 * Interface CoinGuesser.
 *
 * @authors  Jeremy Balmos, Shawn Chasse, Eric Ferguson, Jeremy Dahlgren.
 */
public interface CoinGuesser {

	/**
	 * Receive the name and unihandle of a coin flipping player.
	 * 
	 * @param theHandle  The unihandle for the participant.
	 * @param theName	String name of the participant.
	 */
	public void presentPlayer( CoinFlipper theHandle, String theName );
	 
	/**
	 * Receive the blob encoding
	 * of the flip result and the value x in the blob y.
	 * 
	 * @param theBlob  The encoded x and bit values ('y' == theBlob).
	 */
	public void receiveBlob( BigInteger theBlob );
	 
	/**
	 * Receive the answer and the x encoding value.  This will 
	 * allow the guesser to verify that the result is true.
	 * 
	 * @param flipResult  The actual coin flip result (1 == heads, 0 == tails).
	 * @param x  The x encoding value.
	 */
	public void receiveResult( int flipResult, BigInteger x);
}
