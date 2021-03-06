package ax.commons.time;

/**
 * Utility class for time stamping using System.nanoTime
 * 
 * @author Jonathan Lacombe
 *
 */
public class StopWatch {

    private long startTime;

    public StopWatch() {
        startTime = System.nanoTime();
    }

    /**
     * Returns nanoseconds since creation of StopWatch
     * @return
     */
    public long lap() {
        return System.nanoTime() - startTime;
    }
    
    /**
     * Calls lap and prints result
     * @see #lap()
     */
    public long print() {
        long lap = lap();
        System.out.println(lap);
        return lap;
    }

}