package jonl.ge.core;

import jonl.ge.core.Input.CursorState;
import jonl.ge.core.app.AbstractApplication;
import jonl.ge.core.app.ApplicationInput;
import jonl.ge.core.app.ApplicationWindow;
import jonl.ge.core.ui.ApplicationUI;
import jonl.ge.core.ui.UI;
import jonl.jgl.AudioDevice;
import jonl.jgl.AL;
import jonl.jgl.lwjgl.ALDevice;
import jonl.jgl.lwjgl.GLFWWindow;

public class Application extends AbstractApplication {

	private String title = "Application";
    private int width = 1024;
    private int height = 576;
    private boolean fullscreen = false;
    private boolean resizable = false;
    
    private int samples = 4; // antialias samples
    
    private jonl.jgl.Window glWindow;
    
    private AudioDevice audio;
    @SuppressWarnings("unused") //TODO handle al
    private AL al;
    
    private Input input;
    private Window window;
    
    private SceneManager manager = new SceneManager();
	
    private boolean esc = true;
    
    private UI ui = new UI();
    private ApplicationUI appUi;
    
	public Application() {
		super();
		
		caller.implement("SET_SAMPLES", (args) -> {
		    samples = args.getInt(0);
		    return true;
		});
		
		caller.implement("SET_ESC", (args) -> {
            esc = args.getBoolean(0);
            return true;
        });
		
	}
	
	@Override
	public void start() {
		glWindow = new GLFWWindow(
	        title,
	        width,
	        height,
	        true,
	        fullscreen,
	        resizable,
	        true,
	        samples,
	        jonl.jgl.Window.MONITOR,
	        true);
        
        width = glWindow.getWidth();
        height = glWindow.getHeight();
        
        audio = new ALDevice();
        al = audio.getAudioLibrary();
        
        input = new ApplicationInput(glWindow.getInput());
        window = new ApplicationWindow(this);
        manager.create(delegate, service, glWindow.getGraphicsLibrary());
        
        appUi = new ApplicationUI();
        
        glWindow.setLoader(()->{
            putInfo();
            manager.load();
            manager.update();
            
            appUi.setWidget(ui.getWidget());
            appUi.load(glWindow);
        });
        
        glWindow.setRunner(()->{
            while (glWindow.isRunning()) {
                //TODO find out why this is causing weird rendering issues
                //when synchronization is not used between two windows
                synchronized (Application.class) {
                    manager.update();
                    appUi.update();
                    if (esc && input.isKeyPressed(Input.K_ESCAPE)) {
                        close();
                    }
                }
            }
        });
        
        glWindow.setCloser(()->{
            manager.close();
            audio.destroy();
        });
        
        glWindow.addSizeListener((w,h,pw,ph)->{
            width = w;
            height = h;
        });
        
        audio.create();
        glWindow.start();
        
	}

	void putInfo() {
        info.put("NAME",            "Editor");
        info.put("VERSION",         "1.0");
        info.put("GL_VERSION",      glWindow.getGraphicsLibrary().glGetVersion());
        info.put("GLSL_VERSION",    glWindow.getGraphicsLibrary().glGetGLSLVersion());
    }
	
	public UI ui() {
	    return ui;
	}
	
	@Override
	public void close() {
		glWindow.close();
	}
	
	@Override
	public void setScene(Scene scene) {
		scene.application = this;
		manager.setScene(scene);
	}

	@Override
	public Input input() {
		return input;
	}

	@Override
	public Window window() {
		return window;
	}

	@Override
	public CursorState getCursorState() {
		return CursorState.state(glWindow.getCursorState());
	}

	@Override
	public void setCursorState(CursorState state) {
		glWindow.setCursorState(state.state);
		
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
        if (glWindow!=null) {
            glWindow.setTitle(title);
        }
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
        this.height = height;
        if (glWindow!=null) {
            glWindow.setSize(this.width,this.height);
        }
	}

	@Override
	public boolean isResizable() {
		if (glWindow!=null) return glWindow.isResizable();
        return resizable;
	}

	@Override
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
        if (glWindow!=null) {
            Engine.log.info("Cannot change resizable window property after creation");
        }
	}

	@Override
	public boolean isFullscreen() {
		if (glWindow!=null) return glWindow.isFullscreen();
        return fullscreen;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
        if (glWindow!=null) {
            Engine.log.info("Cannot change fullscreen window property after creation");
        }
	}

}
