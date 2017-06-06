package jonl.aui;

public interface Widget {

    Widget getRoot();
    Container getParent();
    
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    
    int getWindowX();
    int getWindowY();
    
    int getScreenX();
    int getScreenY();
    
    /** @return whether this point is within the local widget bounds */
    boolean isWithin(int x, int y);
    
    void addPainter(Painter p);
    void removePainter(Painter p);
    Painter[] getPainters();
    
    void addPositionChangedListener(Int2ChangedListener pc);
    void removePositionChangedListener(Int2ChangedListener pc);
    Int2ChangedListener[] getPositionChangedListeners();
    
    void addSizeChangedListener(Int2ChangedListener sc);
    void removeSizeChangedListener(Int2ChangedListener sc);
    Int2ChangedListener[] getSizeChangedListeners();
    
    void addMouseListener(MouseMotionListener m);
    void removeMouseListener(MouseMotionListener m);
    MouseMotionListener[] getMouseListeners();
    
    void addMouseButtonListener(MouseButtonListener m);
    void removeMouseButtonListener(MouseButtonListener m);
    MouseButtonListener[] getMouseButtonListeners();
    
    void addGlobalMouseListener(MouseButtonListener m);
    void removeGlobalMouseListener(MouseButtonListener m);
    MouseButtonListener[] getGlobalMouseListeners();
    
    void addGlobalMousePositionChangedListener(Int2ChangedListener mpc);
    void removeGlobalMousePositionChangedListener(Int2ChangedListener mpc);
    Int2ChangedListener[] getGlobalMousePositionListeners();
    
}
