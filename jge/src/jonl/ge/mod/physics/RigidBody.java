package jonl.ge.mod.physics;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;

import jonl.ge.core.Property;
import jonl.ge.core.Transform;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;

public class RigidBody extends Property {

    PhysicsWorld world;
    
    protected com.bulletphysics.dynamics.RigidBody rb;
    
    public boolean hasInertia = true;
    public boolean kinematic = false;
    public float mass = 1;
    public boolean useGravity = true;
    Collider collider;
    
    public RigidBody(Collider collider) {
        this.collider = collider;
    }
    
    @Override
    public void create() {
        world = (PhysicsWorld) scene().data().get("physics-world");
        
        createRigidBody();
    }
    
    @Override
    public void update() {
        
    }
    
    private void createRigidBody() {
        
        Transform T = computeWorldTransform();
        
        Vector3 pos = T.translation;
        Quaternion rot = T.rotation;
        
        com.bulletphysics.linearmath.Transform t = new com.bulletphysics.linearmath.Transform();
        t.setIdentity();
        t.origin.set(pos.x, pos.y, pos.z);
        t.basis.set(new Quat4f(rot.x, rot.y, rot.z, rot.w));
        
        MotionState motion = new DefaultMotionState(t);
        
        RigidBodyConstructionInfo info = null;
        
        if (hasInertia) {
            Vector3f inertia = new Vector3f(0,0,0);
            if (mass != 0) {
                collider.shape.calculateLocalInertia(mass, inertia);
            }           
            info = new RigidBodyConstructionInfo(mass, motion, collider.shape, inertia);
        } else {
            info = new RigidBodyConstructionInfo(mass, motion, collider.shape);
        }
        
        rb = new com.bulletphysics.dynamics.RigidBody(info);
        
        int collisionFlags = 0;
        if (kinematic) collisionFlags |= CollisionFlags.KINEMATIC_OBJECT;
        if (collisionFlags != 0) {
            rb.setCollisionFlags(collisionFlags);
        }
        
        world.add(rb);
    }
    
    public Vector3 getLinearVelocity() {
        Vector3f v = new Vector3f();
        rb.getLinearVelocity(v);
        return new Vector3(v.x, v.y, v.z);
    }
    public void setLinearVelocity(Vector3 v) {
        rb.setLinearVelocity(new Vector3f(v.x, v.y, v.z));
    }
    
    public Vector3 getAngularVelocity() {
        Vector3f v = new Vector3f();
        rb.getAngularVelocity(v);
        return new Vector3(v.x, v.y, v.z);
    }
    public void setAngularVelocity(Vector3 v) {
        rb.setAngularVelocity(new Vector3f(v.x, v.y, v.z));
    }
    
    public void applyCentralImpulse(Vector3 impulse) {
        rb.applyCentralImpulse(new Vector3f(impulse.x, impulse.y, impulse.z));
    }

}
