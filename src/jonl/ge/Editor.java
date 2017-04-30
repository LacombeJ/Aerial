package jonl.ge;

import java.util.ArrayList;

import jonl.aui.Widget;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Input;
import jonl.jgl.GraphicsLibrary.Target;
import jonl.jgl.Input.CursorState;

public class Editor implements App {
    
    final jonl.jgl.Window glWindow;
    final GraphicsLibrary gl;
    final EditorInput input;
    final Updater updater;
    final Renderer renderer;
    final EditorGUI gui;
    
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
        
        initialize();
        
        gui.window.setLoader(()->{
            updater.load();
            renderer.load();
        });
        
        gui.editorViewer.addPainter((g)->{
            setViewport(camera);
            Scene scene = scenes.get(currentScene);
            updater.update(scene);
            gl.glEnable(Target.CULL_FACE);
            gl.glEnable(Target.DEPTH_TEST);
            int[] box = gl.glGetScissor();
            renderer.render(scene);
            gl.glScissor(box);
            gl.glDisable(Target.CULL_FACE);
            gl.glDisable(Target.DEPTH_TEST);
            gl.glEnable(Target.SCISSOR_TEST);
        });
        
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
                
                GameObject b = EngineAssets.cube();
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
    public void prepare() {
        // TODO Auto-generated method stub
        
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
        return glWindow.getCursorState();
    }

    @Override
    public void setCursorState(CursorState state) {
        glWindow.setCursorState(state);
    }

    @Override
    public String getTitle() {
        return gui.window.getTitle();
    }

    @Override
    public int[] getSize() {
        return new int[]{getWidth(),getHeight()};
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
    
    
    
}
