/* File:        $Id: BinaryTree.java,v 2.3 2003/02/19 20:44:49 etf2954 Exp $
** Author:      p590-99d
** Contributors:Balmos,   Jeremy
**              Chasse,   Shawn
**              Dahlgren, Jeremy
**              Ferguson, Eric
** Description: BinaryTree for the ends of a SecureGroupCommunication.
**              This is different from a regular binary tree in the fact that
**              it's represented as an array.  In this representation the root
**              is at vector[0].  A node, of index i, has its left child at
**              index (((i + 1) * 2) - 1), and right child at index
**              ((i + 1) * 2).
**
**              Insersion of a node follows the following rules
**              1.) always insert closest to the root
**              2.) all members are leaves
**              3.) every node has 2 or 0 childer
**
**              Removal follows the same rules but in reverse
**
**              BinaryTreeNodes with the
**              memberIndex of -1 are intermediary nodes and anything greater
**              than zero is the memberIndex of an actual group member.  Since
**              group member can only exist as leaves in this tree all the
**              intermediary level will have nodes with a member index of -1.
**              All nodes with a memberIndex of 0 are place holders in our
**              vector object.
**
**              Ex. Level               Root
**                      0               -1
**                                    /    \
**                      1           -1      3
**                                /    \
**                      2        1      2
**
**              Array representation of this is
**              int[] = { -1, -1, 3, 1, 2, 0, 0 } ;
**
** Revisions:
**              use `cvs log <filename>`
*/

package SecureGroupCommunication ;

/* SecureGroupCommunication's includes */

import SecureGroupCommunication.BinaryTreeNode ;

import java.util.Vector ;
import java.lang.Math ;
import java.math.BigInteger ;

/**
 *              BinaryTree for the ends of a SecureGroupCommunication.
 *              This is different from a regular binary tree in the fact that
 *              it's represented as an array.  In this representation the root
 *              is at vector[0].  A node, of index i, has its left child at
 *              index (((i + 1) * 2) - 1), and right child at index
 *              ((i + 1) * 2).<BR><BR>
 *
 *              Insersion of a node follows the following rules<BR>
 *              1.) always insert closest to the root<BR>
 *              2.) all members are leaves<BR>
 *              3.) every node has 2 or 0 children<BR><BR>
 *
 *              Removal follows the same rules but in reverse<BR><BR>
 *
 *              BinaryTreeNodes with the
 *              memberIndex of -1 are intermediary nodes and anything greater
 *              than zero is the memberIndex of an actual group member.  Since
 *              group member can only exist as leaves in this tree all the
 *              intermediary level will have nodes with a member index of -1.
 *              All nodes with a memberIndex of 0 are place holders in our
 *              vector object.<BR><BR>
 *
 * @author Jeremy Balmos, Shawn Chasse, Jeremy Dahlgren, Eric Ferguson.
 */
public class BinaryTree
{

    private int    members_ ;
    private int    height_ ; ;
    private Vector tree_ ;
    private int    nextMemberIndex_ ;

    /**
     * Constructs a new binary tree with nothing in it.
     */
    public BinaryTree()
    {
        tree_            = new Vector(0) ;
        members_         = 0 ;
        nextMemberIndex_ = 1 ;
        height_          = 0 ;
    }

    /**
     * Constructs a new binary using the given array representation.
     */
    public BinaryTree( int[] rep )
    {
        tree_ = new Vector(0) ;
        setTreeRepresentation( rep ) ;
    }

