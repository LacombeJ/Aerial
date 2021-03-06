package ax.box;

import java.util.ArrayList;

import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Transform;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;

public class Physics2dWorld {

    int velocityIterations = 8;
    int positionIterations = 3;
    
    World world = new World(new Vec2(0.0f, -10.0f));
    float timeStep = 1.0f / 30.0f; //TODO should be 1/60 but this is slower than expected, find out why and fix
    
    Physics2dWorld(Scene scene) {
        
    }
    
    void step() {
        world.step(timeStep, velocityIterations, positionIterations);
    }
    
    void updateScene(Scene scene) {
        ArrayList<SceneObject> sceneObjects = scene.getAllSceneObjects();
        
        for (SceneObject so : sceneObjects) {
            RigidBody2d rb2d = so.getComponentOfType(RigidBody2d.class);
            Collider2d c2d = so.getComponentOfType(Collider2d.class);
            if (rb2d!=null && c2d!=null) {
                if (rb2d.body==null) {
                    Transform transform = rb2d.computeWorldTransform();
                    Vector3 euler = Quaternion.asEulerRad(transform.rotation);

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = Util.type(rb2d.type);
                    bodyDef.position.set(transform.translation.x, transform.translation.y);
                    bodyDef.angle = euler.z;
                    Body body = world.createBody(bodyDef);
                    
                    rb2d.fixtureDef.shape = c2d.shape();
                    
                    body.createFixture(rb2d.fixtureDef);
                    
                    rb2d.body = body;
                }
            }
        }
        
    }
    
    void update(SceneObject so, Transform transform) {
        
        RigidBody2d rb2d = so.getComponentOfType(RigidBody2d.class);
        Collider2d c2d = so.getComponentOfType(Collider2d.class);
        if (rb2d!=null && c2d!=null) {
            if (rb2d.body!=null) {
                Vec2 position = rb2d.body.getPosition();
                float angle = rb2d.body.getAngle();
                
                //TODO make transform consider world transform using parentTransform
                
                so.transform().translation.x = position.x;
                so.transform().translation.y = position.y;
                
                so.transform().rotation = Matrix4.rotationZ(angle).getRotation();
            }
        }
    }
    
    public void setGravity(Vector2 gravity) {
        world.setGravity(Util.vec(gravity));
    }
    
    public void setTimeStep(float dt) {
        timeStep = dt;
    }
    
    public void setVelocityIterations(int iter) {
        velocityIterations = iter;
    }
    
    public void setPositionIterations(int iter) {
        positionIterations = iter;
    }
    
}
