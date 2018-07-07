package ax.engine.core.app;

import java.util.HashMap;

import ax.commons.call.Caller;
import ax.commons.misc.SystemUtils;
import ax.commons.structs.AttributeMap;
import ax.engine.core.Attachment;
import ax.engine.core.Delegate;
import ax.engine.core.Input;
import ax.engine.core.Platform;
import ax.engine.core.Scene;
import ax.engine.core.Service;
import ax.engine.core.Time;
import ax.engine.core.Window;
import ax.engine.core.Input.CursorState;
import ax.engine.mod.fx.FXModule;
import ax.engine.mod.physics.PhysicsModule;
import ax.engine.mod.ray.RayModule;
import ax.engine.mod.render.RenderModule;
import ax.engine.mod.text.TextModule;

public abstract class AbstractApplication {
	
	//TODO move comments/descriptions to abstract functions in this and other classes
	//TODO move as much things as possible here
    protected Platform platform;
    protected Time time;
    protected Delegate delegate;
    protected Service service;
    protected AttributeMap info;
    protected HashMap<String,Attachment> attachments;
    protected Caller caller;
	
    protected AbstractApplication() {
    	if (SystemUtils.isWindows()) {
            platform = Platform.WINDOWS;
        } else if (SystemUtils.isLinux()) {
            platform = Platform.LINUX;
        }
        time = new Time();
        delegate = new Delegate();
        service = new Service();
        info = new AttributeMap();
        attachments = new HashMap<>();
        caller = new Caller();
        
        add(new RenderModule());
        add(new TextModule());
        add(new FXModule());
        add(new PhysicsModule());
        add(new RayModule());
    }
    
    // -------------------------------------------------------
    
    public abstract void start();
    public abstract void close();
    
    public abstract void addScene(Scene scene);
    public abstract void removeScene(Scene scene);
    
    public abstract Input input();
    public abstract Window window();
    
    public abstract CursorState getCursorState();
    public abstract void setCursorState(CursorState state);
    
    public abstract String getTitle();
    public abstract void setTitle(String title);
    
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract void setSize(int width, int height);
    
    public abstract boolean isResizable();
    public abstract void setResizable(boolean resizable);
    
    public abstract boolean isFullscreen();
    public abstract void setFullscreen(boolean fullscreen);
    
    // -------------------------------------------------------
    
    public Delegate delegate() {
    	return delegate;
    }
    
    public Service service() {
    	return service;
    }
    
    public void add(Attachment attach) {
    	Attachment prev = attachments.put(attach.getName(), attach);
    	if (prev != null) {
    	    prev.remove(delegate, service);
    	}
    	attach.add(delegate, service);
    }
    
    public void remove(String key) {
    	Attachment attach = attachments.remove(key);
    	if (attach != null) {
    	    attach.remove(delegate, service);
    	}
    }
    
    public void remove(Attachment attach) {
        boolean removed = attachments.remove(attach.getName(), attach);
        if (removed) {
            attach.remove(delegate, service);
        }
    }
    
    public Platform platform() {
        return platform;
    }
    
    public Time time() {
        return time;
    }
    
    public String info(String key) {
        return info.getString(key);
    }
    
    /**
     * Used to add functionality without changing api
     * @param call
     * @return null if call failed, true or an object if succeeded
     */
    public Object call(String call) {
        return caller.call(call);
    }
    
}
