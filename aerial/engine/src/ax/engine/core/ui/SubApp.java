package ax.engine.core.ui;

import ax.aui.Widget;
import ax.engine.core.BaseApp;
import ax.engine.core.Input;
import ax.engine.core.Input.CursorState;
import ax.engine.core.Window;
import ax.engine.core.app.ApplicationWindow;
import ax.graphics.GL;
import ax.math.vector.Matrix4;
import ax.tea.TGraphics;
import ax.tea.TWindow;

public class SubApp extends BaseApp {

    private GL gl;
    private SubAppInput input;
    
    private Window window;
    private ax.tea.TWindow uiWindow;
    private ax.graphics.Window glWindow;
    
    private boolean loaded = false;
    
    private Widget widget;
    
    // This variable is used because SubAppInput doesn't handle input events when input is out of sub app bounds
    // but this might ignore when the mouse is grabbed and the cursor is "out of bounds"
    boolean grabbed = false;
    
	public SubApp(ax.aui.Window window, Widget widget) {
		super();
		uiWindow = (TWindow) window;
		this.widget = widget;
	}
	
	void putInfo() {
        info.put("NAME",            "Application");
        info.put("VERSION",         "1.0");
        info.put("GL_VERSION",      gl.glGetVersion());
        info.put("GLSL_VERSION",    gl.glGetGLSLVersion());
    }

	@Override
	public void start() {
	    
        glWindow = uiWindow.window();
        gl = glWindow.getGL();
        input = new SubAppInput(this, widget, uiWindow.handledInput());
        window = new ApplicationWindow(this);
        managerCreate(delegate, service, glWindow.getGL());
        managerRendererSubsection(true);
        
        uiWindow.addLoader(()->{
            putInfo();
            managerLoad();
            loaded = true;
            gl.glDisable(GL.DEPTH_TEST);
            gl.glDisable(GL.CULL_FACE);
        });
        
        widget.paint().connect((g)->{
            TGraphics tg = (TGraphics)g;
            
            //TODO find out why this is causing weird rendering issues
            //when synchronization is not used between two windows
            if (loaded) {
                synchronized (SubApp.class) {
                    gl.glEnable(GL.DEPTH_TEST);
                    gl.glEnable(GL.CULL_FACE);
                    int[] box = gl.glGetScissor();
                    managerRendererOffset(box[0], box[1]);
                    managerRendererDimension(box[2], box[3]);
                    managerUpdate();
                    gl.glEnable(GL.SCISSOR_TEST);
                    gl.glScissor(box);
                    gl.glDisable(GL.DEPTH_TEST);
                    gl.glDisable(GL.CULL_FACE);
                }
            }
            
            //Reset viewport and projection
            Matrix4 ortho = Matrix4.orthographic(0,glWindow.getWidth(),glWindow.getHeight(),0,-1,1);
            tg.setOrtho(ortho);
            gl.glViewport(0,0,glWindow.getWidth(),glWindow.getHeight());
        });
        
        //manager.close();
	}

	@Override
	public void close() {
		glWindow.close();
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
	    ax.graphics.Input.CursorState state = glWindow.getCursorState();
	    switch(state) {
	    case NORMAL:   return CursorState.NORMAL;
	    case GRABBED:  return CursorState.GRABBED;
	    case HIDDEN:   return CursorState.HIDDEN;
	    }
	    return CursorState.NORMAL;
	}

	@Override
	public void setCursorState(CursorState state) {
	    switch(state) {
        case NORMAL:
            glWindow.setCursorState(ax.graphics.Input.CursorState.NORMAL);
            grabbed = false;
            break;
        case GRABBED:
            glWindow.setCursorState(ax.graphics.Input.CursorState.GRABBED);
            grabbed = true;
            break;
        case HIDDEN:
            glWindow.setCursorState(ax.graphics.Input.CursorState.HIDDEN);
            grabbed = false;
            break;
	    }
	}

	@Override
	public String getTitle() {
		return widget.name();
	}

	@Override
	public void setTitle(String title) {
	    widget.setName(title);
	}

	@Override
	public int getWidth() {
		return widget.width();
	}

	@Override
	public int getHeight() {
		return widget.height();
	}

	@Override
	public void setSize(int width, int height) {
	    widget.setMinSize(width,height);
	}

	@Override
	public boolean isResizable() {
	    //Editor should always be resizable
	    return true;
	}

	@Override
	public void setResizable(boolean resizable) {
	    //Editor should always be resizable
	}

	@Override
	public boolean isFullscreen() {
		return false;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		//Cannot fullscreen TODO
	}

}
