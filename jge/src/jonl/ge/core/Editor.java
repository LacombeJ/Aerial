package jonl.ge.core;

import jonl.aui.Widget;
import jonl.aui.tea.TGraphics;
import jonl.ge.core.Input.CursorState;
import jonl.ge.core.app.AbstractApplication;
import jonl.ge.core.app.ApplicationWindow;
import jonl.ge.core.editor.EditorCore;
import jonl.ge.core.editor.EditorInput;
import jonl.jgl.GL;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

public class Editor extends AbstractApplication {

    private jonl.jgl.Window glWindow;
    private GL gl;
    private EditorInput input;
    private Window window;
    
    private SceneManager manager = new SceneManager();
    
    private EditorCore core = new EditorCore(this);
    
    private boolean loaded = false;
    
	public Editor() {
		super();
		
	}
	
	void putInfo() {
        info.put("NAME",            "Editor");
        info.put("VERSION",         "1.0");
        info.put("GL_VERSION",      gl.glGetVersion());
        info.put("GLSL_VERSION",    gl.glGetGLSLVersion());
    }
    
    void initialize() {
        
        core.scene.create();
        
        addScene(core.scene.scene);
        addScene(core.scene.overlayScene);
        
    }
    
    void setViewport(Camera camera) {
        Widget view = core.gui.editorViewer;
        double width = getWidth();
        double height = getHeight();
        double yDiff = view.windowY() - view.y();
        double px = view.windowX();
        double py = view.windowY() - yDiff; //Using yDiff because of TWidget orientation of top-left
        double pw = view.width();
        double ph = view.height();
        float left = (float) (px / width);
        float right = (float) ((px+pw) / width);
        float bottom = (float) (py / height);
        float top = (float) ((py+ph) / height);
        camera.setViewport(left,bottom,right,top);
    }
	
	@Override
	public void start() {
	    core.gui.create();

        glWindow = core.gui.window.window();
        gl = glWindow.getGraphicsLibrary();
        input = new EditorInput(core.gui.editorViewer, core.gui.window.input());
        window = new ApplicationWindow(this);
        manager.create(delegate, service, glWindow.getGraphicsLibrary());
        
        
        initialize();
        
        core.gui.window.setLoader(()->{
            putInfo();
            manager.load();
            loaded = true;
            gl.glDisable(GL.DEPTH_TEST);
            gl.glDisable(GL.CULL_FACE);
        });
        
        core.gui.editorViewer.paint().connect((g)->{
            TGraphics tg = (TGraphics)g;
            
            setViewport(core.scene.camera);
            setViewport(core.scene.overlayCamera);
            //TODO find out why this is causing weird rendering issues
            //when synchronization is not used between two windows
            
            if (loaded) {
                synchronized (Editor.class) {
                    gl.glEnable(GL.DEPTH_TEST);
                    gl.glEnable(GL.CULL_FACE);
                    int[] box = gl.glGetScissor();
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
        
        core.gui.window.setCloser(()->{
            manager.close();
        });
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
		return core.gui.window.title();
	}

	@Override
	public void setTitle(String title) {
	    core.gui.window.setTitle(title);
	}

	@Override
	public int getWidth() {
		return core.gui.editorViewer.width();
	}

	@Override
	public int getHeight() {
		return core.gui.editorViewer.height();
	}

	@Override
	public void setSize(int width, int height) {
	    core.gui.window.setWidth(width);
	    core.gui.window.setHeight(height);
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
	
	// ------------------------------------------------------------------------
	
	public void setBackground(Vector4 color) {
	    core.scene.background = color.get();
	    core.scene.camera.setClearColor(core.scene.background);
    }
	
	public Vector4 getBackground() {
	    return core.scene.background.get();
	}
	
	

}
