package jonl.ge.base.physics;

import jonl.ge.core.Property;

public abstract class BaseRigidBody extends Property {
    
    protected com.bulletphysics.dynamics.RigidBody rb;
    
    protected abstract void createRigidBody();
    
    @Override
    public void create() {
        createRigidBody();
    }
    
    @Override
    public void update() {
        
    }
    
}
