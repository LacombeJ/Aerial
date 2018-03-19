package jonl.ge.base.app;

import java.util.HashMap;

import jonl.ge.base.light.LightModule;
import jonl.ge.base.physics.PhysicsModule;
import jonl.ge.base.text.TextModule;
import jonl.ge.core.Attachment;
import jonl.ge.core.Delegate;
import jonl.ge.core.Input;
import jonl.ge.core.Platform;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.ge.core.Time;
import jonl.ge.core.Window;
import jonl.ge.core.Input.CursorState;
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
        
        add(new LightModule());
        add(new TextModule());
        add(PhysicsModule.instance());
    }
    
    // -------------------------------------------------------
    
    public abstract void start();
    public abstract void close();
    
    public abstract void setScene(Scene scene);
    
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
    
}
