package ax.std.misc;

import ax.engine.core.Property;
import ax.math.vector.Mathf;

/**
 * Class that oscillates value from min to max with given speed each step
 * 
 * @author Jonathan Lacombe
 *
 */
public class SineWave extends Property {

    public float min    = 0;
    public float max    = 1;
    public float speed  = 0.01f;
    public float value  = 0;
    private float internal = 0;
    
    public SineWave() {
        
    }
    
    public SineWave(float min, float max, float speed, float value) {
        this.min = min;
        this.max = max;
        this.speed = speed;
        this.value = value;
    }
    
    @Override
    public void create() {
        
    }
    
    @Override
    public void update() {
        internal += speed;
        value = (max - min) * (Mathf.sin(internal)+1)/2f + min;
    }
    
}
