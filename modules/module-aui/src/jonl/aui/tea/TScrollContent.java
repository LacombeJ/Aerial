package jonl.aui.tea;

import jonl.aui.Signal;
import jonl.aui.Widget;
import jonl.aui.tea.event.TScrollEvent;
import jonl.jutils.func.Callback;
import jonl.vmath.Mathi;

public class TScrollContent extends TWidget {

    TWidget scrollWidget;
    
    private Signal<Callback<Integer>> scrolled = new Signal<>();
    
    public TScrollContent() {
        super();
        setWidgetLayout(new TScrollContentLayout());
    }
    
    public TWidget widget() {
        return scrollWidget;
    }
    
    public void setWidget(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (this.scrollWidget != null) {
            widgetLayout().remove(this.scrollWidget);
        }
        this.scrollWidget = tw;
        widgetLayout().add(tw);
    }
    
    public Signal<Callback<Integer>> scrolled() { return scrolled; }
    
    @Override
    protected boolean handleScroll(TScrollEvent scroll) {
        scrolled.emit((cb)->cb.f(scroll.sy));
        return true;
    }
    
    public static class TScrollContentLayout extends TLayout {

        @Override
        public void layout() {
            if (!isEmpty()) {
                TScrollContent scrollContent = (TScrollContent)parent;
                TScrollPanel scrollPanel = (TScrollPanel)parent.parent();
                
                int scrollWidth = scrollContent.width;
                int scrollHeight = scrollContent.height;
                
                int prefWidth = freeWidth(scrollContent.widget());
                int prefHeight = freeHeight(scrollContent.widget());
                
                int contentWidth = Math.max(prefWidth, scrollWidth);
                int contentHeight = Math.max(prefHeight, scrollHeight);
                
                int diffX = prefWidth - scrollWidth;
                int diffY = prefHeight - scrollHeight;
                int x = (int) ((scrollPanel.horBar.value()/100f) * diffX);
                int y = (int) ((scrollPanel.verBar.value()/100f) * diffY);
                
                x = Mathi.clamp(x, 0, Integer.MAX_VALUE);
                y = Mathi.clamp(y, 0, Integer.MAX_VALUE);
                
                setPositionAndSize(scrollContent.widget(), -x, -y, contentWidth, contentHeight);
            }
        }

        @Override
        public TSizeHint calculateSizeHint() {
            TSizeHint hint = new TSizeHint();
            
            return hint;
        }
        
    }
    
}
