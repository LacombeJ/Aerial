package jonl.ge.mod.ray;

import java.util.ArrayList;

import jonl.ge.core.Scene;

public class RayComponents {

    Scene scene;
    
    ArrayList<RayComponent> components = new ArrayList<>();
    
    public RayComponents(Scene scene) {
        this.scene = scene;
    }
    
    public RayComponentHit hit(Ray ray) {
        RayComponent comp = null;
        RayHit hit = null;
        for (RayComponent component : components) {
            RayHit rayHit = component.hit(ray);
            if (hit==null) {
                hit = rayHit;
                comp = component;
            } else if (rayHit!=null) {
                if (rayHit.distance()<hit.distance()) {
                    hit = rayHit;
                    comp = component;
                }
            }
        }
        if (hit==null) {
            return null;
        }
        return new RayComponentHit(comp,hit);
    }
    
}
