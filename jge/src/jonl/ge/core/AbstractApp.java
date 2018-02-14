package jonl.ge.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jonl.ge.base.App;
import jonl.jutils.func.Callback0D;
import jonl.jutils.misc.SystemUtils;

public abstract class AbstractApp implements App {

    //TODO move as much things as possible here
    private Platform platform;
    private Time time;
    private Callback0D appLoader = () -> {};
    private Callback0D appUpdater = () -> {};
    private Callback0D appCloser = () -> {};
    private HashMap<String,Module> moduleMap;
    
    AbstractApp() {
        if (SystemUtils.isWindows()) {
            platform = Platform.WINDOWS;
        } else if (SystemUtils.isLinux()) {
            platform = Platform.LINUX;
        }
        time = new Time();
        moduleMap = new HashMap<>();
        addModule("Light", new ModuleLight());
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    public Time getTime() {
        return time;
    }
    
    @Override
    public void addModule(String name, Module m) {
    	moduleMap.put(name, m);
    }
    
    @Override
    public void removeModule(String name) {
    	moduleMap.remove(name);
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
    
    public void setLoader(Callback0D load) {
    	appLoader = load;
    }
    protected Callback0D appLoader() {
    	return appLoader;
    }
    public void setUpdater(Callback0D update) {
    	appUpdater = update;
    }
    protected Callback0D appUpdater() {
    	return appUpdater;
    }
    public void setCloser(Callback0D close) {
    	appCloser = close;
    }
    protected Callback0D appCloser() {
    	return appCloser;
    }
    
    
    
    
    
}
