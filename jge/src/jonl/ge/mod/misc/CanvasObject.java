package jonl.ge.mod.misc;

import jonl.ge.core.Camera;
import jonl.ge.core.SceneObject;
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

    private SceneObject root;
    private Camera camera;
    
    public CanvasObject() {
        
        root = new SceneObject();
        
        SceneObject canvas = new SceneObject();
        
        camera = new Camera();
        camera.setOrder(10);
        camera.setScissorMode(Camera.NONE);
        camera.enableClearColor(false);
        camera.setTargetType(Camera.ONLY);
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
    
    public SceneObject get() {
        return root;
    }
    
    public void add(SceneObject so) {
        CameraCull cull = new CameraCull(camera, Target.ONLY);
        so.addComponent(cull);
        camera.addTargets(so);
    }
    
    public void addChild(SceneObject so) {
        add(so);
        root.addChild(so);
    }
    
}
