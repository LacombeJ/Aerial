package ax.tea;

import ax.aui.HAlign;
import ax.aui.Justify;
import ax.aui.LayoutItem;
import ax.aui.Overlay;
import ax.aui.VAlign;
import ax.aui.Widget;

public class TOverlay extends TWidget implements Overlay {
    
    public TOverlay() {
        super();
        setWidgetLayout(new TOverlayLayout());
    }
    
    @Override
    public Widget get(int index) {
        return widgetLayout().getWidget(index);
    }

    @Override
    public void add(Widget widget) {
        widgetLayout().add(new TOverlayWidgetItem(widget));
    }

    @Override
    public void add(Widget widget, int x, int y) {
        widgetLayout().add(new TOverlayWidgetItem(widget,x,y));
        
    }

    @Override
    public void add(Widget widget, int x, int y, int width, int height) {
        widgetLayout().add(new TOverlayWidgetItem(widget,x,y,width,height));
    }
    
    @Override
    public void add(Widget widget, int x, int y, int width, int height, Justify justify) {
        widgetLayout().add(new TOverlayWidgetItem(widget,x,y,width,height,justify));
    }

    @Override
    public void remove(Widget widget) {
        widgetLayout().remove(widget);
    }

    @Override
    public void setPosition(int index, int x, int y) {
        TOverlayWidgetItem item = (TOverlayWidgetItem) widgetLayout().getItem(index);
        item.x = x;
        item.y = y;
        invalidateLayout();
    }

    @Override
    public void setSize(int index, int width, int y) {
        TOverlayWidgetItem item = (TOverlayWidgetItem) widgetLayout().getItem(index);
        item.width = width;
        item.height = height;
        invalidateLayout();
    }
    
    @Override
    protected void paint(TGraphics g) {
        
        paint().emit(cb->cb.f(g));
    }
    
    static class TOverlayLayout extends TLayout {
        @Override
        public void layout() {
            int width = parent.width;
            int height = parent.height;
            for (LayoutItem layoutItem : items()) {
                TOverlayWidgetItem item = (TOverlayWidgetItem)layoutItem;
                
                int x = 0;
                int y = 0;
                
                if (item.justify.halign()==HAlign.LEFT) {
                    x = item.x;
                } else if (item.justify.halign()==HAlign.CENTER) {
                    x = (width/2) - (item.width/2) + item.x;
                } else if (item.justify.halign()==HAlign.RIGHT) {
                    x = width - item.width - item.x;
                }
                
                if (item.justify.valign()==VAlign.TOP) {
                    y = item.y;
                } else if (item.justify.valign()==VAlign.MIDDLE) {
                    y = (height/2) - (item.height/2) + item.y;
                } else if (item.justify.valign()==VAlign.BOTTOM) {
                    y = height - item.height- item.y;
                }
                
                setPositionAndSize(item.widget(), x, y, item.width, item.height);
            }
        }
        @Override
        public TSizeHint calculateSizeHint() {
            return new TSizeHint();
        }
    }
    
    static class TOverlayWidgetItem extends TWidgetItem {
        int x;
        int y;
        int width;
        int height;
        Justify justify = Justify.TOP_LEFT;
        public TOverlayWidgetItem(Widget widget, int x, int y, int width, int height, Justify justify) {
            super(widget);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.justify = justify;
        }
        public TOverlayWidgetItem(Widget widget, int x, int y, int width, int height) {
            this(widget,x,y,width,height,Justify.TOP_LEFT);
        }
        public TOverlayWidgetItem(Widget widget, int x, int y) {
            this(widget,x,y,0,0);
        }
        public TOverlayWidgetItem(Widget widget) {
            this(widget,0,0);
        }
    }

}
