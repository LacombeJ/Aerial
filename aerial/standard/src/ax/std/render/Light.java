package ax.std.render;

import ax.engine.core.Component;
import ax.math.vector.Vector3;

public class Light extends Component {
    
    //TODO make different classes for different types of lights
    public final static int POINT       = 1;
    public final static int SPOT        = 2;
    public final static int DIRECTIONAL = 3;
    
    int type = POINT;
    Vector3 color = new Vector3(1,1,1);
    Vector3 ambient = new Vector3(0,0,0);
    float falloff = 0.15f;
    float radius = 10f;
    
    Vector3 direction = new Vector3(0,-1,0);
    
    public int getType() {
        return type;
    }
    
    /** Must be either {@link #POINT}, {@link #SPOT}, {@link #DIRECTIONAL},*/
    public void setType(int type) {
        this.type = type;
    }
    
    public Vector3 getColor() {
        return color.get();
    }
    
    public void setColor(Vector3 color) {
        this.color.set(color);
    }
    
    public Vector3 getAmbient() {
        return ambient.get();
    }
    
    public void setAmbient(Vector3 color) {
        this.ambient.set(color);
    }
    
    public float getFalloff() {
        return falloff;
    }
    
    public void setFalloff(float falloff) {
        this.falloff = falloff;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public Vector3 getDirection() {
        return direction.get();
    }
    
    public void setDirection(Vector3 direction) {
        this.direction.set(direction);
    }
    
}
