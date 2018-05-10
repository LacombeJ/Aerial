package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.ScrollPanel;
import jonl.aui.Signal;
import jonl.aui.Widget;
import jonl.aui.tea.event.TScrollEvent;
import jonl.jutils.func.Callback;
import jonl.vmath.Mathd;

public class TScrollPanel extends TWidget implements ScrollPanel {

    TScrollPanelArea content; 
    TScrollBar horBar;
    TScrollBar verBar;
    
    public TScrollPanel(boolean lockWidth, boolean lockHeight) {
        super();
        content = new TScrollPanelArea(lockWidth,lockHeight);
        horBar = new TScrollBar(Align.HORIZONTAL);
        verBar = new TScrollBar(Align.VERTICAL);
        setWidgetLayout(new TScrollLayout());
        widgetLayout().add(content);
        widgetLayout().add(horBar);
        widgetLayout().add(verBar);
        
        content.layoutCb = (size) -> {
            int diffX = size.width - content.width;
            int diffY = size.height - content.height;
            int x = (int) ((horBar.value()/100f) * diffX);
            int y = (int) ((verBar.value()/100f) * diffY);
            content.setScrollX(x);
            content.setScrollY(y);
        };
        
        content.scrolled().connect((sy)->{
            verBar.setValue(verBar.value() - sy*10);
            content.invalidateLayout();
        });
        horBar.scrolled().connect((sy)->{
            horBar.setValue(horBar.value() - sy*10);
            content.invalidateLayout();
        });
        verBar.scrolled().connect((sy)->{
            verBar.setValue(verBar.value() - sy*10);
            content.invalidateLayout();
        });
        
        horBar.moved().connect((value)->{
            content.invalidateLayout();
        });
        verBar.moved().connect((value)->{
            content.invalidateLayout();
        });
    }
    
    public TScrollPanel() {
        this(false,false);
    }
    
    @Override
    public TWidget widget() {
        return content.scrollWidget();
    }

    @Override
    public void setWidget(Widget widget) {
        content.setScrollWidget(widget);
        invalidateLayout();
    }
    
    void updateScrollPanel() {
        if (widget()!=null) {
            
            int horWidth = 0;
            if (content.width>=widget().width) {
                horWidth = content.width;
            } else {
                double horRatio = (double)(content.width) / widget().width();
                horRatio = Mathd.clamp(horRatio,0,1);
                horWidth = (int) Mathd.lerp(horRatio,horBar.height,content.width);
            }
            
            int verHeight = 0;
            if (content.height>=widget().height) {
                verHeight = content.height;
            } else {
                double verRatio = (double)(content.height) / widget().height();
                verRatio = Mathd.clamp(verRatio,0,1);
                verHeight = (int) Mathd.lerp(verRatio,verBar.width,content.height);
            }
            
            horBar.setBarSize(horWidth);
            verBar.setBarSize(verHeight);
            
        }
    }
    
    static class TScrollPanelArea extends TScrollArea {
        private Signal<Callback<Integer>> scrolled = new Signal<>();
        public Signal<Callback<Integer>> scrolled() { return scrolled; }
        public TScrollPanelArea(boolean lockWidth, boolean lockHeight) {
            super(lockWidth,lockHeight);
        }
        @Override
        protected boolean handleScroll(TScrollEvent scroll) {
            scrolled.emit((cb)->cb.f(scroll.sy));
            return true;
        }
    }
    
    static class TScrollLayout extends TLayout {
        
        @Override
        public void layout() {
            TScrollPanel scrollPanel = (TScrollPanel)parent;
            
            if (scrollPanel.content.scrollWidget()==null) return;
            
            int width = parent.width;
            int height = parent.height;
            
            int contentWidgetWidth = freeWidth(scrollPanel.content.scrollWidget());
            int contentWidgetHeight = freeHeight(scrollPanel.content.scrollWidget());
            
            int horHeight = freeHeight(scrollPanel.horBar);
            int verWidth = freeWidth(scrollPanel.verBar);
            
            boolean hasHorBar = false;
            boolean hasVerBar = false;
            
            if (contentWidgetWidth > width) {
                hasHorBar = true;
            }
            
            if (contentWidgetHeight > height) {
                hasVerBar = true;
            }
            
            if (!hasHorBar) {
                horHeight = 0;
            }
            
            if (!hasVerBar) {
                verWidth = 0;
            }
            
            if (hasHorBar && !contains(scrollPanel.horBar)) {
                addNoInvalidate(scrollPanel.horBar);
            } else if (!hasHorBar && contains(scrollPanel.horBar)) {
                removeNoInvalidate(scrollPanel.horBar);
            }
            
            if (hasVerBar && !contains(scrollPanel.verBar)) {
                addNoInvalidate(scrollPanel.verBar);
            } else if (!hasVerBar && contains(scrollPanel.verBar)) {
                removeNoInvalidate(scrollPanel.verBar);
            }
                
            if (hasHorBar) {
                setPositionAndSize(scrollPanel.horBar, 0, height - horHeight, width - verWidth, horHeight );
            }
            
            if (hasVerBar) {
                setPositionAndSize(scrollPanel.verBar, width - verWidth, 0, verWidth, height - horHeight);
            }
            
            setPositionAndSize(scrollPanel.content, 0, 0, width - verWidth, height - horHeight);
            
            scrollPanel.updateScrollPanel();
        }

        @Override
        public TSizeHint calculateSizeHint() {
            TScrollPanel scrollPanel = (TScrollPanel)parent;
            
            int width = freeWidth(scrollPanel.verBar);
            int height = freeHeight(scrollPanel.horBar);
            
            if (scrollPanel.content!=null) {
                width += freeWidth(scrollPanel.content);
                height += freeHeight(scrollPanel.content);
            }
            
            return new TSizeHint(width, height);
        }
        
    }

}
