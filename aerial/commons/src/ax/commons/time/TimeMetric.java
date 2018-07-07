package ax.commons.time;

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
