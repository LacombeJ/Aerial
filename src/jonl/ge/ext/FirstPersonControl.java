package jonl.ge.ext;

import jonl.jgl.Input;
import jonl.vmath.Mathf;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;

import jonl.ge.Property;
import jonl.ge.Transform;

public class FirstPersonControl extends Property {

    Input input;
    Transform transform;
    
    float resistance = 12;
    float speed = 0.1f; 
    
    Vector3 targForward = new Vector3(0,0,1);
    Vector3 forward = new Vector3(0,0,1);
    
    Vector3 targRight = new Vector3(1,0,0);
    Vector3 right = new Vector3(1,0,0);
    
    @Override
    public void create() {
        input = getInput();
        transform = getTransform();
    }

    @Override
    public void update() {
        
        forward = transform.rotation.get().conjugate().transform(targForward.get());
        right = transform.rotation.get().conjugate().transform(targRight.get());
        
        forward.y = 0;
        forward.norm();
        right.y = 0;
        right.norm();
        
        speed += 0.01f * input.getScrollY();
        if (speed<0.01f) {
            speed = 0.01f;
        }
        
        strafeRight(Mathf.toFloat(input.isKeyDown(Input.K_D))*speed);
        strafeLeft(Mathf.toFloat(input.isKeyDown(Input.K_A))*speed);
        
        moveForward(Mathf.toFloat(input.isKeyDown(Input.K_W))*speed);
        moveBackward(Mathf.toFloat(input.isKeyDown(Input.K_S))*speed);
        
        moveUp(Mathf.toFloat(input.isKeyDown(Input.K_E))*speed);
        moveDown(Mathf.toFloat(input.isKeyDown(Input.K_Q))*speed);
        
        lookHorizontal(input.getDX()/resistance);
        lookVertical(input.getDY()/resistance);
        

        
    }
    
    void moveForward(float z) {
        transform.translation.x -= forward.x*z;
        transform.translation.z -= forward.z*z;
    }
    
    void moveBackward(float z) {
        moveForward(-z);
    }
    
    void moveUp(float y) {
        transform.translation.y += y;
    }
    
    void moveDown(float y) {
        moveUp(-y);
    }
    
    void strafeRight(float x) {
        transform.translation.x += right.x*x;
        transform.translation.z += right.z*x;
    }
    
    void strafeLeft(float x) {
        strafeRight(-x);
    }
    
    void lookVertical(float yaw) {
        //TODO limit going over 90 or under -90
        //float currPitch = camera.getOrientation().getPitchDeg();
        //if (currPitch-pitch<90 && currPitch-pitch>-90) {
            Quaternion quat = new Quaternion().setEulerAnglesDeg(-yaw,0,0);
            transform.rotation.set(quat.mul(transform.rotation));
        //}
    }
    
    void lookHorizontal(float pitch) {
        Quaternion quat = new Quaternion().setEulerAnglesDeg(0,pitch,0);
        transform.rotation.mul(quat);
    }
    
}