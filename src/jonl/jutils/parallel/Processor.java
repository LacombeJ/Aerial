package jonl.jutils.parallel;

public interface Processor {

    public void run();
    
    /**
     * Calls Thread.sleep for this given number of milliseconds
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
