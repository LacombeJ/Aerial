package jonl.ge.ext;

import jonl.ge.core.Property;

/**
 * Class that oscillates value from min to max with given speed each step
 * 
 * @author Jonathan Lacombe
 *
 */
public class Oscillator extends Property {

    public float min    = 0;
    public float max    = 1;
    public float speed  = 0.01f;
    public float value  = 0;
    
    public boolean up   = true;
    
    public Oscillator() {
        
    }
    
    public Oscillator(float min, float max, float speed, float value) {
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
        if (up) 
            value += speed;
        else 
            value -= speed;
        
        if (value>max) {
            value = max;
            up = false;
        } else if (value<min) {
            value = min;
            up = true;
        }
    }
    
}
