
public class Signal
{
    int x = 0 ;

    Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try {
                System.out.println("   x = " + x) ;
                System.out.println("Shutting down ...");
                //some cleaning up code...

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    });

    public static void main(String[] args)
    {
        while(true)
        {
            Thread.sleep(1000);
            x++ ;
        }
    }
}
