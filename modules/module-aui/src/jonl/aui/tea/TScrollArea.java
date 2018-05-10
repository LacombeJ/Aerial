package jonl.aui.tea;

import jonl.aui.Widget;
import jonl.aui.tea.spatial.TSize;
import jonl.jutils.func.Callback;
import jonl.vmath.Mathi;

public class TScrollArea extends TWidget {

    protected TWidget scrollWidget;
    
    protected int scrollX = 0;
    protected int scrollY = 0;
    protected boolean lockWidth = false;
    protected boolean lockHeight = false;
    
    protected Callback<TSize> layoutCb = (size) -> {};
    
    public TScrollArea(boolean lockWidth, boolean lockHeight) {
        super();
        this.lockWidth = lockWidth;
        this.lockHeight = lockHeight;
        setWidgetLayout(new TScrollContentLayout());
    }
    
    public TScrollArea() {
        this(false,false);
    }
    
    protected TWidget scrollWidget() {
        return scrollWidget;
    }
    
    protected void setScrollWidget(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (this.scrollWidget != null) {
            widgetLayout().remove(this.scrollWidget);
        }
        this.scrollWidget = tw;
        widgetLayout().add(tw);
    }
    
    protected void setScrollX(int x) {
        this.scrollX = x;
    }
    
    protected void setScrollY(int y) {
        this.scrollY = y;
    }
    
    public static class TScrollContentLayout extends TLayout {

        @Override
        public void layout() {
            if (!isEmpty()) {
                TScrollArea scrollContent = (TScrollArea)parent;
                
                int prefWidth = freeWidth(scrollContent.scrollWidget());
                int prefHeight = freeHeight(scrollContent.scrollWidget());
                
                scrollContent.layoutCb.f(new TSize(prefWidth,prefHeight));
                
                int scrollWidth = scrollContent.width;
                int scrollHeight = scrollContent.height;
                
                int contentWidth = Math.max(prefWidth, scrollWidth);
                int contentHeight = Math.max(prefHeight, scrollHeight);
                
                if (scrollContent.lockWidth) {
                    contentWidth = prefWidth;
                }
                
                if (scrollContent.lockHeight) {
                    contentHeight = prefHeight;
                }
                
                int x = scrollContent.scrollX;
                int y = scrollContent.scrollY;
                
                x = Mathi.clamp(x, 0, Integer.MAX_VALUE);
                y = Mathi.clamp(y, 0, Integer.MAX_VALUE);
                
                setPositionAndSize(scrollContent.scrollWidget(), -x, -y, contentWidth, contentHeight);
                
                // Called because when content widget's size policy changes, layout manager
                // doesn't handle invalidating the scroll panel's layouts because this widget returns
                // the same size hint (ignoring the changed size hint of the content widget).
                // So we force a layout invalidation.
                if (scrollContent.parent()!=null) {
                    scrollContent.parent().invalidateLayout();
                }
            }
        }

        @Override
        public TSizeHint calculateSizeHint() {
            if (!isEmpty()) {
                TScrollArea scrollContent = (TScrollArea)parent;
                
                int prefWidth = 0;
                int prefHeight = 0;
                
                if (scrollContent.lockWidth) {
                    prefWidth = freeWidth(scrollContent.scrollWidget());
                }
                
                if (scrollContent.lockHeight) {
                    prefHeight = freeHeight(scrollContent.scrollWidget());
                }
                
                return new TSizeHint(prefWidth,prefHeight);
            }
            return new TSizeHint();
        }
        
    }
    
}
