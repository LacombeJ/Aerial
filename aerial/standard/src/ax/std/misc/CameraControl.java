package ax.std.misc;

import ax.engine.core.Component;
import ax.engine.core.Transform;
import ax.math.vector.Mathf;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;

public class CameraControl extends Component {
    
    /**
     * Looks at point using euler calculations for standard cameras without roll. Up vector = Vector3(0,1,0)
     * @param point
     */
    public void lookAt(Vector3 point) {
        Transform parentWorldTransform = parentWorldTransform();
        Transform selfWorldTransform = transform().multiply(parentWorldTransform);
        
        Vector3 selfPos = selfWorldTransform.translation;
        
        float theta = Vector3.thetaBetween(selfPos, point);
        float phi = Vector3.phiBetween(selfPos, point);
        
        Quaternion quat = new Quaternion();
        quat.mul( Quaternion.eulerRad(theta, 0, 0).conjugate().nor() );
        quat.mul( Quaternion.eulerRad(0, phi+Mathf.PI_OVER_2, 0) );
        
        selfWorldTransform.rotation = quat;
        
        Transform updateTransform = selfWorldTransform.inverse(parentWorldTransform);
        
        transform().set(updateTransform);
    }
    
}