    /**
     * Insert a new node into this tree, storing the given BigInteger value.
     *
     * @param  data     BigInteger to store.
     *
     * @return          The member index of the newly added node.
     */
    public int addNode( BigInteger data )
    {
        int initialCapacity = nodeCapacity() ;
        ++members_ ;

        /* resize if necessary */
        if( members_ > memberCapacity() )
        {
            height_++ ;
            int newCapacity = nodeCapacity() ;
            tree_.setSize( newCapacity ) ;

            /* fill the tree in with "empty" BinaryTreeNodes as markers */
            for( int i = initialCapacity ; i < newCapacity ; i++ )
                tree_.setElementAt( new BinaryTreeNode( null, 0 ), i ) ;
        }

        /* find a place to insert the new node */
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            /*
            ** if we find an "empty" BinaryTreeNode, this is where the insertion
            ** will take place
            */
            if( ((BinaryTreeNode)tree_.get(i)).getMemberIndex() == 0 )
            {
                /* special case, 'cause the root has no parent */
                if( i == 0 )
                {
                    ((BinaryTreeNode)tree_.get(0)).setMemberIndex(
                                                        nextMemberIndex_++ ) ;
                }
                else
                {
                    ((BinaryTreeNode)tree_.get(i)).setMemberIndex(
                        ((BinaryTreeNode)tree_.get(parent(i)))
                                                .setMemberIndex(-1)) ;
                    ((BinaryTreeNode)tree_.get(i)).setKey(
                        ((BinaryTreeNode)tree_.get(parent(i))).setKey(null)) ;
                    ((BinaryTreeNode)tree_.get(rightSibling(i)))
                                        .setMemberIndex(nextMemberIndex_++) ;
                    ((BinaryTreeNode)tree_.get(rightSibling(i))).setKey(data) ;
                }
                return nextMemberIndex_ - 1 ;

            }
        }
        /* error* */
        return -1 ;
    }

    /**
     *  Remove a node.
     *
     *  @param  memberIndex     The member index of the node to be removed.
     */
    public void removeNode( int memberIndex )
    {
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            if( ((BinaryTreeNode)tree_.get(i)).getMemberIndex() == memberIndex )
            {
                if( isLeftNode(i) )
                {
                    ((BinaryTreeNode)tree_.get(i)).setMemberIndex(0) ;
                    ((BinaryTreeNode)tree_.get(i)).setKey(null) ;

                    /* move right subtree up one */
                    moveSubTreeUp( rightSibling(i) ) ;
                }
                else if( isRightNode(i) )
                {
                    ((BinaryTreeNode)tree_.get(i)).setMemberIndex(0) ;
                    ((BinaryTreeNode)tree_.get(i)).setKey(null) ;

                    /* move left subtree up one */
                    moveSubTreeUp( leftSibling(i) ) ;
                }
                else
                {
                    /* root case, nothing left in tree */
                    tree_.setSize(0) ;
                }
                /*
                ** check to see if the last memberCapacity() isn't being used
                ** that means there is an entire level of nodes unused and they
                ** must be removed
                */
                boolean removeLevel = true ;
                for( int j = tree_.size() - 1 ;
                                 j > (nodeCapacity() - memberCapacity()) ; j-- )
                {
                    if( ((BinaryTreeNode)tree_.get(j)).getMemberIndex() != 0 )
                    {
                        removeLevel = false ;
                        break ;
                    }
                }
                if( removeLevel )
                {
                    height_-- ;
                    tree_.setSize( nodeCapacity() ) ;
                }
                members_-- ;
                return ;
            }
        }
    }

    /**
     *  Move a subtree up one level.
     *
     *  @param  child   The array index of the subtree rooted at child.
     */
    private void moveSubTreeUp( int child )
    {
        int destinationIndex = parent(child) ;

        int levelIndex = 0 ;
        do
        {
            int breadth = (int)Math.pow(2,levelIndex) ;
            for( int i = 0 ; i < breadth ; i++ )
            {
                moveNode( child + i, destinationIndex + i ) ;
            }
            levelIndex++ ;
            destinationIndex = leftChild(destinationIndex) ;
        } while( (child = leftChild(child)) < nodeCapacity() ) ;
    }

    /**
     *  Move a node.
     *
     *  @param  sourceIndex             Array index of the source.
     *  @param  destinationIndex        Array index of the destination.
     */
    private void moveNode( int sourceIndex, int destinationIndex )
    {
        ((BinaryTreeNode)tree_.get(destinationIndex)).setMemberIndex(
            ((BinaryTreeNode)tree_.get(sourceIndex)).setMemberIndex(0)) ;
        ((BinaryTreeNode)tree_.get(destinationIndex)).setKey(
            ((BinaryTreeNode)tree_.get(sourceIndex)).setKey(null)) ;
    }

    /**
     *  Get the array representation of the tree.
     *
     *  @return         An integer array of all the member indexes
     *                  representing the underlying array of the binary tree.
     */
    public int[] getTreeRepresentation()
    {
        int[] rep = new int[nodeCapacity()] ;
        for( int i = 0 ; i < nodeCapacity() ; i++ )
            rep[i] = ((BinaryTreeNode)tree_.get(i)).getMemberIndex() ;
        return rep ;
    }

    /**
     *  Set the array representation of the tree.
     *
     *  @param  rep     The representation of the tree to be set (filled
     *                  will member indexes.
     */
    public void setTreeRepresentation( int[] rep )
    {
        int temp ;
        height_ = 0 ;
        while( nodeCapacity() < rep.length )
            height_++ ;

        tree_.setSize(nodeCapacity()) ;
        members_ = 0 ;

        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            if( i >= rep.length )
                temp = 0 ;
            else
                temp = rep[i] ;

            if( temp > 0 )
                members_++ ;
            tree_.setElementAt( new BinaryTreeNode( null, temp ), i ) ;
        }
    }

    /**
     *  Get a list of all the member indexes in the tree.
     *
     *  @return         A array of all the member's member indexes.
     */
    public int[] getMemberIndexes()
    {
        int[] members = new int[members_] ;
        int member_index = 0 ;
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            int temp = ((BinaryTreeNode)tree_.get(i)).getMemberIndex() ;
            if( temp > 0 )
                members[member_index++] = temp ;
        }
        return members ;
    }

    /**
     *  Get a list of all siblings of a member.
     *
     *  @param  level           The level from which you want to consider
     *                          siblings.
     *  @param  memberIndex     The member index of the node whos
     *                          siblings you are looking for.
     *
     *  @return                 The array of all the sibling's member indexes.
     */
    public int[] getSiblings( int level, int memberIndex )
    {
        Vector members = new Vector(0) ;
        int root ;
        for( root = 0 ; root < tree_.size() ; root++ )
            if( ((BinaryTreeNode)tree_.get(root)).getMemberIndex() == memberIndex )
                break ;
        if( root == tree_.size() ) return null ;

        int iterations = level(root) - level ;
        for( int i = 0 ; i < iterations ; i++ )
            root = parent(root) ;

        if( root < 0 ) return null ;

        int levelIndex = 0 ;
        do
        {
            int breadth = (int)Math.pow(2,levelIndex) ;
            for( int j = 0 ; j < breadth ; j++ )
            {
                int temp = ((BinaryTreeNode)tree_.get(root + j)).getMemberIndex() ;
                if( (temp > 0) && (temp != memberIndex) )
                    members.add(new Integer(temp)) ;

            }
            levelIndex++ ;
        } while( (root = leftChild(root)) < nodeCapacity() ) ;
        int[] rep = new int[members.size()] ;
        for( int i = 0 ; i < rep.length ; i++ )
            rep[i] = ((Integer)members.get(i)).intValue() ;
        return rep ;
    }

    /**
     *  Get the array index of leftmost child.
     *
     *  @param  i       Root of subtree in question.
     *
     *  @return         Leftmost child of subtree.
     */
    public int getLeftMember( int i )
    {
        while( i < nodeCapacity() )
        {
            int temp = ((BinaryTreeNode)tree_.get(i)).getMemberIndex() ;
            if( temp > 0 ) return temp ;
            i = leftChild(i) ;
        }
        return -1 ;
    }

    /**
     *  Get the array index of rigthmost child.
     *
     *  @param  i       Root of subtree in question.
     *
     *  @return         Rightmost child of subtree.
     */
    public int getRightMember( int i )
    {
        while( i < nodeCapacity() )
        {
            int temp = ((BinaryTreeNode)tree_.get(i)).getMemberIndex() ;
            if( temp > 0 ) return temp ;
            i = rightChild(i) ;
        }
        return -1 ;
    }

    /**
     *  Print the tree representation.
     */
    public void printTreeRepresentation()
    {
        System.out.print( "Tree = { " ) ;
        int size = tree_.size() ;
        for( int i = 0 ; i < size ; i++ )
        {
            System.out.print( ((BinaryTreeNode)tree_.get(i)).getMemberIndex()) ;
            if( i != ( size - 1) )
                System.out.print( ", " ) ;
        }
        System.out.print( " }\n" ) ;
    }

    /**
     *  Get the lowest memember index.
     *
     *  @return         The lowest member index in the binary tree.
     */
    public int getLowestMemberIndex()
    {
        int x = Integer.MAX_VALUE ;
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            int temp = ((BinaryTreeNode)tree_.get(i)).getMemberIndex() ;
            if( (temp > 0) && (temp < x ))
                x = temp ;
        }
        return x ;
    }

    /**
     *  Get the height of the tree.
     *
     *  @return         The height of the tree.
     */
    public int     height()  { return height_ ; }

    /**
     *  Check to see if the tree is empty
     *
     *  @return         True if the tree represetents and empty tree.
     */
    public boolean isEmpty() { return tree_.size() == 0 ; }

    /**
     *  Return the next available member index.
     *
     *  @return         The next available member index.
     */
    public int     getNextMemberIndex() { return nextMemberIndex_ ; }

    /**
     *  Set the next available member index.
     */
    public void    setNextMemberIndex( int nextMemberIndex )
    { nextMemberIndex_ = nextMemberIndex  ; }

    /**
     *  Get the number of members that can be stored in the current tree.
     *
     *  @return         The member of the current tree can hold
     *                  (2^(height - 1)).
     */
    public  int memberCapacity() { return (int)Math.pow( 2, height_ - 1 ) ; }

    /**
     *  Get the number of nodes that can be stored in the current tree.
     *
     *  @return         The number of nodes the current tree can hold
     *                  ((2^height) - 1).
     */
    private int   nodeCapacity() { return (int)Math.pow( 2, height_ ) - 1 ; }

    /**
     *  Aquire the <I>level</I> key for member <I>memberIndex</I>.
     *
     *  @param  level           The level (zero-based).
     *  @param  memberIndex     Who's key you want.
     *
     *  @return                 The members key at that level.
     */
    public BigInteger getKey( int level, int memberIndex )
    {
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            if( ((BinaryTreeNode)tree_.get(i)).getMemberIndex() == memberIndex )
            {
                int hops = level(i) - level ;
                if( hops < 0 ) return null ;
                for( int j = 0 ; j < hops ; j++ )
                    i = parent(i) ;
                if( (i >= 0) && (i < nodeCapacity()) )
                    return ((BinaryTreeNode)tree_.get(i)).getKey() ;
            }
        }
        return null ;
    }

    /**
     *  Set the level <I>level</I> key for member <I>memberIndex</I>.
     *
     *  @param  data            The key to be set.
     *  @param  level           The level (zero-based).
     *  @param  memberIndex     Who's key you want.
     */
    public void setKey( BigInteger data, int level, int memberIndex )
    {
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            if( ((BinaryTreeNode)tree_.get(i)).getMemberIndex() == memberIndex )
            {
                int hops = level(i) - level ;
                if( hops < 0 ) return ;
                for( int j = 0 ; j < hops ; j++ )
                    i = parent(i) ;
                if( (i >= 0) && (i < nodeCapacity()) )
                {
                    ((BinaryTreeNode)tree_.get(i)).setKey(data) ;
                    return ;
                }
            }
        }
    }

    /**
     *  Get the level (zero-based) of a member.
     *
     *  @param  memberIndex     The member in question.
     *
     *  @return                 The level.
     */
    public int getLevel( int memberIndex )
    {
        for( int i = 0 ; i < tree_.size() ; i++ )
        {
            if( ((BinaryTreeNode)tree_.get(i)).getMemberIndex() == memberIndex )
            {
                return level(i) ;
            }
        }
        return -1 ;
    }

    /*
    ** Helper functions
    */
    private static int parent      ( int i ){ return (((i + 1) / 2) - 1) ; }
    private static int leftChild   ( int i ){ return (((i + 1) * 2) - 1) ; }
    private static int rightChild  ( int i ){ return  ((i + 1) * 2)      ; }
    private static int leftSibling ( int i ){ return i - 1 ; }
    private static int rightSibling( int i ){ return i + 1 ; }

    /**
     *  Get the level of the array index <I>i</I>.
     *
     *  @param  i       Array index in question.
     *
     *  @return         The level.
     */
    public static int level( int i )
    {
        int level = 0 ;
        while( ((int)Math.pow(2,level)-2) < i )
            level++ ;
        return level - 1 ;
    }

    /**
     * Is this a right node?
     */
    private static boolean isRightNode( int i )
    {
        if( i == 0 )
            return false ;
        else if( (i % 2) == 0 )
            return true ;
        else
            return false ;
    }

    /**
     *  Is this a left node?
     */
    private static boolean isLeftNode(  int i )
    {
        if( (i % 2) == 1 )
            return true ;
        else
            return false ;
    }
}
