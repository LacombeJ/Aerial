package ax.bullet;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;

public class BoxCollider extends Collider {

    public BoxCollider(float x, float y, float z) {
        shape = new BoxShape(new Vector3f(x,y,z));
    }
    
}
