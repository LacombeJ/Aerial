package jonl.ge.mod.ray;

import jonl.ge.core.Geometry;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

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
