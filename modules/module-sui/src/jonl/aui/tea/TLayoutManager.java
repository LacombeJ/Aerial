package jonl.aui.tea;

import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;

class TLayoutManager {

    static void setPositionAndRequestFire(TWidget w, int x, int y) {
        int prevX = w.x;
        int prevY = w.y;
        w.x = x;
        w.y = y;
        if (prevX!=x || prevY!=y) {
            TEventHandler.firePositionChanged(w,new TMoveEvent(TEventType.Move,x,y,prevX,prevY));
        }
    }
    
    static void setSizeAndRequestFire(TWidget w, int width, int height) {
        int prevWidth = w.width;
        int prevHeight = w.height;
        w.width = width;
        w.height = height;
        if (prevWidth!=width || prevHeight!=height) {
            TEventHandler.fireSizeChanged(w,new TResizeEvent(TEventType.Resize,width,height,prevWidth,prevHeight));
        }
    }
    
}
