package jonl.jgl.lwjgl;

import jonl.jgl.Window;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class GLFWConstructor {
    
    private String title = "GLFW Window";
    
    private int width = 1024;
    private int height = 576;
    
    private boolean visible = true;
    private boolean fullscreen = false;
    private boolean resizable = false;
    private boolean decorated = true;
    private boolean floating = false;
    private int multiSamples = 4;
    private int resolutionType = Window.WINDOW;
    private boolean vsyncEnabled = true;
    
    public GLFWWindow construct() {
        return new GLFWWindow(title,width,height,visible,fullscreen,resizable,decorated,floating,multiSamples,resolutionType,vsyncEnabled);
    }
    
    public GLFWConstructor setTitle(String title) {
        this.title = title;
        return this;
    }
    public GLFWConstructor setWidth(int width) {
        this.width = width;
        return this;
    }
    public GLFWConstructor setHeight(int height) {
        this.height = height;
        return this;
    } 
    public GLFWConstructor setVisible(boolean visible) {
    	this.visible = visible;
    	return this;
    }
    public GLFWConstructor setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }
    public GLFWConstructor setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }
    public GLFWConstructor setDecorated(boolean decorated) {
        this.decorated = decorated;
        return this;
    }
    public GLFWConstructor setFloating(boolean floating) {
        this.floating = floating;
        return this;
    }
    public GLFWConstructor setMultiSamples(int multiSamples) {
        this.multiSamples = multiSamples;
        return this;
    }
    public GLFWConstructor setResolutionType(int resolutionType) {
        this.resolutionType = resolutionType;
        return this;
    }
    public GLFWConstructor setVsyncEnabled(boolean vsyncEnabled) {
        this.vsyncEnabled = vsyncEnabled;
        return this;
    }
    
}
