package ax.engine.mod.ray;

import ax.math.vector.Vector3;

public class Ray {

    private final Vector3 origin;
    private final Vector3 direction;
    
    public Ray(Vector3 original, Vector3 direction) {
        this.origin = original.get();
        this.direction = direction.get();
    }
    
    public Vector3 origin() {
        return origin.get();
    }
    
    public Vector3 direction() {
        return direction.get();
    }
    
    @Override
    public String toString() {
        return "[ "+origin+" "+direction+" ]";
    }
    
}
