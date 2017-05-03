package jonl.ge;

import java.util.ArrayList;

import jonl.jutils.io.Console;
import jonl.ge.Input.CursorState;
import jonl.jgl.AudioDevice;
import jonl.jgl.AudioLibrary;
import jonl.jgl.Window;
import jonl.jgl.lwjgl.ALDevice;
import jonl.jgl.lwjgl.GLFWWindow;

public abstract class Application implements App {

    private String title = "Application";
    private int width = 1024;
    private int height = 576;
    private boolean fullscreen = false;
    private boolean resizable = false;
    
    private Window window;
    
    private AudioDevice audio;
    @SuppressWarnings("unused") //TODO handle al
    private AudioLibrary al;
    
    private Input input;
    private Updater updater;
    private Renderer renderer;
    
    private ArrayList<Scene> scenes = new ArrayList<>();
    private int currentScene = 0;
    
    @Override
    public void add(Scene scene) {
        scene.application = this;
        scenes.add(scene);
    }
    
    @Override
    public void start() {
        
        prepare();
        
        window = new GLFWWindow(title,width,height,fullscreen,resizable,true,4,true);
        
        width = window.getWidth();
        height = window.getHeight();
        
        audio = new ALDevice();
        al = audio.getAudioLibrary();
        
        input = new AppInput(window.getInput());
        updater = new AppUpdater();
        renderer = new AppRenderer(this,window.getGraphicsLibrary());
        
        window.setLoader(()->{
            updater.load();
            renderer.load();
            Scene scene = scenes.get(currentScene);
            scene.create();
            updater.update(scene);
            renderer.render(scene);
        });
        
        window.setRunner(()->{
            while (window.isRunning()) {
                Scene scene = scenes.get(currentScene);
                updater.update(scene);
                renderer.render(scene);
            }
        });
        
        window.setCloser(()->{
            audio.destroy();
        });
        
        window.addSizeListener((w,h,pw,ph)->{
            width = w;
            height = h;
        });
        
        audio.create();
        window.start();
        
    }

    @Override
    public void close() {
        window.close();
    }
    
    @Override
    public Input getInput() {
        return input;
    }
    
    @Override
    public CursorState getCursorState() {
        return CursorState.state(window.getCursorState());
    }
    
    @Override
    public void setCursorState(CursorState state) {
        window.setCursorState(state.state);
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        if (window!=null) {
            window.setTitle(title);
        }
    }
    
    @Override
    public int[] getSize() {
        return new int[]{width,height};
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
        if (window!=null) return window.isResizable();
        return resizable;
    }

    @Override
    public boolean isFullscreen() {
        if (window!=null) return window.isFullscreen();
        return fullscreen;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (window!=null) {
            window.setSize(this.width,this.height);
        }
    }

    @Override
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        if (window!=null) {
            //TODO app error
            Console.println("Cannot change resizable window property after creation");
        }
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if (window!=null) {
            //TODO app error
            Console.println("Cannot change fullscreen window property after creation");
        }
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
