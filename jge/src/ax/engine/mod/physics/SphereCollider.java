package ax.engine.mod.physics;

import com.bulletphysics.collision.shapes.SphereShape;

public class SphereCollider extends Collider {

    public SphereCollider(float radius) {
        shape = new SphereShape(radius);
    }
    
}
