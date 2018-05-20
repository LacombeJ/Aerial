package jonl.ge.mod.fx;

import jonl.ge.core.Attachment;
import jonl.ge.core.Camera;
import jonl.ge.core.Delegate;
import jonl.ge.core.SceneObject;
import jonl.ge.core.Mesh;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.jgl.GL;
import jonl.jutils.func.Callback;
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
