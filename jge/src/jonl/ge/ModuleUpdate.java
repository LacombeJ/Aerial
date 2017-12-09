package jonl.ge;

import java.util.List;

public abstract class ModuleUpdate extends Module {

	public void preUpdate() {
		
	}
	
	public void update(GameObject gameObject, Transform worldParent) {
		
	}
	
	public void postUpdate() {
		
	}
	
	static void preUpdate(List<ModuleUpdate> modules) {
		for (ModuleUpdate m : modules) {
			m.preUpdate();
		}
	}
	
	static void update(List<ModuleUpdate> modules, GameObject gameObject, Transform worldParent) {
		for (ModuleUpdate m : modules) {
			m.update(gameObject, worldParent);
		}
	}
	
	static void postUpdate(List<ModuleUpdate> modules) {
		for (ModuleUpdate m : modules) {
			m.postUpdate();
		}
	}
	
}
