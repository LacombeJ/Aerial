package jonl.ge.mod.render;

import jonl.ge.core.Attachment;
import jonl.ge.core.Camera;
import jonl.ge.core.Delegate;
import jonl.ge.core.Service;
import jonl.ge.core.Window;
import jonl.jutils.func.Callback;

/**
 * Module for advanced rendering
 * 
 * @author Jonathan
 *
 */
public class RenderModule extends Attachment {

    Callback<Camera> postRenderCamera;
    
    public RenderModule() {
        super("render-module");
        
        postRenderCamera = (camera) -> {
            render(camera);
        };
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onPostRenderCamera().add(postRenderCamera);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onPostRenderCamera().remove(postRenderCamera);
    }
    
    private void render(Camera camera) {
        if (camera instanceof PostCamera) {
            PostCamera pc = (PostCamera)camera;
            
            camera.service().renderDirect(pc,pc.material,null);
            
            Window window = pc.window();
            int width = window.width();
            int height = window.height();
            if (width!=pc.buffer().width() || height!=pc.buffer().height()) {
                pc.resize(width,height);
            }
        }
    }
    
}

