package jonl.ge.core;

import jonl.aui.Widget;
import jonl.aui.tea.TGraphics;
import jonl.ge.core.Input.CursorState;
import jonl.ge.core.app.AbstractApplication;
import jonl.ge.core.app.ApplicationWindow;
import jonl.ge.core.app.EditorAssets;
import jonl.ge.core.app.EditorGUI;
import jonl.ge.core.app.EditorInput;
import jonl.ge.mod.misc.CameraControl;
import jonl.jgl.GL;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class Editor extends AbstractApplication {

	final jonl.jgl.Window glWindow;
    final GL gl;
    final EditorInput input;
    final Window window;
    final EditorGUI gui;
    
    private SceneManager manager = new SceneManager();
    
    Camera camera;
	
    Vector4 background = new Vector4(0.5f,0.5f,0.55f,1f);
    
	public Editor() {
		super();
		
		gui = new EditorGUI(this);
		
        gui.create();
        
        glWindow = gui.window.window();
        gl = glWindow.getGraphicsLibrary();
        input = new EditorInput(gui.editorViewer, gui.window.input());
        window = new ApplicationWindow(this);
        manager.create(delegate, service, glWindow.getGraphicsLibrary());
        
        initialize();
        
        gui.window.setLoader(()->{
            putInfo();
            manager.load();
            
            gl.glDisable(GL.DEPTH_TEST);
            gl.glDisable(GL.CULL_FACE);
        });
        
        gui.editorViewer.paint().connect((g)->{
            TGraphics tg = (TGraphics)g;
            
            setViewport(camera);
            //TODO find out why this is causing weird rendering issues
            //when synchronization is not used between two windows
            
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
            
            //Reset viewport and projection
            Matrix4 ortho = Matrix4.orthographic(0,glWindow.getWidth(),glWindow.getHeight(),0,-1,1);
            tg.setOrtho(ortho);
            gl.glViewport(0,0,glWindow.getWidth(),glWindow.getHeight());
        });
        
        gui.window.setCloser(()->{
        	manager.close();
        });
	}
	
	void putInfo() {
        info.put("NAME",            "Editor");
        info.put("VERSION",         "1.0");
        info.put("GL_VERSION",      gl.glGetVersion());
        info.put("GLSL_VERSION",    gl.glGetGLSLVersion());
    }
    
    void initialize() {
    	
        Scene s = new Scene();

        GameObject control = EditorAssets.control(gui.window);
        
        camera  = new Camera();
        camera.setClearColor(background);
        camera.scaleProjection = true;
        control.addComponent(camera);
        
        CameraControl cc = new CameraControl();
        control.addComponent(cc);
        control.addCreate(()->{
            cc.lookAt(new Vector3(0,0,0));
        });
        
        control.transform().translation.set(5,5,5);
        
        
        GameObject b = EditorAssets.cube();
        s.add(b);
        
        s.add(control);
        
        addScene(s);
        
        s.create();
    }
    
    void setViewport(Camera camera) {
        Widget view = gui.editorViewer;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
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
		return gui.window.title();
	}

	@Override
	public void setTitle(String title) {
		gui.window.setTitle(title);
	}

	@Override
	public int getWidth() {
		return gui.editorViewer.width();
	}

	@Override
	public int getHeight() {
		return gui.editorViewer.height();
	}

	@Override
	public void setSize(int width, int height) {
		gui.window.setWidth(width);
        gui.window.setHeight(height);
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
	    background = color.get();
        camera.setClearColor(background);
    }
	
	public Vector4 getBackground() {
	    return background.get();
	}
	
	

}
