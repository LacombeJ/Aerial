package ax.engine.mod.ray;

public class RayComponentHit {

    RayComponent component;
    RayHit hit;
    
    public RayComponentHit(RayComponent component, RayHit hit) {
        this.component = component;
        this.hit = hit;
    }
    
    public RayComponent component() {
        return component;
    }
    
    public RayHit hit() {
        return hit;
    }
    
}
