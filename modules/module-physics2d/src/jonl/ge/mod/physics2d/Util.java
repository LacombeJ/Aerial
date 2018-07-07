package jonl.ge.mod.physics2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import jonl.vmath.Vector2;

class Util {

    static Vec2 vec(Vector2 v) {
        return new Vec2(v.x, v.y);
    }
    
    static Vector2 vec(Vec2 v) {
        return new Vector2(v.x, v.y);
    }
    
    static BodyType type(Body2dType type) {
        switch(type) {
        case STATIC:    return BodyType.STATIC;
        case KINEMATIC: return BodyType.KINEMATIC;
        case DYNAMIC:   return BodyType.DYNAMIC;
        }
        return BodyType.STATIC;
    }
    
    static Body2dType type(BodyType type) {
        switch(type) {
        case STATIC:    return Body2dType.STATIC;
        case KINEMATIC: return Body2dType.KINEMATIC;
        case DYNAMIC:   return Body2dType.DYNAMIC;
        }
        return Body2dType.STATIC;
    }

}
