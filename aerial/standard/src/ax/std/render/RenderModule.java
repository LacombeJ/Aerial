package ax.std.render;

import java.util.List;

import ax.commons.func.Callback;
import ax.commons.func.Callback2D;
import ax.commons.func.Callback3D;
import ax.engine.core.Attachment;
import ax.engine.core.Camera;
import ax.engine.core.Delegate;
import ax.engine.core.Material;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Service;
import ax.engine.core.TextureUniform;
import ax.engine.core.Window;
import ax.graphics.Program;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;
import ax.std.render.RenderPass.Pass;

/**
 * Module for advanced rendering
 * 
 * @author Jonathan
 *
 */
public class RenderModule extends Attachment {

    Callback<Scene> onSceneUpdate;
    Callback<Camera> preRenderCamera;
    Callback<Camera> postRenderCamera;
    Callback3D<Program, Material, Camera> setUniforms;
    Callback2D<SceneObject,Camera> onSceneObjectRender;
    
    Service service;
    
    List<Light> lights;
    
    Vector4 clearColor = new Vector4(0,0,0,1);
    
    public RenderModule() {
        super("render-module");
        
        onSceneUpdate = (scene) -> {
            onSceneUpdate(scene);
        };
        
        preRenderCamera = (camera) -> {
            preRender(camera);
        };
        
        postRenderCamera = (camera) -> {
            render(camera);
        };
        
        onSceneObjectRender = (so,camera) -> {
            onSceneObjectRender(so,camera);
        };
        
        setUniforms = (program, material, camera) -> {
            setUniforms(material,camera);
        };
    }

    @Override
    public void add(Delegate delegate, Service service) {
        this.service = service;
        delegate.onPreRenderCamera().add(preRenderCamera);
        delegate.onPostRenderCamera().add(postRenderCamera);
        delegate.onSceneUpdate().add(onSceneUpdate);
        delegate.onMaterialUpdate().add(setUniforms);
        delegate.onSceneObjectRender().add(onSceneObjectRender);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onPreRenderCamera().remove(preRenderCamera);
        delegate.onPostRenderCamera().remove(postRenderCamera);
        delegate.onSceneUpdate().remove(onSceneUpdate);
        delegate.onMaterialUpdate().remove(setUniforms);
        delegate.onSceneObjectRender().remove(onSceneObjectRender);
    }
    
    private void preRender(Camera camera) {
        if (camera instanceof DeferredCamera) {
            DeferredCamera pc = (DeferredCamera)camera;
            
            clearColor = pc.getClearColor();
            pc.setClearColor(new Vector4(0,0,0,1));
        }
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
        } else if (camera instanceof DeferredCamera) {
            DeferredCamera pc = (DeferredCamera)camera;
            
            pc.setClearColor(clearColor);
            
            if (pc.renderPass==null) {
            
                if (pc.material instanceof DeferredMaterial) {
                    pc.material.setUniform("gPosition",new TextureUniform(pc.position(),0));
                    pc.material.setUniform("gNormal",new TextureUniform(pc.normal(),1));
                    pc.material.setUniform("gTexCoord",new TextureUniform(pc.texCoord(),2));
                    pc.material.setUniform("gStencil",new TextureUniform(pc.stencil(),3));
                }
                
                setUniforms(pc.material,pc); //Adding lights, etc
                
                pc.service().renderDirect(pc,pc.material,null);
                
            } else {
                
                for (Pass pass : pc.renderPass.passes) {
                    pass.x.preRender(camera);
                    pass.x.input(pc,pass.y.f());
                    pc.service().renderDirect(pc,pass.x.material(),pass.x.buffer());
                    pass.x.postRender(camera);
                }
                if (pc.renderPass.finish!=null) {
                    pc.renderPass.prepareFinish();
                    pc.service().renderDirect(pc,pc.renderPass.material,null);
                }
                
            }
            
            Window window = pc.window();
            int width = window.width();
            int height = window.height();
            if (width!=pc.buffer().width() || height!=pc.buffer().height()) {
                pc.resize(width,height);
            }
        }
    }
    
    private void onSceneUpdate(Scene scene) {
        lights = scene.findComponents(Light.class);
    }
    
    private void onSceneObjectRender(SceneObject so, Camera camera) {
        Mesh mesh = so.getComponent(Mesh.class);
        if (mesh != null && mesh.isVisible()) {
            Material material = mesh.getMaterial();
            
            if (material instanceof StandardMaterial) {
                StandardMaterial sm = (StandardMaterial)material;
                
                if (camera instanceof DeferredCamera) {
                    sm.deferred = true;
                } else {
                    sm.deferred = false;
                }
            }
        }
    }
    
    private void setUniforms(Material material, Camera camera) {
        if (material instanceof StandardMaterial || material instanceof DeferredMaterial) {
            
            Vector3 eye = service.getWorldTransform(camera.sceneObject()).translation;
            
            material.setUniform("eye",eye);
            
            int numLights = 0;
            for (int i=0; i<lights.size(); i++) {
                Light light = lights.get(i);
                Vector3 p = service.getWorldTransform(light.sceneObject()).translation;
                
                material.setUniform("light["+i+"].position",p.get());
                
                material.setUniform("light["+i+"].type",light.type);
                material.setUniform("light["+i+"].color",light.color);
                material.setUniform("light["+i+"].ambient",light.ambient);
                material.setUniform("light["+i+"].falloff",light.falloff);
                material.setUniform("light["+i+"].radius",light.radius);
                material.setUniform("light["+i+"].direction",light.direction);
                
                numLights++;
            }
            material.setUniform("numLights",numLights);
        
        }
    }
    
}

