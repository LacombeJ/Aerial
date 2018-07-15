package ax.std.misc;

import ax.engine.core.Camera;
import ax.engine.core.SceneObject;
import ax.engine.core.Window;
import ax.engine.core.render.CameraCull;
import ax.engine.core.render.CameraCull.Target;

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
    private int width;
    private int height;
    
    public CanvasObject() {

        width = 1024;
        height = 576;

        root = new SceneObject();
        
        SceneObject canvas = new SceneObject();
        
        camera = new Camera();
        camera.setOrder(10);
        camera.setScissorMode(Camera.NONE);
        camera.enableClearColor(false);
        camera.setTargetType(Camera.ONLY);
        camera.setOrthographicBox(height, width, -1, 1);
        
        canvas.addComponent(camera);
        canvas.transform().translation.set(512,288,0);
        
        canvas.addUpdate(()->{
            Window window = canvas.window();
            width = window.width();
            height = window.height();
            camera.setOrthographicBox(height, width, -1, 1);
            camera.transform().translation.set(width/2, height/2, 0);
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

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
    
}
