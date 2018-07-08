package ax.examples.math;

import ax.commons.io.Console;
import ax.math.vector.Mathf;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class MathMatrixMain {

    public static void main(String[]args) {
        new MathMatrixMain().run();
    }

    void run() {

        test(0f, 0f, 0f);
        test(0.1f, 0f, 0.1f);
        test(3.14f, 0.707f, 0f);
        test(1f, 2f, 3f);
        test(-1f, 0f, 1f);

        Console.log("=============");

        testAlt(0f, 0f, 0f);
        testAlt(0.1f, 0f, 0.1f);
        testAlt(3.14f, 0.707f, 0f);
        testAlt(1f, 2f, 3f);
        testAlt(-1f, 0f, 1f);

    }

    void test(float x, float y, float z) {

        Quaternion quat = Quaternion.eulerRad(x,y,z);

        Vector3 e = Quaternion.asEulerRad(quat);

        Console.log("Input:",x,y,z,"   Output:",e.x,e.y,e.z);

    }

    void testAlt(float x, float y, float z) {

        Vector4 quat = toQuaternion(x,y,z);

        Vector3 e = toEulerAngle(quat);

        Console.log("Input:",x,y,z,"   Output:",e.x,e.y,e.z);

    }

    // https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles

    Vector4 toQuaternion(float yaw, float pitch, float roll) {
        Vector4 q = new Vector4();

        float cy = Mathf.cos(yaw * 0.5f);
        float sy = Mathf.sin(yaw * 0.5f);
        float cr = Mathf.cos(roll * 0.5f);
        float sr = Mathf.sin(roll * 0.5f);
        float cp = Mathf.cos(pitch * 0.5f);
        float sp = Mathf.sin(pitch * 0.5f);

        q.w = cy * cr * cp + sy * sr * sp;
        q.x = cy * sr * cp - sy * cr * sp;
        q.y = cy * cr * sp + sy * sr * cp;
        q.z = sy * cr * cp - cy * sr * sp;

        return q;
    }

    Vector3 toEulerAngle(Vector4 q) {
        float roll;
        float pitch;
        float yaw;

        // roll (x-axis rotation)
        float sinr = +2.0f * (q.w * q.x + q.y * q.z);
        float cosr = +1.0f - 2.0f * (q.x * q.x + q.y * q.y);
        roll = Mathf.atan2(sinr, cosr);

        // pitch (y-axis rotation)
        float sinp = +2.0f * (q.w * q.y - q.z * q.x);
        if (Mathf.abs(sinp) >= 1)
            pitch = Mathf.copySign(Mathf.PI / 2, sinp); // use 90 degrees if out of range
        else
            pitch = Mathf.asin(sinp);

        // yaw (z-axis rotation)
        float siny = +2.0f * (q.w * q.z + q.x * q.y);
        float cosy = +1.0f - 2.0f * (q.y * q.y + q.z * q.z);
        yaw = Mathf.atan2(siny, cosy);

        return new Vector3(yaw, pitch, roll);
    }

}
