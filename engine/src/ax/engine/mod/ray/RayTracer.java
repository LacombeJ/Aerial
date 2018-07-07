package ax.engine.mod.ray;

import ax.math.vector.Vector3;

public class RayTracer {

    private Ray ray;
    
    public RayTracer(Ray ray) {
        this.ray = ray;
    }
    
    public Ray ray() {
        return ray;
    }
    
    public RayHit sphere(Vector3 center, float radius) {
        return RayIntersection.sphere(ray,center,radius);
    }
    
    public RayHit triangle(Vector3 a, Vector3 b, Vector3 c) {
        return RayIntersection.triangle(ray,a,b,c);
    }
    
    // http://www.siggraph.org/education/materials/HyperGraph/raytrace/rtinter3.htm
    public RayHit boundingBox(Vector3 min, Vector3 max) {
        return RayIntersection.boundingBox(ray,min,max);
    }
    
    // https://www.siggraph.org//education/materials/HyperGraph/raytrace/rayplane_intersection.htm
    public RayHit plane(Vector3 unitNormal, float dist) {
        return RayIntersection.plane(ray,unitNormal,dist);
    }
    
    
    
}
