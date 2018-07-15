package ax.std.misc;

import ax.engine.core.Component;
import ax.engine.core.Transform;
import ax.math.vector.Mathf;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;
import ax.std.Std;

public class CameraControl extends Component {
    
    /**
     * Looks at point using euler calculations for standard cameras without roll. Up vector = Vector3(0,1,0)
     * @param point
     */
    public void lookAt(Vector3 point) {
        Transform parentWorldTransform = parentWorldTransform();
        Transform selfWorldTransform = transform().multiply(parentWorldTransform);
        
        Vector3 selfPos = selfWorldTransform.translation;

        Vector3 dir = point.get().sub(selfPos);

        Quaternion quat = Std.rotationFromVector(dir);

        selfWorldTransform.rotation = quat;
        
        Transform updateTransform = selfWorldTransform.inverse(parentWorldTransform);
        
        transform().set(updateTransform);
    }
    
}
