package jonl.ge.ext;

import jonl.jgl.Input;
import jonl.vmath.Quaternion;
import jonl.vmath.Vector3;

import jonl.ge.Property;
import jonl.ge.Transform;

public class OrbitControl extends Property {

    Input input;
    Transform transform;
    
    float rotSpeed = 1f;
    
    float distance = 10;
    Vector3 iPos = new Vector3(0,0,1);
    Quaternion iRot = new Quaternion();
    
    boolean mouseDown;
    float dragx;
    float dragy;
    
    @Override
    public void create() {
        input = getInput();
        transform = getTransform();
        transform.translation.set(iPos.get().multiply(distance));
        transform.rotation.set(iRot);
    }

    private void rotate(float x, float y) {
        Quaternion tickx = new Quaternion().setEulerAnglesDeg(rotSpeed*x,0,0);
        Quaternion ticky = new Quaternion().setEulerAnglesDeg(0,-rotSpeed*y,0);
        
        transform.rotation.set(tickx.get().mul(transform.rotation));
        transform.rotation.set(ticky.get().mul(transform.rotation));
        
        Vector3 camPos = transform.rotation.get().conjugate().transform(iPos.get()).multiply(distance);
        transform.translation.set(camPos);
    }
    
    @Override
    public void update() {
        
        if (input.isButtonDown(Input.MB_LEFT)) {
            float dx = input.getDX();
            float dy = input.getDY();
            
            rotate(dx,dy);
            
            mouseDown = true;
        } else if (input.isButtonReleased(Input.MB_LEFT)) {
            if (mouseDown) {
                dragx = input.getDX();
                dragy = input.getDY();
                
                rotate(dragx,dragy);
                mouseDown = false;
            }
        } else {
            mouseDown = false;
            if (dragx>0 || dragy>0) {
                rotate(dragx,dragy);
                dragx-=0.3f;
                dragy-=0.3f;
                if (dragx<0) dragx = 0;
                if (dragy<0) dragy = 0;
            } else {
                
            }
        }
        
        float f = input.getScrollY();
        if (f!=0) {
            distance -= f*Math.sqrt(distance)/10;
            if (distance<0.1f) {
                distance = 0.1f;
            }
            transform.translation.norm().scale(distance);
        }
        
        
    }
    
}