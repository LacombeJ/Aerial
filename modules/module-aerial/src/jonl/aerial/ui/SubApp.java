package jonl.aerial.ui;

import jonl.aui.Widget;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWindow;
import jonl.ge.core.BaseApp;
import jonl.ge.core.Input;
import jonl.ge.core.Input.CursorState;
import jonl.ge.core.Window;
import jonl.ge.core.app.ApplicationWindow;
import jonl.jgl.GL;
import jonl.vmath.Matrix4;

public class SubApp extends BaseApp {

    private GL gl;
    private SubAppInput input;
    
    private jonl.ge.core.Window window;
    private jonl.aui.tea.TWindow uiWindow;
    private jonl.jgl.Window glWindow;
    
    private boolean loaded = false;
    
    private Widget widget;
    
    // This variable is used because SubAppInput doesn't handle input events when input is out of sub app bounds
    // but this might ignore when the mouse is grabbed and the cursor is "out of bounds"
    boolean grabbed = false;
    
	public SubApp(jonl.aui.Window window, Widget widget) {
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
	    jonl.jgl.Input.CursorState state = glWindow.getCursorState();
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
            glWindow.setCursorState(jonl.jgl.Input.CursorState.NORMAL);
            grabbed = false;
            break;
        case GRABBED:
            glWindow.setCursorState(jonl.jgl.Input.CursorState.GRABBED);
            grabbed = true;
            break;
        case HIDDEN:
            glWindow.setCursorState(jonl.jgl.Input.CursorState.HIDDEN);
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
