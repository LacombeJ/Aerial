package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.SplitPanel;
import jonl.aui.Widget;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.vmath.Vector4;

public class TSplitPanel extends TWidget implements SplitPanel {
    
    private TWidget widgetOne;
    private TWidget widgetTwo;
    
    private Align align = Align.HORIZONTAL;
    
    private double ratio = 0.5f;
    
    private double min = 0.05;
    private boolean inAdjustState = false;
    
    public TSplitPanel() {
        super();
        setMouseFocusSupport(true);
        setWidgetLayout(new TSplitLayout());
    }
    
    public TSplitPanel(Widget w1, Widget w2, Align align, double ratio) {
        this();
        setSplit(w1,w2,align,ratio);
    }
    
    @Override
    public void setSplit(Widget w1, Widget w2, Align align, double ratio) {
        setWidgetOne(w1);
        setWidgetTwo(w2);
        this.align = align;
        this.ratio = ratio;
    }

    @Override
    public TWidget widgetOne() {
        return widgetOne;
    }

    @Override
    public TWidget widgetTwo() {
        return widgetTwo;
    }

    @Override
    public Widget[] widgets() {
        return new Widget[] { widgetOne, widgetTwo };
    }

    @Override
    public void setWidgetOne(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (widgetOne != null) {
            widgetLayout().remove(widgetOne);
        }
        widgetLayout().add(widget);
        widgetOne = tw;
    }

    @Override
    public void setWidgetTwo(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (widgetTwo != null) {
            widgetLayout().remove(widgetTwo);
        }
        widgetLayout().add(widget);
        widgetTwo = tw;
    }

    @Override
    public void setWidgets(Widget w1, Widget w2) {
        setWidgetOne(w1);
        setWidgetTwo(w2);
    }

    @Override
    public Align align() {
        return align;
    }

    @Override
    public void setAlign(Align align) {
        this.align = align;
    }

    @Override
    public double ratio() {
        return ratio;
    }

    @Override
    public void setRatio(double ratio) {
        this.ratio = ratio;
        layoutChildren();
    }
    
    // ------------------------------------------------------------------------
    
    @Override
    protected void paint(TGraphics g) {
        switch (align()) {
        case HORIZONTAL:
            int minX = widgetOne().width();
            g.renderRect(minX,0,widgetLayout().spacing(),height(),new Vector4(0.1f,0.1f,0.3f,1));
            break;
        case VERTICAL:
            int minY = widgetOne().height();
            g.renderRect(0,minY,width(),widgetLayout().spacing(),new Vector4(0.1f,0.1f,0.3f,1));
            break;
        }
        
        paint().emit(cb->cb.f(g));
    }
    
    private boolean isOnBorder(int x, int y) {
        switch (align) {
        case HORIZONTAL:
            int minX = widgetOne().width();
            int maxX = minX + widgetLayout().spacing();
            if (x>=minX && x<=maxX) {
                return true;
            }
            break;
        case VERTICAL:
            int minY = widgetOne().height();
            int maxY = minY + widgetLayout().spacing();
            if (y>=minY && y<=maxY) {
                return true;
            }
            break;
        }
        return false;
    }
    
    @Override
    protected void handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT && isOnBorder(event.x,event.y)) {
            inAdjustState = true;
        }
    }
    
    @Override
    protected void handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = false;
        }
    }
    
    @Override
    protected void handleMouseMove(TMouseEvent event) {
        double ratio = this.ratio;
        if (inAdjustState) {
            switch (align) {
            case HORIZONTAL:
                ratio = (double)event.globalX / width();
                break;
            case VERTICAL:
                ratio = (double)event.globalY / height();
                break;
            }
        }
        if (ratio<min) ratio = min;
        if (ratio>(1-min)) ratio = (1-min);
        setRatio(ratio);
    }

}
