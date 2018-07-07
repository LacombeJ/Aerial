package ax.commons.parallel;

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
    
    public static void thread(Processor process) {
        SequentialProcessor sp = new SequentialProcessor();
        sp.add(process);
        sp.run();
    }
    
}
