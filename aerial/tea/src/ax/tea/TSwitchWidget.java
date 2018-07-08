package ax.tea;

import java.util.ArrayList;

import ax.aui.LayoutItem;
import ax.aui.SwitchWidget;
import ax.aui.Widget;
import ax.math.vector.Mathi;

public class TSwitchWidget extends TWidget implements SwitchWidget {

    private int index = 0;
    private ArrayList<TWidget> widgets;
    
    public TSwitchWidget() {
        widgets = new ArrayList<>();
        
        TSwitchLayout layout = new TSwitchLayout();
        setWidgetLayout(layout);
    }
    
    private void refreshContent() {
        if (index < count()) {
            TWidget nextWidget = widgets.get(index);
            if (widgetLayout().count()!=0) {
                TWidget prevWidget = widgetLayout().getWidget();
                if (prevWidget != nextWidget) {
                    widgetLayout().setWidget(nextWidget);
                }
            } else {
                widgetLayout().setWidget(nextWidget);
            }
        }
    }
    
    @Override
    public TWidget current() {
        return widgets.get(index);
    }
    
    @Override
    public TWidget get(int index) {
        return widgets.get(index);
    }

    @Override
    public void add(Widget widget) {
        widgets.add((TWidget) widget);
        refreshContent();
    }

    @Override
    public void remove(Widget widget) {
        int i = indexOf(widget);
        if (i!=-1) {
            remove(i);
        }
    }

    @Override
    public void remove(int index) {
        widgets.remove(index);
        refreshContent();
    }

    @Override
    public int count() {
        return widgets.size();
    }

    @Override
    public int indexOf(Widget widget) {
        return widgets.indexOf(widget);
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
        refreshContent();
    }
    
    @Override
    public void setWidget(Widget widget) {
        setIndex(indexOf(widget));
    }
    
    
    class TSwitchLayout extends TLayout {
        
        public TSwitchLayout() {
            super();
            setMargin(0, 0, 0, 0);
        }
        
        @Override
        public void layout() {
            if (widgets.size()!=0) {
                int width = parent.width() - margin().left - margin().right;
                int height = parent.height() - margin().bottom - margin().top;
                
                int sx = margin().left;
                int sy = margin().top;
                
                for (LayoutItem layoutItem : items()) {
                    TLayoutItem item = (TLayoutItem)layoutItem;
                    if (item instanceof TWidgetItem) {
                        int wWidth = allocate(getWidthPreference(item), width);
                        int wHeight = allocate(getHeightPreference(item), height);
                        setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
                    }
                }
            }
        }
        
        @Override
        public TSizeHint calculateSizeHint() {
            int maxWidth = 0;
            int maxHeight = 0;
            
            for (TWidget widget : widgets) {
                
                int width = freeAllocate(getWidthPreference(widget));
                int height = freeAllocate(getHeightPreference(widget));
                
                width += margin().width();
                height += margin().height();
                
                maxWidth = Mathi.max(maxWidth, width);
                maxHeight = Mathi.max(maxHeight,height);
                
            }
            
            return new TSizeHint(maxWidth, maxHeight);
        }

    }
    
}
