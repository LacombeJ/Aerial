package ax.std.fx;

import ax.commons.func.Callback;
import ax.commons.func.Callback2D;
import ax.commons.func.Callback3D;
import ax.engine.core.Attachment;
import ax.engine.core.Camera;
import ax.engine.core.Delegate;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Service;
import ax.graphics.GL;

/**
 * Visual-effects module
 * 
 * @author Jonathan
 *
 */
public class FXModule extends Attachment {
    
    private Service service;
    private Callback<Scene> onSceneUpdate;
    private Callback2D<SceneObject,Camera> onSceneObjectRender;
    private Callback3D<SceneObject,Mesh,GL> onGLPreRender;
    private Callback3D<SceneObject,Mesh,GL> onGLPostRender;
    
    private FXService[] fxServices = {
        new DepthSort()
    };
    
    public FXModule() {
        super("fx-module");
        
        onSceneUpdate = (s) -> onSceneUpdate(s);
        onSceneObjectRender = (g,camera) -> onSceneObjectRender(g,camera);
        onGLPreRender = (g,mesh,gl) -> onGLPreRender(g,mesh,gl);
        onGLPostRender = (g,mesh,gl) -> onGLPostRender(g,mesh,gl);
    }
    
    private void onSceneUpdate(Scene s) {
        for (FXService fx : fxServices) {
            fx.prepare(s, service);
        }
    }
    
    private void onSceneObjectRender(SceneObject g, Camera camera) {
        for (FXService fx : fxServices) {
            fx.update(g, camera, service);
        }
    }
    
    private void onGLPreRender(SceneObject g, Mesh mesh, GL gl) {
        for (FXService fx : fxServices) {
            fx.begin(g, mesh, gl, service);
        }
    }
    
    private void onGLPostRender(SceneObject g, Mesh mesh, GL gl) {
        for (FXService fx : fxServices) {
            fx.end(g, mesh, gl, service);
        }
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onSceneUpdate().add(onSceneUpdate);
        delegate.onSceneObjectRender().add(onSceneObjectRender);
        delegate.onGLPreRender().add(onGLPreRender);
        delegate.onGLPostRender().add(onGLPostRender);
        this.service = service;
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onSceneUpdate().remove(onSceneUpdate);
        delegate.onSceneObjectRender().remove(onSceneObjectRender);
        delegate.onGLPreRender().remove(onGLPreRender);
        delegate.onGLPostRender().remove(onGLPostRender);
    }
    
}
