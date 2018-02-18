package jonl.ge.core;

import jonl.aui.Widget;
import jonl.ge.base.app.ApplicationWindow;
import jonl.ge.base.SceneManager;
import jonl.ge.base.app.AbstractApplication;
import jonl.ge.base.app.EditorAssets;
import jonl.ge.base.app.EditorGUI;
import jonl.ge.base.app.EditorInput;
import jonl.ge.core.Input.CursorState;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.Target;

public class Editor extends AbstractApplication {

	final jonl.jgl.Window glWindow;
    final GraphicsLibrary gl;
    final EditorInput input;
    final Window window;
    final EditorGUI gui;
    
    private SceneManager manager = new SceneManager();
    
    Camera camera;
	
	public Editor() {
		super();
		
		gui = new EditorGUI(this);
        
        gui.create();
        
        glWindow = gui.window.getWindow();
        gl = glWindow.getGraphicsLibrary();
        input = new EditorInput(gui.editorViewer,gui.window.getInput());
        window = new ApplicationWindow(this);
        manager.create(delegate, service, glWindow.getGraphicsLibrary());
        
        initialize();
        
        gui.window.setLoader(()->{
            putInfo();
            manager.load();
        });
        
        gui.editorViewer.addPainter((g)->{
            setViewport(camera);
            //TODO find out why this is causing weird rendering issues
            //when synchronization is not used between two windows
            synchronized (Editor.class) {
                gl.glEnable(Target.CULL_FACE);
                gl.glEnable(Target.DEPTH_TEST);
                int[] box = gl.glGetScissor();
                manager.update();
                gl.glScissor(box);
                gl.glDisable(Target.CULL_FACE);
                gl.glDisable(Target.DEPTH_TEST);
                gl.glEnable(Target.SCISSOR_TEST);
            }
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
        camera.setClearColor(new float[]{0.1f,0.2f,0.4f,1f});
        camera.scaleProjection = true;
        
        control.addComponent(camera);
        
        GameObject b = EditorAssets.cube();
        s.add(b);
        
        s.add(control);
        
        setScene(s);
        
        s.create();
    }
    
    void setViewport(Camera camera) {
        Widget view = gui.editorViewer;
        double px = view.getWindowX();
        double py = view.getWindowY();
        double pw = view.getWidth();
        double ph = view.getHeight();
        double width = getWidth();
        double height = getHeight();
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
		return gui.window.getTitle();
	}

	@Override
	public void setTitle(String title) {
		gui.window.setTitle(title);
	}

	@Override
	public int getWidth() {
		return gui.editorViewer.getWidth();
	}

	@Override
	public int getHeight() {
		return gui.editorViewer.getHeight();
	}

	@Override
	public void setSize(int width, int height) {
		gui.window.setWidth(width);
        gui.window.setHeight(height);
	}

	@Override
	public boolean isResizable() {
		return gui.resizable;
	}

	@Override
	public void setResizable(boolean resizable) {
		gui.window.setResizable(resizable);
        gui.resizable = resizable;
	}

	@Override
	public boolean isFullscreen() {
		return gui.fullscreen;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		//Cannot fullscreen TODO
	}

}
