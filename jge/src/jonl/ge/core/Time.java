package jonl.ge.core;

import jonl.ge.base.BaseTime;
import jonl.jutils.time.Clock;
import jonl.jutils.time.TimeMetric;
import jonl.jutils.time.TimeUtils;

public class Time extends BaseTime {

    private Clock clock;
    private long timeSinceStart;
    
    public Time() {
        clock = new Clock();
    }
    
    @Override
    protected void update() {
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
