package ax.std.ray;

import ax.math.vector.Matrix4;

public interface RayIntersector {
    
    public RayHit hit(Ray ray, Matrix4 model);
    
}
