package ax.std.ray;

import ax.engine.core.Geometry;
import ax.engine.core.Mesh;
import ax.engine.core.Property;
import ax.engine.core.geometry.SphereGeometry;

public class RayComponent extends Property {

    RayIntersector intersector;
    
    boolean triangulated;
    
    public RayComponent(boolean triangulated) {
        this.triangulated = triangulated;
    }
    
    public RayComponent() {
        this(true);
    }
    
    @Override
    public void create() {
        Mesh mesh = getComponent(Mesh.class);
        if (mesh!=null) {
            Geometry geometry = mesh.getGeometry();
            if (triangulated) {
                intersector = new MeshIntersector(geometry);
            } else if (geometry instanceof SphereGeometry) {
                intersector = new SphereIntersector(geometry);
            } else {
                intersector = new MeshIntersector(geometry);
            }
        }
        RayComponents rayComponents = this.scene().data().getAsType("ray-components",RayComponents.class);
        if (rayComponents!=null) {
            rayComponents.components.add(this);
        }
    }

    @Override
    public void update() {
        
    }

    public RayHit hit(Ray ray) {
        if (intersector!=null) {
            return intersector.hit(ray,computeWorldMatrix());
        }
        return null;
    }
    
}
