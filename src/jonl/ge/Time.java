package jonl.ge;

import jonl.jutils.time.Clock;
import jonl.jutils.time.TimeMetric;
import jonl.jutils.time.TimeUtils;

public class Time {

    private Clock clock;
    private long timeSinceStart;
    
    Time() {
        clock = new Clock();
    }
    
    /** Call this only once per frame by application */
    void update() {
        timeSinceStart = clock.lap();
    }
    
    /** Return time since start of app in milliseonds */
    public float ms() {
        return (float) TimeUtils.timeConvert(timeSinceStart, TimeMetric.NANO, TimeMetric.MILLI);
    }
    
    /** Return time since start of app in seconds */
    public float seconds() {
        return (float) TimeUtils.timeConvert(timeSinceStart, TimeMetric.NANO, TimeMetric.SECOND);
    }
    
    
}
