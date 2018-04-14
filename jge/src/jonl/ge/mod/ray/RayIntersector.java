package jonl.ge.mod.ray;

import jonl.vmath.Matrix4;

public interface RayIntersector {
    
    public RayHit hit(Ray ray, Matrix4 model);
    
}
