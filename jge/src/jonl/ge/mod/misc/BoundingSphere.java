package jonl.ge.mod.misc;

import jonl.ge.core.Geometry;
import jonl.ge.core.Mesh;
import jonl.ge.core.Property;
import jonl.vmath.Mathf;
import jonl.vmath.Vector3;

public class BoundingSphere extends Property {

    float radius;
    
    @Override
    public void create() {
        compute();
    }

    @Override
    public void update() {
        
    }
    
    public float radius() {
        return radius;
    }
    
    private void compute() {
        Mesh mesh = getComponent(Mesh.class);
        if (mesh != null) {
            Geometry geometry = mesh.getGeometry();
            radius = 0;
            for (Vector3 v : geometry.getVertexArray()) {
                radius = Mathf.max(radius, v.mag());
            }
        }
    }
 
}
