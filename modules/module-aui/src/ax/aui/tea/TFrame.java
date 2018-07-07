package ax.aui.tea;

import ax.aui.Frame;
import ax.aui.Margin;
import ax.aui.Widget;
import ax.aui.tea.graphics.FrameRenderer;

public class TFrame extends TWindow implements Frame {
    
    TFrameBar frameBar;
    
    Margin defaultInsets;
    Margin insets;
    
    TFrame() {
        super(new TFrameLayout());
        setMouseFocusSupport(true);
        setDecorated(false);
        TFrameLayout layout = (TFrameLayout) widgetLayout();
        defaultInsets = new Margin(3,3,27,3);
        insets = new Margin(defaultInsets);
        
        frameBar = new TFrameBar(this);
        layout.add(frameBar);
    }
    
    TFrameBar frameBar() {
        return frameBar;
    }

    @Override
    public void setWidget(Widget widget) {
        if (widget()!=null) {
            widgetLayout().remove(this.widget);
        }
        this.widget = (TWidget) widget;
        widgetLayout().add(this.widget);
    }
    
    @Override
    public Margin insets() {
        return insets;
    }

    @Override
    public void setInsets(int left, int right, int top, int bottom) {
        insets = new Margin(left,right,top,bottom);
    }

    @Override
    public void setInsets(Margin margin) {
        insets = margin;
    }
    
    @Override
    protected void paint(TGraphics g) {
        FrameRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }

}
