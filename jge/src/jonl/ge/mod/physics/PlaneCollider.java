package jonl.ge.mod.physics;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.StaticPlaneShape;

public class PlaneCollider extends Collider {

    public PlaneCollider() {
        shape = new StaticPlaneShape(new Vector3f(0f, 1f, 0f), 0f);
    }
    
}
