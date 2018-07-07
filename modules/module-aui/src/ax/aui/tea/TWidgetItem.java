package ax.aui.tea;

import ax.aui.SizePolicy;
import ax.aui.Widget;

public class TWidgetItem extends TLayoutItem {

    private TWidget widget;
    
    public TWidgetItem(Widget widget) {
        this.widget = (TWidget) widget;
    }
    
    public TWidget widget() {
        return widget;
    }

    @Override
    public int minWidth() {
        return widget.minWidth();
    }

    @Override
    public int minHeight() {
        return widget.minHeight();
    }

    @Override
    public int maxWidth() {
        return widget.maxWidth();
    }

    @Override
    public int maxHeight() {
        return widget.maxHeight();
    }
    
    @Override
    public SizePolicy sizePolicy() {
        return widget.sizePolicy();
    }
    
    @Override
    public int hintWidth() {
        return widget.sizeHint().width;
    }
    
    @Override
    public int hintHeight() {
        return widget.sizeHint().height;
    }
    
}
