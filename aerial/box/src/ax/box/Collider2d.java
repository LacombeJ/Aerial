package ax.box;

import org.jbox2d.collision.shapes.Shape;

import ax.engine.core.Component;

public abstract class Collider2d extends Component {

    abstract Shape shape();
    
}
