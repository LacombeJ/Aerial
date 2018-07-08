package ax.commons.time;

/**
 * Utility class for getting a time period and rate such as FPS
 * 
 * @author Jonathan Lacombe
 *
 */
public class TimeRate {

    private long lastTime;

    public TimeRate() {
        lastTime = System.nanoTime();
    }

    public double period(TimeMetric t) {
        long newTime = System.nanoTime();
        long elapsedTime = newTime - lastTime;
        lastTime = newTime;
        return TimeUtils.timeConvert(elapsedTime, TimeMetric.NANO, t);
    }
    
    public double rate(TimeMetric t) {
        return 1 / period(t); //Frequency 
    }
    
    public double FPS() {
        return rate(TimeMetric.SECOND);
    }

}