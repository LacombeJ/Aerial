package jonl.aui;

import jonl.jutils.func.Callback;
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
    
    <T> void emit(Signal<T> signal, Callback<T> cb);
    <T> void connect(Signal<T> signal, T slot);
    
    Signal<Painter> paint();
    Signal<Int2ChangedListener> positionChanged();
    Signal<Int2ChangedListener> sizeChanged();
    Signal<MouseMotionListener> mouseMoved();
    Signal<MouseButtonListener> mouseButton();
    Signal<MouseMotionListener> globalMouseMoved();
    Signal<MouseButtonListener> globalMouseButton();
    
}
