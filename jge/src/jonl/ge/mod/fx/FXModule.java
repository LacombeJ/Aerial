package jonl.ge.mod.fx;

import jonl.ge.core.Attachment;
import jonl.ge.core.Camera;
import jonl.ge.core.Delegate;
import jonl.ge.core.GameObject;
import jonl.ge.core.Mesh;
import jonl.ge.core.Service;
import jonl.jgl.GraphicsLibrary;
import jonl.jutils.func.Callback2D;
import jonl.jutils.func.Callback3D;

/**
 * Visual-effects module
 * 
 * @author Jonathan
 *
 */
public class FXModule extends Attachment {
    
    private Service service;
    private Callback2D<GameObject,Camera> onGameObjectRender;
    private Callback3D<GameObject,Mesh,GraphicsLibrary> onGLPreRender;
    private Callback3D<GameObject,Mesh,GraphicsLibrary> onGLPostRender;
    
    private FXService[] fxServices = {
        new DepthSort()
    };
    
    public FXModule() {
        super("fx-module");
        
        onGameObjectRender = (g,camera) -> { onGameObjectRender(g,camera); };
        onGLPreRender = (g,mesh,gl) -> { onGLPreRender(g,mesh,gl); };
        onGLPostRender = (g,mesh,gl) -> { onGLPostRender(g,mesh,gl); };
    }
    
    private void onGameObjectRender(GameObject g, Camera camera) {
        for (FXService fx : fxServices) {
            fx.update(g, camera, service);
        }
    }
    
    private void onGLPreRender(GameObject g, Mesh mesh, GraphicsLibrary gl) {
        for (FXService fx : fxServices) {
            fx.begin(g, mesh, gl, service);
        }
    }
    
    private void onGLPostRender(GameObject g, Mesh mesh, GraphicsLibrary gl) {
        for (FXService fx : fxServices) {
            fx.end(g, mesh, gl, service);
        }
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onGameObjectRender().add(onGameObjectRender);
        delegate.onGLPreRender().add(onGLPreRender);
        delegate.onGLPostRender().add(onGLPostRender);
        this.service = service;
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onGameObjectRender().remove(onGameObjectRender);
        delegate.onGLPreRender().remove(onGLPreRender);
        delegate.onGLPostRender().remove(onGLPostRender);
    }
    
}
