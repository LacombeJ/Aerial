package jonl.ge.mod.physics2d;

import org.jbox2d.collision.shapes.Shape;

import jonl.ge.core.Component;

public abstract class Collider2d extends Component {

    abstract Shape shape();
    
}
