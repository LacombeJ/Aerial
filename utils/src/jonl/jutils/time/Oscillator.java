package jonl.jutils.time;

public class Oscillator implements Rhythm {

    private final float min;
    private final float max;
    private final float increment;
    
    private boolean up;
    private float value;
    
    public Oscillator(float initial, float min, float max, float increment) {
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
    
    public Oscillator(float min, float max, float increment) {
        this(min,min,max,increment);
    }
    
    @Override
    public void increment() {
        float i = up ? increment : -increment;
        value += i;
        if (value>max) {
            value = max;
            up = !up;
        } else if (value<min) {
            value = min;
            up = !up;
        }
    }

    @Override
    public float value() {
        return value;
    }

}
