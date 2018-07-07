package jonl.ge.mod.ray;

import jonl.ge.core.Geometry;
import jonl.ge.core.Mesh;
import jonl.ge.core.Property;
import jonl.ge.core.geometry.SphereGeometry;

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
