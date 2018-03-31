package jonl.ge.core.light;

import java.util.List;

import jonl.ge.core.Attachment;
import jonl.ge.core.Camera;
import jonl.ge.core.Delegate;
import jonl.ge.core.Material;
import jonl.ge.core.Service;
import jonl.ge.utils.GLUtils;
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
            
            Vector3 eye = service.getWorldTransform(camera.gameObject()).translation;
            GLUtils.setUniform(program,"eye",eye);
            
            int numLights = 0;
            for (int i=0; i<lights.size(); i++) {
                Light light = lights.get(i);
                Vector3 p = service.getWorldTransform(light.gameObject()).translation;
                program.setUniformi("light["+i+"].type",light.getType());
                GLUtils.setUniform(program,"light["+i+"].position",p);
                program.setUniform("light["+i+"].range",light.getRange());
                GLUtils.setUniform(program,"light["+i+"].color",light.getColor());
                program.setUniform("light["+i+"].intensity",light.getIntensity());
                program.setUniform("light["+i+"].angle",light.getAngle());
                numLights++;
            }
            program.setUniformi("numLights",numLights);
        };

    }

    @Override
    public void add(Delegate delegate, Service service) {
        this.service = service;
        delegate.onFindLights().add(findLights);
        delegate.onProgramUpdate().add(setUniforms);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onFindLights().remove(findLights);
        delegate.onProgramUpdate().remove(setUniforms);
    }

}
