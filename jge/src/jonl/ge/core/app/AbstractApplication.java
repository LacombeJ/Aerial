package jonl.ge.core.app;

import java.util.HashMap;

import jonl.ge.core.Attachment;
import jonl.ge.core.Delegate;
import jonl.ge.core.Input;
import jonl.ge.core.Platform;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.ge.core.Time;
import jonl.ge.core.Window;
import jonl.ge.core.Input.CursorState;
import jonl.ge.mod.fx.FXModule;
import jonl.ge.mod.physics.PhysicsModule;
import jonl.ge.mod.ray.RayModule;
import jonl.ge.mod.render.RenderModule;
import jonl.ge.mod.text.TextModule;
import jonl.jutils.call.Caller;
import jonl.jutils.misc.SystemUtils;
import jonl.jutils.structs.AttributeMap;

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
