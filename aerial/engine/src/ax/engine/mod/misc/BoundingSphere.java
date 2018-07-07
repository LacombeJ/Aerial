package ax.engine.mod.misc;

import ax.engine.core.Geometry;
import ax.engine.core.Mesh;
import ax.engine.core.Property;
import ax.math.vector.Mathf;
import ax.math.vector.Vector3;

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
