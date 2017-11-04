package jonl.jutils.time;

public class TimeUtils {

    public static final double timeConvert(double time, TimeMetric source, TimeMetric destination) {
        if (source.id == destination.id) {
            return time;
        }
        if (source.id < destination.id) {
            return timeConvert( time / source.parts, TimeMetric.get(source.id+1), destination );
        }
        //else (source.id > destination.id)
        return timeConvert( time * TimeMetric.get(source.id-1).parts, TimeMetric.get(source.id-1), destination );
    }
    
    public static final double nanoToMilli(long nano) {
        return timeConvert(nano,TimeMetric.NANO,TimeMetric.MILLI);
    }
    
    public static final double milliToSeconds(long milli) {
        return timeConvert(milli,TimeMetric.MILLI,TimeMetric.SECOND);
    }
    
    
}
