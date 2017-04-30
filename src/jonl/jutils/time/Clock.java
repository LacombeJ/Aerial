package jonl.jutils.time;

import jonl.jutils.time.TimeUtils.TimeMetric;

public class Clock {

    private static StopWatch stopWatch;
    private static TimeRate timeRate;
    
    public static void clock() {
        stopWatch = new StopWatch();
        timeRate = new TimeRate();
    }
    
    public static long lap() {
        return stopWatch.lap();
    }
    
    public static double rate() {
        return timeRate.rate(TimeMetric.SECOND);
    }
    
}
