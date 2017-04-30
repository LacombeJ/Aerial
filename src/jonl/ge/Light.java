package jonl.ge;

import jonl.vmath.Vector3;

public class Light extends Component {
    
    public final static int POINT       = 1;
    public final static int SPOT        = 2;
    public final static int DIRECTIONAL = 3;
    
    int type = POINT;
    
    float range = 10;
    
    Vector3 color = new Vector3(1,1,1);
    
    float intensity = 0.5f;
    
    float angle;
    
    public int getType() {
        return type;
    }
    
    /** Must be either {@link #POINT}, {@link #SPOT}, {@link #DIRECTIONAL},*/
    public void setType(int type) {
        this.type = type;
    }
    
    public float getRange() {
        return range;
    }
    
    /** Sets range for point and spot lights */
    public void setRange(float range) {
        this.range = range;
    }
    
    public Vector3 getColor() {
        return color.get();
    }
    
    public void setColor(Vector3 color) {
        this.color.set(color);
    }
    
    public float getIntensity() {
        return intensity;
    }
    
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    
    public float getAngle() {
        return angle;
    }
    
    /** Sets angle for spotlights */
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
}
