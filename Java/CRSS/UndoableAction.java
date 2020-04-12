/**
 * Interface for undoable objects
 *
 * @version    $Id: UndoableAction.java,v 1.2 2000/04/18 13:30:53 p361-45a Exp $
 *
 * @author     Christopher Bunk
 * @author     Adam Chipalowsky
 * @author     Dan Cooley
 * @author     Eric Fergesun
 * @author     Phil Light
 *
 * Revisions:
 *     $Log: UndoableAction.java,v $
 *     Revision 1.2  2000/04/18 13:30:53  p361-45a
 *     finished method headers
 *
 *     Revision 1.1  2000/04/16 22:51:51  p361-45a
 *     Initial revision
 *
 */

public interface UndoableAction{

    /**
     * will call correct undo
     *
     */
    public abstract void undo();
    
}
