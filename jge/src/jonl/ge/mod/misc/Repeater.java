package jonl.ge.mod.misc;

import jonl.ge.core.Property;

/**
 * Class that oscillates value from min to max with given speed each step
 * 
 * @author Jonathan Lacombe
 *
 */
public class Repeater extends Property {

    public float min    = 0;
    public float max    = 1;
    public float speed  = 0.01f;
    public float value  = 0;
    
    public Repeater() {
        
    }
    
    public Repeater(float min, float max, float speed, float value) {
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
        value += speed;
        
        if (value>max || value<min) {
            value = min;
        }
    }
    
}
