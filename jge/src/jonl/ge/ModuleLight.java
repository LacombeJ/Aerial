package jonl.ge;

import java.util.List;

import jonl.jgl.Program;
import jonl.vmath.Vector3;

class ModuleLight extends Module {

	void setUniforms(Updater updater, Program program, List<Light> lights, Camera camera) {
		
		Vector3 eye = updater.getWorldTransform(camera.gameObject).translation;
        AppUtil.setUniform(program,"eye",eye);
        
        int numLights = 0;
        for (int i=0; i<lights.size(); i++) {
            Light light = lights.get(i);
            Vector3 p = updater.getWorldTransform(light.gameObject).translation;
            program.setUniformi("light["+i+"].type",light.type);
            AppUtil.setUniform(program,"light["+i+"].position",p);
            program.setUniform("light["+i+"].range",light.range);
            AppUtil.setUniform(program,"light["+i+"].color",light.color);
            program.setUniform("light["+i+"].intensity",light.intensity);
            program.setUniform("light["+i+"].angle",light.angle);
            numLights++;
        }
        program.setUniformi("numLights",numLights);
		
	}
	
	static void setUniforms(List<ModuleLight> modules, Updater updater, Program program, List<Light> lights, Camera camera) {
		for (ModuleLight m : modules) {
			m.setUniforms(updater, program,lights,camera);
		}
	}
	
}
