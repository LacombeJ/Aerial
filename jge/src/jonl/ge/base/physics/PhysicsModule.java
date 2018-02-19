package jonl.ge.base.physics;

import java.util.ArrayList;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import jonl.ge.core.Attachment;
import jonl.ge.core.Delegate;
import jonl.ge.core.GameObject;
import jonl.ge.core.Service;
import jonl.ge.core.Transform;
import jonl.ge.core.physics.RigidBody;
import jonl.jutils.func.Callback0D;
import jonl.jutils.func.Callback2D;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;

public class PhysicsModule extends Attachment {

    private static PhysicsModule instance;
    
    public static PhysicsModule instance() {
        if (instance == null) {
            instance = new PhysicsModule();
        }
        return instance;
    }
    
    DynamicsWorld world;
    Dispatcher dispatcher;
    CollisionConfiguration collisionConfig;
    BroadphaseInterface broadPhase;
    ConstraintSolver solver;
    
    Callback0D preUpdate;
    Callback2D<GameObject,Transform> update;
    
    private PhysicsModule() {
        super("physics-module");
        
        collisionConfig = new DefaultCollisionConfiguration();
        dispatcher = new CollisionDispatcher(collisionConfig);
        broadPhase = new DbvtBroadphase();
        solver = new SequentialImpulseConstraintSolver();
        
        world = new DiscreteDynamicsWorld(dispatcher, broadPhase, solver, collisionConfig);
        world.setGravity(new Vector3f(0f, -10f, 0f));
        
        preUpdate = () -> {
            world.stepSimulation(1/60f);
        };
        
        update = (gameObject, worldParent) -> {
            update(gameObject, worldParent);
        };
    }
    
    public void add(com.bulletphysics.dynamics.RigidBody rb) {
        world.addRigidBody(rb);
    }
    
    private void update(GameObject gameObject, Transform worldParent) {
        
        ArrayList<RigidBody> rigidBodies = gameObject.getComponents(RigidBody.class);
        for (RigidBody rigidBody : rigidBodies) {
            
            Vector3 translation = null;
            Quaternion rotation = null;
            
            //TODO make sure these get called in right order (called before or after update()?)
            if (rigidBody.kinematic) {
                Transform world = worldParent.get().multiply(gameObject.transform());
                translation = world.translation;
                rotation = world.rotation;
                
                com.bulletphysics.linearmath.Transform t = new com.bulletphysics.linearmath.Transform();
                t.origin.set(translation.x, translation.y, translation.z);
                t.setRotation(new Quat4f(rotation.x, rotation.y, rotation.z, rotation.w));
                rigidBody.rb.setWorldTransform(t);
                
            } else {
                com.bulletphysics.linearmath.Transform t = new com.bulletphysics.linearmath.Transform();
                rigidBody.rb.getWorldTransform(t);
                
                translation = new Vector3(t.origin.x, t.origin.y, t.origin.z);
                Quat4f basis = new Quat4f();
                t.getRotation(basis);
                rotation = new Quaternion(basis.x, basis.y, basis.z, basis.w);
            }
            
            Transform rbWorldTransform = new Transform(new Vector3(1,1,1), rotation, translation);
            
            Transform relative = rbWorldTransform.inverse(worldParent);
            
            rigidBody.transform().translation.set(relative.translation);
            rigidBody.transform().rotation.set(relative.rotation);
            
        }
        
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onUpdate().add(preUpdate);
        delegate.onParentWorldTransformUpdate().add(update);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onUpdate().remove(preUpdate);
        delegate.onParentWorldTransformUpdate().remove(update);
    }
    
}
