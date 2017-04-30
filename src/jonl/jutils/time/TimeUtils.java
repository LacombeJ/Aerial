package jonl.jutils.time;

public class TimeUtils {

    public enum TimeMetric {
        
        NANO        (0,1000),
        MICRO       (1,1000),
        MILLI       (2,1000),
        SECOND      (3,60),
        MINUTE      (4,60),
        HOUR        (5,24),
        DAY         (6,7),
        WEEK        (7,4.34524),
        MONTH       (8,12),
        YEAR        (9,10),
        DECADE      (10,10),
        CENTURY     (11,10);
        
        int id;
        double parts;
        
        TimeMetric(int id, double parts) {
            this.id = id;
            this.parts = parts;
        }
        
        static TimeMetric get(int id) {
            for (TimeMetric tm : values()) {
                if (tm.id==id) return tm;
            }
            return null;
        }
        
    }

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
