package ax.editor;

import ax.engine.core.Input;
import ax.engine.core.Property;
import ax.engine.core.SceneObject;
import ax.engine.core.Transform;
import ax.engine.core.Input.CursorState;
import ax.std.render.Light;
import ax.math.vector.Mathf;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;

public class EditorAssets {

    /* Mesh Assets */
	public static SceneObject control() {
        SceneObject control = new SceneObject();
        control.setName("EditorControl");
        
        EditorControl ec = new EditorControl();
        MouseGrab mg = new MouseGrab();
        
        control.addComponent(ec);
        control.addComponent(mg);
        
        Light pl = new Light();
        control.addComponent(pl);
        
        return control;
    }
	
	
    
    
	public static class EditorControl extends Property {
        
        EditorInput input;
        Transform transform;
        
        float resistance = 12;
        float speed = 0.1f; 
        
        Vector3 targForward = new Vector3(0,0,1);
        Vector3 forward = new Vector3(0,0,1);
        
        Vector3 targRight = new Vector3(1,0,0);
        Vector3 right = new Vector3(1,0,0);
        
        boolean inDragState = false;
        
        @Override
        public void create() {
            input = (EditorInput) input();
            transform = transform();
        }
        
        @Override
        public void update() {
            if (input.isButtonPressed(Input.MB_RIGHT)) {
                inDragState = true;
            }
            if (inDragState && input.glIsButtonDown(Input.MB_RIGHT)) {
                handleFirstPersonControl();
            } else {
                inDragState = false;
            }
        }
        
        private void handleFirstPersonControl() {
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
        
        private void moveForward(float z) {
            transform.translation.x -= forward.x*z;
            transform.translation.z -= forward.z*z;
        }
        
        private void moveBackward(float z) {
            moveForward(-z);
        }
        
        private void moveUp(float y) {
            transform.translation.y += y;
        }
        
        private void moveDown(float y) {
            moveUp(-y);
        }
        
        private void strafeRight(float x) {
            transform.translation.x += right.x*x;
            transform.translation.z += right.z*x;
        }
        
        private void strafeLeft(float x) {
            strafeRight(-x);
        }
        
        private void lookVertical(float yaw) {
            //TODO limit going over 90 or under -90
            //float currPitch = camera.getOrientation().getPitchDeg();
            //if (currPitch-pitch<90 && currPitch-pitch>-90) {
                Quaternion quat = new Quaternion().setEulerAnglesDeg(-yaw,0,0);
                transform.rotation.set(quat.mul(transform.rotation));
            //}
        }
        
        private void lookHorizontal(float pitch) {
            Quaternion quat = new Quaternion().setEulerAnglesDeg(0,pitch,0);
            transform.rotation.mul(quat);
        }
        
    }
    
    
	public static class MouseGrab extends Property {

        boolean open = false;
        boolean close = false;
        
        MouseGrab() {
            
        }
        
        @Override
        public void create() { }
        
        @Override
        public void update() {
            //TODO handle open/close and integration with TUI
            //Using these open and close variables to call the sw._Input()
            //methods after the cursor moves back after the grab state as it
            //might affect the input then
            
            EditorInput i = (EditorInput) input();
            if (i.isButtonPressed(Input.MB_RIGHT)) {
                if (getCursorState()==CursorState.NORMAL) {
                    setCursorState(CursorState.GRABBED);
                }
            }
            if (!i.glIsButtonDown(Input.MB_RIGHT)) {
                if (getCursorState()==CursorState.GRABBED) {
                    setCursorState(CursorState.NORMAL);
                }
            }
            
        }
    }
    
    
}
