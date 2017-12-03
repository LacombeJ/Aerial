package jonl.ge;

import java.util.ArrayList;

import jonl.aui.Widget;
import jonl.ge.Input.CursorState;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.Target;
import jonl.jutils.misc.DataMap;

public class Editor extends AbstractApp {
    
    final jonl.jgl.Window glWindow;
    final GraphicsLibrary gl;
    final EditorInput input;
    final Updater updater;
    final Renderer renderer;
    final Window window;
    final EditorGUI gui;
    
    private DataMap info = new DataMap();
    
    private ArrayList<Scene> scenes = new ArrayList<>();
    private int currentScene = 0;
    
    Camera camera;
    
    public Editor() {
        
        gui = new EditorGUI(this);
        
        gui.create();
        
        glWindow = gui.window.getWindow();
        gl = glWindow.getGraphicsLibrary();
        input = new EditorInput(gui.editorViewer,gui.window.getInput());
        updater = new AppUpdater();
        renderer = new AppRenderer(this,gl);
        window = new AppWindow(this);
        
        initialize();
        
        gui.window.setLoader(()->{
            putInfo();
            getTime().update();
            updater.load();
            renderer.load();
            appLoader().f();
        });
        
        gui.editorViewer.addPainter((g)->{
            setViewport(camera);
            Scene scene = scenes.get(currentScene);
            //TODO find out why this is causing weird rendering issues
            //when synchronization is not used between two windows
            synchronized (Editor.class) {
                updater.update(scene);
                appUpdater().f();
                gl.glEnable(Target.CULL_FACE);
                gl.glEnable(Target.DEPTH_TEST);
                int[] box = gl.glGetScissor();
                renderer.render(scene);
                gl.glScissor(box);
                gl.glDisable(Target.CULL_FACE);
                gl.glDisable(Target.DEPTH_TEST);
                gl.glEnable(Target.SCISSOR_TEST);
            }
        });
        
        gui.window.setCloser(()->{
        	appCloser().f();
        });
    }
    
    void putInfo() {
        info.put("NAME",            "Editor");
        info.put("VERSION",         "1.0");
        info.put("GL_VERSION",      gl.glGetVersion());
        info.put("GLSL_VERSION",    gl.glGetGLSLVersion());
    }
    
    void initialize() {
        Scene s = new Scene() {

            @Override
            protected void prepare() {
                
                GameObject control = EditorAssets.control(gui.window);
                
                camera  = new Camera();
                camera.setClearColor(new float[]{0.1f,0.2f,0.4f,1f});
                camera.scaleProjection = true;
                
                control.addComponent(camera);
                
                GameObject b = AppUtil.cube();
                add(b);
                
                add(control);
                
            }
            
        };
        
        add(s);
        
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
    public void add(Scene scene) {
        scene.application = this;
        scenes.add(scene);
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
    public Input getInput() {
        return input;
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
    public int getWidth() {
        return gui.editorViewer.getWidth();
    }

    @Override
    public int getHeight() {
        return gui.editorViewer.getHeight();
    }

    @Override
    public boolean isResizable() {
        return gui.resizable;
    }

    @Override
    public boolean isFullscreen() {
        return gui.fullscreen;
    }

    @Override
    public void setTitle(String title) {
        gui.window.setTitle(title);
    }

    @Override
    public void setSize(int width, int height) {
        gui.window.setWidth(width);
        gui.window.setHeight(height);
    }

    @Override
    public void setResizable(boolean resizable) {
        gui.window.setResizable(resizable);
        gui.resizable = resizable;
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        //Cannot fullscreen TODO
    }

    @Override
    public Updater getUpdater() {
        return updater;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public String getInfo(String key) {
        return null;
    }

    @Override
    public Window getWindow() {
        return window;
    }
    
    
    
}
