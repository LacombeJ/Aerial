package jonl.aui.tea;

import jonl.aui.Widget;
import jonl.vmath.Mathi;

public class TScrollContent extends TWidget {

    TWidget scrollWidget;
    
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
    
    public static class TScrollContentLayout extends TLayout {

        @Override
        void layout() {
            if (!isEmpty()) {
                TScrollContent scrollContent = (TScrollContent)parent;
                TScrollPanel scrollPanel = (TScrollPanel)parent.parent();
                
                int scrollWidth = scrollContent.width;
                int scrollHeight = scrollContent.height;
                
                int prefWidth = TLayoutManager.freeWidth(scrollContent.widget());
                int prefHeight = TLayoutManager.freeHeight(scrollContent.widget());
                
                int contentWidth = Math.max(prefWidth, scrollWidth);
                int contentHeight = Math.max(prefHeight, scrollHeight);
                
                int diffX = prefWidth - scrollWidth;
                int diffY = prefHeight - scrollHeight;
                int x = (int) ((scrollPanel.horBar.value()/100f) * diffX);
                int y = (int) ((scrollPanel.verBar.value()/100f) * diffY);
                
                x = Mathi.clamp(x, 0, Integer.MAX_VALUE);
                y = Mathi.clamp(y, 0, Integer.MAX_VALUE);
                
                TLayoutManager.setPositionAndSize(scrollContent.widget(), -x, -y, contentWidth, contentHeight);
            }
        }

        @Override
        TSizeHint calculateSizeHint() {
            TSizeHint hint = new TSizeHint();
            
            return hint;
        }
        
    }
    
}
