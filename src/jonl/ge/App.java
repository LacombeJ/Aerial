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
    
    String getTitle();
    int[] getSize();
    int getWidth();
    int getHeight();
    boolean isResizable();
    boolean isFullscreen();
    
    void setTitle(String title);
    void setSize(int width, int height);
    void setResizable(boolean resizable);
    void setFullscreen(boolean fullscreen);
    
    Updater getUpdater(); 
    Renderer getRenderer();
    
    
}
