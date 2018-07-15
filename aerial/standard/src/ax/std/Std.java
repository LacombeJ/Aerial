package ax.std;

import ax.math.vector.Mathf;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;

public class Std {

    public static Quaternion rotationFromVector(Vector3 v) {

        float theta = v.theta();
        float phi = v.phi();

        Quaternion quat = new Quaternion();
        quat.mul( Quaternion.eulerRad(0, 0, theta).conjugate().nor() );
        quat.mul( Quaternion.eulerRad(0, phi+Mathf.PI_OVER_2, 0) );

        return quat;

    }

}
