package jonl.ge;

import java.util.ArrayList;

import jonl.jutils.io.Console;
import jonl.jutils.misc.DataMap;
import jonl.ge.Input.CursorState;
import jonl.jgl.AudioDevice;
import jonl.jgl.AudioLibrary;
import jonl.jgl.lwjgl.ALDevice;
import jonl.jgl.lwjgl.GLFWWindow;

public class Application extends AbstractApp {

    private String title = "Application";
    private int width = 1024;
    private int height = 576;
    private boolean fullscreen = false;
    private boolean resizable = false;
    
    private DataMap info = new DataMap();
    
    private jonl.jgl.Window glWindow;
    
    private AudioDevice audio;
    @SuppressWarnings("unused") //TODO handle al
    private AudioLibrary al;
    
    private Input input;
    private Updater updater;
    private Renderer renderer;
    private Window window;
    
    private ArrayList<Scene> scenes = new ArrayList<>();
    private int currentScene = 0;
    
    @Override
    public void add(Scene scene) {
        scene.application = this;
        scenes.add(scene);
    }
    
    @Override
    public void start() {
        
        glWindow = new GLFWWindow(title,width,height,true,fullscreen,resizable,true,4,true);
        
        width = glWindow.getWidth();
        height = glWindow.getHeight();
        
        audio = new ALDevice();
        al = audio.getAudioLibrary();
        
        input = new AppInput(glWindow.getInput());
        updater = new AppUpdater(this);
        renderer = new AppRenderer(this,glWindow.getGraphicsLibrary());
        window = new AppWindow(this);
        
        glWindow.setLoader(()->{
            putInfo();
            updater.load();
            renderer.load();
            appLoader().f();
            Scene scene = scenes.get(currentScene);
            scene.create();
            updater.update(scene);
            renderer.render(scene);
        });
        
        glWindow.setRunner(()->{
            while (glWindow.isRunning()) {
                Scene scene = scenes.get(currentScene);
                //TODO find out why this is causing weird rendering issues
                //when synchronization is not used between two windows
                synchronized (Application.class) {
                    getTime().update();
                    updater.update(scene);
                    appUpdater().f();
                    renderer.render(scene);
                }
            }
        });
        
        glWindow.setCloser(()->{
            audio.destroy();
            appCloser().f();
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

    @Override
    public void close() {
        glWindow.close();
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
    public boolean isResizable() {
        if (glWindow!=null) return glWindow.isResizable();
        return resizable;
    }

    @Override
    public boolean isFullscreen() {
        if (glWindow!=null) return glWindow.isFullscreen();
        return fullscreen;
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
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        if (glWindow!=null) {
            //TODO app error
            Console.println("Cannot change resizable window property after creation");
        }
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if (glWindow!=null) {
            //TODO app error
            Console.println("Cannot change fullscreen window property after creation");
        }
    }
    
    @Override
    public String getInfo(String key) {
        return info.get(key);
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
    public Window getWindow() {
        return window;
    }

}
