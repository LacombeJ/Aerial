package ax.box;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public class BoxCollider2d extends Collider2d {

    float width = 0.0f;
    float height = 0.0f;
    PolygonShape shape;
    
    public BoxCollider2d(float width, float height) {
        this.width = width;
        this.height = height;
        shape = new PolygonShape();
        shape.setAsBox(width*0.5f, height*0.5f);
    }
    
    Shape shape() {
        return shape;
    }
    
}
