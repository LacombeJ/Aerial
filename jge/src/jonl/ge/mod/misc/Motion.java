package jonl.ge.mod.misc;

import jonl.ge.core.Property;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;

/**
 * Motion property for moving objects by setting linear and angular velocity
 * 
 * @author Jonathan
 *
 */
public class Motion extends Property {

    private Vector3 linear = new Vector3(0,0,0);
    
    private Vector3 angular = new Vector3(0,0,0);
    
    private float SPF = 1/60f;
    
    @Override
    public void create() {
        
    }

    @Override
    public void update() {
        Vector3 dPosition = linear.get().scale(SPF);
        Vector3 dRotation = angular.get().scale(SPF);
        Quaternion rot = Quaternion.euler(dRotation.x, dRotation.y, dRotation.z);
        transform().rotation.mul(rot);
        transform().translation.add(dPosition);
    }
    
    public Vector3 getLinearVelocity() {
        return linear.get();
    }
    
    public void setLinearVelocity(Vector3 linear) {
        this.linear.set(linear);
    }
    
    public Vector3 getAngularVelocity() {
        return angular.get();
    }
    
    public void setAngularVelocity(Vector3 angular) {
        this.angular.set(angular);
    }

    
    
}
