package jonl.aui.tea;

import jonl.aui.Frame;
import jonl.aui.Margin;
import jonl.aui.Widget;
import jonl.aui.tea.call.TArgTypes;
import jonl.aui.tea.graphics.TColor;
import jonl.jutils.io.Console;

public class TFrame extends TWindow implements Frame {
    
    TFrameBar frameBar;
    
    Margin defaultInsets;
    Margin insets;
    
    TColor color = TColor.fromFloat(0.5f, 0.45f, 0.45f);
    
    TFrame() {
        super(new TFrameLayout());
        setMouseFocusSupport(true);
        setDecorated(false);
        TFrameLayout layout = (TFrameLayout) widgetLayout();
        defaultInsets = new Margin(3,3,27,3);
        insets = new Margin(defaultInsets);
        
        frameBar = new TFrameBar(this);
        layout.add(frameBar);
        
        caller().implement("SET_COLOR", (args) -> {
            color = TArgTypes.color(args);
            return true;
        });
        
    }
    
    TFrameBar frameBar() {
        return frameBar;
    }

    @Override
    public void setWidget(Widget widget) {
        if (widget()!=null) {
            widgetLayout().remove(this.widget);
        }
        this.widget = widget;
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
    public void paint(TGraphics g) {
        g.renderRect(0,0,width(),height(),color);
        paint().emit(cb->cb.f(g));
    }

}
