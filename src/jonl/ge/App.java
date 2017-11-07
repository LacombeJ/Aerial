package jonl.ge;

import jonl.ge.Input.CursorState;

interface App {
    
    void add(Scene scene);
    void prepare();
    
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
    
    Platform getPlatform();
    String getInfo(String key);
    Time getTime();
    
    Updater getUpdater(); 
    Renderer getRenderer();
    
}
