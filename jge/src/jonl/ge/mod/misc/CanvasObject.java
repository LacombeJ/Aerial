package jonl.ge.mod.misc;

import jonl.ge.core.Camera;
import jonl.ge.core.GameObject;
import jonl.ge.core.Window;
import jonl.ge.core.render.CameraCull;
import jonl.ge.core.render.CameraCull.Target;

/**
 * Class used to render things to screen space which are occluded in other
 * camera views
 * <p>
 * Add this to the root of the scenegraph with {@link #get()}
 * @author Jonathan
 *
 */
public class CanvasObject {

    private GameObject root;
    private Camera camera;
    
    public CanvasObject() {
        
        root = new GameObject();
        
        GameObject canvas = new GameObject();
        
        camera = new Camera();
        camera.setOrder(10);
        camera.setScissor(false);
        camera.enableClearColor(false);
        camera.setTargetType(Camera.Target.ONLY);
        camera.setOrthographicBox(576, 1024, -1, 1);
        
        canvas.addComponent(camera);
        canvas.transform().translation.set(512,288,0);
        
        canvas.addUpdate(()->{
            Window window = canvas.window();
            camera.setOrthographicBox(window.height(), window.width(), -1, 1);
            camera.transform().translation.set(window.width()/2, window.height()/2, 0);
        });
        
        root.addChild(canvas);
        
    }
    
    public GameObject get() {
        return root;
    }
    
    public void add(GameObject go) {
        CameraCull cull = new CameraCull(camera, Target.ONLY);
        go.addComponent(cull);
        camera.addTargets(go);
    }
    
    public void addChild(GameObject go) {
        add(go);
        root.addChild(go);
    }
    
}
