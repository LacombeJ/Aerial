package jonl.aui.tea;

import jonl.aui.Overlay;
import jonl.aui.Widget;

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

}
