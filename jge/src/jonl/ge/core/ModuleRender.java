package jonl.ge.core;

import java.util.List;

/**
 * App module for adding render update functionality to application
 * 
 * @author Jonathan
 *
 */
public abstract class ModuleRender extends Module {

	public void renderPrepare(Scene scene) {
		
	}
	
	public void renderUpdate(Camera camera, GameObject gameObject) {
		
	}
	
	static void renderPrepare(List<ModuleRender> modules, Scene scene) {
		for (ModuleRender m : modules) {
			m.renderPrepare(scene);
		}
	}
	
	static void renderUpdate(List<ModuleRender> modules, Camera camera, GameObject gameObject) {
		for (ModuleRender m : modules) {
			m.renderUpdate(camera,gameObject);
		}
	}
	
}
