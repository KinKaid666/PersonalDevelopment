/* File:        $Id: BinaryTreeNode.java,v 2.3 2003/02/19 20:44:49 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: A node in the binary tree which acts as a Key holder and a
**                      member identifier to encapsulate the knowledge at
**                      each level of the tree.
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

import java.math.BigInteger ;

/**
 * Object stored at each location in a binary tree instance.
 * Contains the member index as an int, and the key stored at the node
 * as a BigInteger.
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson.
 */
public class BinaryTreeNode
{

    BigInteger data_ ;
    int        memberIndex_ ;

    /**
     * Construct an empty node.
     */
    public BinaryTreeNode()
    {
        memberIndex_ = -1 ;
        data_ = null ;
    }

    /**
     * Construct a node with the member index <I>memberIndex</I>
     * that will hold the key <I>data</I>.
     *
     * @param  data         BigInteger to store at this node.
     * @param  memberIndex  int index to store at this node.
     */
    public BinaryTreeNode( BigInteger data, int memberIndex )
    {
        memberIndex_ = memberIndex ;
        data_ = data ;
    }

    /**
     * Set the index stored in this node.
     *
     * @param  memberIndex  int index to store at this node.
     *
     * @return              The previous member index.
     */
    public int setMemberIndex( int memberIndex )
    {
        int temp = memberIndex_ ;
        memberIndex_ = memberIndex ;
        return temp ;
    }

    /**
     * Set the key stored at this node.
     *
     * @param  data  BigInteger data to be stored in this node.
     *
     * @return       The previous key.
     */
    public BigInteger setKey( BigInteger data  )
    {
        BigInteger temp = data_ ;
        data_ = data ;
        return temp ;
    }

    /**
     * Get the index stored at this node.
     *
     * @return  The member index.
     */
    public int getMemberIndex() { return memberIndex_ ; }

    /**
     * Get the key stored at this node.
     *
     * @return  The key.
     */
    public BigInteger getKey() { return data_ ; }
}
