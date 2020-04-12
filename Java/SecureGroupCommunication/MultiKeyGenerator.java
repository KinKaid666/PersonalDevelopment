/* File:        $Id: MultiKeyGenerator.java,v 2.4 2003/02/19 20:44:49 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: Group secret key generation for
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.lang.*;

/**
 * MultiKeyGenerator is used to encrypt data using password
 * based encryption with MD5 and DES.
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson.
 */
public class MultiKeyGenerator
{

    private PBEKeySpec pbeKeySpec;
    private PBEParameterSpec pbeParamSpec;
    private SecretKeyFactory keyFac;
    private SecretKey pbeKey;
    private Cipher pbeCipherEncrypt;
    private Cipher pbeCipherDecrypt;
    private String password;

    /**
     * Construct a new MultiKeyGenerator.
     */
    public MultiKeyGenerator()
    {}

    /**
     * Generate a new key used for encryption.  The password is based
     * on the input group name.
     *
     * @param  GroupName  byte[] used to set up the password for encryption.
     */
    public void GenerateKey( byte[] GroupName ) {
        try {
            password = new String( GroupName );
            // Salt
            byte[] salt = {
                (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
            };

            // Iteration count
            int count = 20;

            // Create PBE parameter set
            pbeParamSpec = new PBEParameterSpec(salt, count);

            // Prompt user for encryption password.
            // Collect user password as char array (using the
            // "readPasswd" method from above), and convert
            // it into a SecretKey object, using a PBE key
            // factory.
            pbeKeySpec = new PBEKeySpec(password.toCharArray());
            keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            pbeKey = keyFac.generateSecret(pbeKeySpec);

            // Create PBE Cipher
            pbeCipherEncrypt = Cipher.getInstance
                ("PBEWithMD5AndDES/CBC/PKCS5Padding");
            pbeCipherDecrypt = Cipher.getInstance
                ("PBEWithMD5AndDES/CBC/PKCS5Padding");

            // Initialize PBE Cipher with key and parameters
            pbeCipherEncrypt.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
            pbeCipherDecrypt.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
        }
        catch( Exception e ){
            System.out.print( "MultiKeyGenerator Constructor Exception: " +
                              e.toString() );
        }
    }

    /**
     * Encrypt the input data and return the cipher text.
     *
     * @param Info  data to be encrypted.
     *
     * @return      The encrypted information.
     */
    public byte[] EncryptInformation( byte[] Info )
    {
        byte[] cipherText = null;
        try {
            cipherText = pbeCipherEncrypt.doFinal(Info);
        }
        catch( Exception e ) {
            System.out.print( "Exception in " +
                              "MultiKeyGenerator:EncryptInformation: " +
                              e.toString() );
        }
        return cipherText;
    }

    /**
     * Decrypt the input byte array and return the original data.
     *
     * @param Info  data to be decrypted.
     *
     * @return      The decrypted information.
     */
    public byte[] DecryptInformation( byte[] Info )
    {
        byte[] cipherText = null;
        try {
            cipherText = pbeCipherDecrypt.doFinal(Info);
        }
        catch( BadPaddingException pde ) {
            System.out.println( "MultiKeyGenerator:DecryptInformation " +
                                "failure.\n\tBadPaddingException.");
        }
        catch( Exception e ) {
            System.out.println( "Exception in " +
                                "MultiKeyGenerator:DecryptInformation: " +
                                e.toString() );
            e.printStackTrace();
        }
        return cipherText;
    }
}
