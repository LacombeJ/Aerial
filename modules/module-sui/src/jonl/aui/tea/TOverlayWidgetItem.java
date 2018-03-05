package jonl.aui.tea;

import jonl.aui.Widget;

public class TOverlayWidgetItem extends TWidgetItem {

    int x;
    int y;
    int width;
    int height;
    
    public TOverlayWidgetItem(Widget widget) {
        super(widget);
    }
    
    public TOverlayWidgetItem(Widget widget, int x, int y) {
        this(widget);
        this.x = x;
        this.y = y;
    }
    
    public TOverlayWidgetItem(Widget widget, int x, int y, int width, int height) {
        this(widget);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

}
