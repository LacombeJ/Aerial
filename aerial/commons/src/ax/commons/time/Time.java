package ax.commons.time;

public class Time {

    private final long msTime;
    
    public Time() {
        msTime = System.currentTimeMillis();
    }
    
    public long elapsed() {
        return System.currentTimeMillis() - msTime;
    }
    
    public double elapsed(TimeMetric metric) {
        return TimeUtils.timeConvert(elapsed(), TimeMetric.MILLI, metric);
    }
    
    public long elapsed(Time t) {
        return t.msTime - msTime;
    }
    
    public double elapsed(Time t, TimeMetric metric) {
        return TimeUtils.timeConvert(elapsed(t), TimeMetric.MILLI, metric);
    }
    
}
