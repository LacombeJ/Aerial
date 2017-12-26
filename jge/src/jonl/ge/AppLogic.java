package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jonl.jutils.func.Callback;

public class AppLogic {

	private HashMap<String,Module> moduleMap;
	
	static abstract class Module {
		
		
		
		static <M extends Module> void call(ArrayList<M> modules, Callback<M> cb) {
			for (M module : modules) {
				cb.f(module);
			}
		}
	}
	
	abstract class ModuleLogic extends Module {
		abstract void create();
		abstract void update();
		abstract void destroy();
	}
	
	abstract class ModuleDisplay extends Module {
		abstract void load();
		abstract void render();
		abstract void close();
	}
	
	AppLogic() {
		
	}
	
	void create() {
		Module.call(getModule(ModuleLogic.class), (m) -> m.create() );
		Module.call(getModule(ModuleDisplay.class), (m) -> m.load() );
	}
	
	void update() {
		Module.call(getModule(ModuleLogic.class), (m) -> m.update() );
		Module.call(getModule(ModuleDisplay.class), (m) -> m.render() );
	}
	
	void destroy() {
		Module.call(getModule(ModuleLogic.class), (m) -> m.destroy() );
		Module.call(getModule(ModuleDisplay.class), (m) -> m.close() );
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Module> ArrayList<T> getModule(Class<T> c) {
    	ArrayList<T> list = new ArrayList<>();
    	for (Entry<String,Module> e : moduleMap.entrySet()) {
    		Module m = e.getValue();
    		if (c.isInstance(m)) {
    			list.add((T) m);
    		}
    	}
    	return list;
    }
	
}
