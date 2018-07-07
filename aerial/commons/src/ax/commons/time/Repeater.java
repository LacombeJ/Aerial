package ax.commons.time;

public class Repeater implements Rhythm {

    private final float min;
    private final float max;
    private final float increment;
    
    private float value;
    
    public Repeater(float initial, float min, float max, float increment) {
        if (max<=min) {
            max+=Math.abs(increment);
        }
        
        this.min = min;
        this.max = max;
        this.increment = increment;
        
        value = initial;
        if (value<min) value = min;
        else if (value>max) value = max;
    }
    
    public Repeater(float min, float max, float increment) {
        this(min,min,max,increment);
    }
    
    @Override
    public void increment() {
        value += increment;
        
        if (value>max) {
            value = min;
        } else if (value<min) {
            value = max;
        }
    }

    @Override
    public float value() {
        return value;
    }

}
