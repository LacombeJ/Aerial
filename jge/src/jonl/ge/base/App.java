package jonl.ge.base;

import jonl.ge.core.Input;
import jonl.ge.core.Module;
import jonl.ge.core.Platform;
import jonl.ge.core.Scene;
import jonl.ge.core.Time;
import jonl.ge.core.Window;
import jonl.ge.core.Input.CursorState;
import jonl.jutils.func.Callback0D;

public interface App {
    
    void add(Scene scene);
    
    void removeModule(String name);
    void addModule(String name, Module module);
    
    void start();
    void close();
    
    Input getInput();
    CursorState getCursorState();
    void setCursorState(CursorState state);
    
    Window getWindow();
    
    String getTitle();
    int getWidth();
    int getHeight();
    boolean isResizable();
    boolean isFullscreen();
    
    void setTitle(String title);
    void setSize(int width, int height);
    void setResizable(boolean resizable);
    void setFullscreen(boolean fullscreen);
    
    void setLoader(Callback0D load);
    void setUpdater(Callback0D update);
    void setCloser(Callback0D close);
    
    Platform getPlatform();
    String getInfo(String key);
    Time getTime();
    
    Updater getUpdater(); 
    Renderer getRenderer();
    
}
