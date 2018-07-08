package ax.box;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;

import ax.engine.core.Component;

public class RigidBody2d extends Component {

    FixtureDef fixtureDef = new FixtureDef();
    Body body = null;
    Body2dType type = Body2dType.STATIC;
    
    public void setDensity(float density) {
        fixtureDef.setDensity(density);
    }
    
    public void setFriction(float friction) {
        fixtureDef.setFriction(friction);
    }
    
    public void setBodyType(Body2dType type) {
        this.type = type;
    }
    
}
