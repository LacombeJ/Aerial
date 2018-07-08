package ax.std.ray;

import ax.engine.core.Geometry;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class MeshIntersector implements RayIntersector {

    private Triangle[] triangles;

    public MeshIntersector(Geometry geometry) {
        
        Vector3[] vertices = geometry.getVertexArray();
        int[] indices = geometry.getIndices();
        
        if (indices.length==0) {
            // Make triangles from every 3 vertices
            triangles = new Triangle[vertices.length/3];
            for (int i=0; i<triangles.length; i++) {
                triangles[i] = new Triangle(
                    vertices[i*3+0].get(),
                    vertices[i*3+1].get(),
                    vertices[i*3+2].get());
            }
        } else {
            // Make triangles from every 3 indices
            triangles = new Triangle[indices.length/3];
            for (int i=0; i<triangles.length; i++) {
                triangles[i] = new Triangle(
                    vertices[indices[i*3+0]].get(),
                    vertices[indices[i*3+1]].get(),
                    vertices[indices[i*3+2]].get());
            }
        }
        
    }
    
    @Override
    public RayHit hit(Ray ray, Matrix4 model) {
        RayHit hit = null;
        for (Triangle t : triangles) {
            Triangle tri = t.apply(model);
            RayHit rayHit = RayIntersection.triangle(ray,tri.a,tri.b,tri.c);
            if (hit==null) {
                hit = rayHit;
            } else if (rayHit!=null) {
                if (rayHit.distance()<hit.distance()) {
                    hit = rayHit;
                }
            }
        }
        return hit;
    }
    
    static class Triangle {
        Vector3 a;
        Vector3 b;
        Vector3 c;
        Triangle(Vector3 a, Vector3 b, Vector3 c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        Triangle apply(Matrix4 matrix) {
            Vector3 ax = matrix.multiply(new Vector4(a,1)).xyz();
            Vector3 bx = matrix.multiply(new Vector4(b,1)).xyz();
            Vector3 cx = matrix.multiply(new Vector4(c,1)).xyz();
            return new Triangle(ax,bx,cx);
        }
    }
    
}
