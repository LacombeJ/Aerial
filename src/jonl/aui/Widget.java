package jonl.aui;

import jonl.jutils.func.Tuple2i;

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
    
    /** @return local space coordinates from global space coordinates */
    Tuple2i fromGlobalSpace(int globalX, int globalY);
    /** @return global space coordinates from local space coordinates */
    Tuple2i toGlobalSpace(int localX, int localY);
    
    
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
    
    
    
    void addMouseMotionListener(MouseMotionListener m);
    void removeMouseMotionListener(MouseMotionListener m);
    MouseMotionListener[] getMouseMotionListener();
    
    void addMouseButtonListener(MouseButtonListener m);
    void removeMouseButtonListener(MouseButtonListener m);
    MouseButtonListener[] getMouseButtonListeners();
    
    
    
    void addGlobalMouseMotionListener(MouseMotionListener m);
    void removeGlobalMouseMotionListener(MouseMotionListener m);
    MouseMotionListener[] getGlobalMouseMotionListeners();
    
    void addGlobalMouseButtonListener(MouseButtonListener m);
    void removeGlobalMouseButtonListener(MouseButtonListener m);
    MouseButtonListener[] getGlobalMouseButtonListeners();
    
}
