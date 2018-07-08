package ax.bullet;

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

import ax.commons.func.Callback0D;
import ax.commons.func.Callback2D;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Transform;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;

class PhysicsWorld {
    
    Scene scene;
    
    DynamicsWorld world;
    Dispatcher dispatcher;
    CollisionConfiguration collisionConfig;
    BroadphaseInterface broadPhase;
    ConstraintSolver solver;
    
    Callback0D preUpdate;
    Callback2D<SceneObject,Transform> update;

    PhysicsWorld(Scene scene) {
        this.scene = scene;
        
        collisionConfig = new DefaultCollisionConfiguration();
        dispatcher = new CollisionDispatcher(collisionConfig);
        broadPhase = new DbvtBroadphase();
        solver = new SequentialImpulseConstraintSolver();
        
        world = new DiscreteDynamicsWorld(dispatcher, broadPhase, solver, collisionConfig);
        world.setGravity(new Vector3f(0f, -10f, 0f));
        
        preUpdate = () -> {
            world.stepSimulation(1/60f);
        };
        
        update = (sceneObject, worldParent) -> {
            update(sceneObject, worldParent);
        };
    }

    void add(com.bulletphysics.dynamics.RigidBody rb) {
        world.addRigidBody(rb);
    }
    
    void step() {
        world.stepSimulation(1/60f);
    }
    
    void update(SceneObject sceneObject, Transform worldParent) {
        
        ArrayList<RigidBody> rigidBodies = sceneObject.getComponents(RigidBody.class);
        for (RigidBody rigidBody : rigidBodies) {
            
            Vector3 translation = null;
            Quaternion rotation = null;
            
            //TODO make sure these get called in right order (called before or after update()?)
            if (rigidBody.kinematic) {
                Transform world = worldParent.get().multiply(sceneObject.transform());
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
    
}
