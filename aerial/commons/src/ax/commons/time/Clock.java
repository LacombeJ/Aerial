package ax.commons.time;

public class Clock {

    private StopWatch stopWatch;
    private TimeRate timeRate;
    
    public Clock() {
        stopWatch = new StopWatch();
        timeRate = new TimeRate();
    }
    
    public long lap() {
        return stopWatch.lap();
    }
    
    public double rate() {
        return timeRate.rate(TimeMetric.SECOND);
    }
    
}
