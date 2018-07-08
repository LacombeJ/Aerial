package ax.std.ray;

import ax.engine.core.Geometry;
import ax.math.vector.Mathf;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class SphereIntersector implements RayIntersector {

    public Vector3 center = new Vector3(0,0,0);
    
    public float radius = 0;

    public SphereIntersector(Geometry geometry) {
        
        Vector3[] vertices = geometry.getVertexArray();
        
        Vector3 sum = new Vector3();
        for (Vector3 v : vertices) {
            sum.add(v);
        }
        
        center = sum.divide(geometry.getNumVertices());
        
        for (Vector3 v : vertices) {
            radius = Mathf.max(radius, v.mag());
        }
        
    }
    
    @Override
    public RayHit hit(Ray ray, Matrix4 model) {
        return RayIntersection.sphere(ray,model.multiply(new Vector4(center,1)).xyz(),radius);
    }
    
}
