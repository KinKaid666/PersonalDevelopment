import java.lang.ref.WeakReference ;

class FakeClassToForceGC
{
    private static boolean gcHappened = false ;
    public static boolean hasGCHappened() { return gcHappened ; }
    public static void forceGC()
    {
        while( !FakeClassToForceGC.hasGCHappened() )
        {
            new FakeClassToForceGC() ;
        }
    }

    protected void finalize()
    {
        gcHappened = true ;
    }
}

public class WeakReferenceTest
{
    public static void main(String args[])
    {
        WeakReference<String> weakStringReference ;
        {
            String s = new String("Hello, World") ;
            weakStringReference = new WeakReference<String>(s) ;
            s = null ;
        }

        FakeClassToForceGC.forceGC() ;
        System.out.println("GC ran!") ;

        if( weakStringReference.get() != null )
        {
            System.out.println("Got '" + weakStringReference.get() + "'") ;
        }
        else
        {
            System.out.println("Could not get it") ;
        }
    }
}
