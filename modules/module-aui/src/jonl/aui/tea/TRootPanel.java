package jonl.aui.tea;

import jonl.aui.LayoutItem;
import jonl.aui.Widget;
import jonl.aui.tea.event.TMouseEvent;

public class TRootPanel extends TWidget {

    public TRootPanel() {
        super();
        setWidgetLayout(new TRootLayout());
    }
    
    public Widget get(int index) {
        return widgetLayout().getWidget(index);
    }

    public void add(Widget widget) {
        widgetLayout().add(new TRootItem(widget));
    }

    public void add(Widget widget, int x, int y) {
        widgetLayout().add(new TRootItem(widget,x,y));
        
    }
    
    public void add(Widget widget, int x, int y, Widget relative) {
        int px = relative.windowX() + x;
        int py = relative.windowY() + y;
            
        widgetLayout().add(new TRootItem(widget,px,py));
        
    }
    
    public void remove(Widget widget) {
        widgetLayout().remove(widget);
    }
    
    
    
    @Override
    protected void paint(TGraphics g) {
        
        paint().emit(cb->cb.f(g));
    }
    
    private boolean activated() {
        return false;
        
        //TODO re-enable this logic when we can properly handle mouse motion events
        //by keeping track of the mouse and sending correct enter/exit commands
        //when mouse regains focus
        
        /*
        boolean empty = widgetLayout().isEmpty();
        if (empty) {
            return false;
        }
        return true;
        */
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        boolean empty = widgetLayout().isEmpty();
        
        widgetLayout().removeAll();
        
        //If empty, we return false so other widgets can handle mouse events
        if (empty) {
            return false;
        }
        
        //If was not empty, this event is handled, mouse press events don't get passed on to other widgets
        return true;
    }
    
    @Override
    protected boolean handleMouseButtonClick(TMouseEvent event) {
        return activated();
    }
    
    @Override
    protected boolean handleMouseButtonRelease(TMouseEvent event) {
        return activated();
    }
    
    @Override
    protected boolean handleMouseEnter(TMouseEvent event) {
        return activated();
    }
    
    @Override
    protected boolean handleMouseExit(TMouseEvent event) {
        return activated();
    }
    
    @Override
    protected boolean handleMouseMove(TMouseEvent event) {
        return activated();
    }
    
    
    class TRootItem extends TWidgetItem {

        int x;
        int y;
        
        public TRootItem(Widget widget) {
            super(widget);
        }
        
        public TRootItem(Widget widget, int x, int y) {
            super(widget);
            this.x = x;
            this.y = y;
        }
        
    }
    
    class TRootLayout extends TLayout {

        @Override
        public void layout() {
            int width = parent.width();
            int height = parent.height();
            
            for (LayoutItem layoutItem : items()) {
                TRootItem item = (TRootItem)layoutItem;
                
                int widgetX = item.x;
                int widgetY = item.y;
                int widgetWidth = this.freeWidth(item);
                int widgetHeight = this.freeHeight(item);
                
                if (widgetX+widgetWidth > width) {
                    widgetX = width-widgetWidth;
                }
                if (widgetY+widgetHeight > height) {
                    widgetY = height-widgetHeight;
                }
                if (widgetX < 0) {
                    widgetX = 0;
                }
                if (widgetY < 0) {
                    widgetY = 0;
                }
                
                // Set the internal positions to clamped positions
                item.x = widgetX;
                item.y = widgetY;
                
                setPositionAndSize(item.widget(), widgetX, widgetY, widgetWidth, widgetHeight);
            }
        }

        @Override
        public TSizeHint calculateSizeHint() {
            return new TSizeHint();
        }
        
    }
    
}
