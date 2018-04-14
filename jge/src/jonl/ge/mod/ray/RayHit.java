package jonl.ge.mod.ray;

import jonl.vmath.Vector3;

public class RayHit {

    private final float distance;
    private final Vector3 hit;
    private final Vector3 normal;
    
    RayHit(float distance, Vector3 hit, Vector3 normal) {
        this.distance = distance;
        this.hit = hit;
        this.normal = normal;
    }

    public float distance() {
        return distance;
    }
    
    public Vector3 hit() {
        return hit.get();
    }
    
    public Vector3 normal() {
        return normal.get();
    }
    
    @Override
    public String toString() {
        return "[ "+distance+" "+hit+" "+normal+" ]";
    }
    
}
