// threadtest.h

// Definition of a class that runs a simple test on the threading
// mechanism in Nachos.

// $Id: threadtest.h,v 3.0 2001/11/04 19:46:56 trc2876 Exp $

// $Log: threadtest.h,v $
// Revision 3.0  2001/11/04 19:46:56  trc2876
// force to 3.0
//
// Revision 2.0  2001/10/11 02:53:27  trc2876
// force update to 2.0 tree
//
// Revision 1.1  2001/09/23 20:19:12  etf2954
// Initial revision
//

class ThreadTest 
{
public:
    static int exits;	     // count number of threads that exited
    int id;				     // which tester is this
    
public:
    ThreadTest(int i) {id = i;}
    
    // This is the actual test thread that just prints a statement and
    // yields the processor.
    static void SimpleThread(int id);

    // Spawns the second thread and runs one copy of the test in the
    // main thread.
    static void RunTest();
};
