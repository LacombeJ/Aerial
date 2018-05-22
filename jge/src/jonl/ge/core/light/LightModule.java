package jonl.ge.core.light;

import java.util.List;

import jonl.ge.core.Attachment;
import jonl.ge.core.Camera;
import jonl.ge.core.Delegate;
import jonl.ge.core.Material;
import jonl.ge.core.Service;
import jonl.ge.core.material.StandardMaterial;
import jonl.jgl.Program;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback3D;
import jonl.vmath.Vector3;

public class LightModule extends Attachment {

    Callback<List<Light>> findLights;
    Callback3D<Program, Material, Camera> setUniforms;
    
    Service service;
    
    List<Light> lights;
    
    public LightModule() {
        super("light-module");

        findLights = (standardLights) -> {
            lights = standardLights;
        };
        
        setUniforms = (program, material, camera) -> {
            
            if (material instanceof StandardMaterial) {
            
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
            
        };

    }

    @Override
    public void add(Delegate delegate, Service service) {
        this.service = service;
        delegate.onFindLights().add(findLights);
        delegate.onMaterialUpdate().add(setUniforms);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onFindLights().remove(findLights);
        delegate.onMaterialUpdate().remove(setUniforms);
    }

}
