package jonl.ge.core;

import jonl.aui.Widget;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWindow;
import jonl.ge.core.Input.CursorState;
import jonl.ge.core.app.AbstractApplication;
import jonl.ge.core.app.ApplicationWindow;
import jonl.jgl.GL;
import jonl.vmath.Matrix4;

public class SubApp extends AbstractApplication {

    private GL gl;
    private SubAppInput input;
    
    private Window window;
    private jonl.aui.tea.TWindow uiWindow;
    private jonl.jgl.Window glWindow;
    
    private SceneManager manager = new SceneManager();
    
    private boolean loaded = false;
    
    private Widget widget;
    
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
        gl = glWindow.getGraphicsLibrary();
        input = new SubAppInput(widget, uiWindow.input());
        window = new ApplicationWindow(this);
        manager.create(delegate, service, glWindow.getGraphicsLibrary());
        manager.renderer().subsection(true);
        
        uiWindow.addLoader(()->{
            putInfo();
            manager.load();
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
                    manager.renderer().offset(box[0], box[1]);
                    manager.renderer().dimension(box[2], box[3]);
                    manager.update();
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
	public void addScene(Scene scene) {
		scene.application = this;
		manager.addScene(scene);
	}
	
	@Override
    public void removeScene(Scene scene) {
        scene.application = null;
        manager.removeScene(scene);
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
