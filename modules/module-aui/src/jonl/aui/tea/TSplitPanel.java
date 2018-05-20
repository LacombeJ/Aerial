package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Signal;
import jonl.aui.SplitPanel;
import jonl.aui.Widget;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.SplitPanelRenderer;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.vmath.Mathd;
import jonl.vmath.Mathi;

public class TSplitPanel extends TWidget implements SplitPanel {
    
    private TSplitLayout layout;
    
    private TWidget widgetOne;
    private TWidget widgetTwo;
    
    private Align align = Align.HORIZONTAL;
    
    private int splitPos = 0;
    
    private double ratio = 0.5f;
    private boolean ratioSet = false;
    
    Signal<Callback<Integer>> moved = new Signal<>();
    
    private boolean inAdjustState = false;
    
    
    public TSplitPanel() {
        super();
        setMouseFocusSupport(true);
        //setMouseMotionBounds(true); //TODO why did we add this, try another way, interferes with other widgets
        layout = new TSplitLayout();
        setWidgetLayout(layout);
    }
    
    public TSplitPanel(Align align) {
        this();
        setAlign(align);
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
        setRatio(ratio);
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
        widgetOne = tw;
        widgetLayout().add(widget);
    }

    @Override
    public void setWidgetTwo(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (widgetTwo != null) {
            widgetLayout().remove(widgetTwo);
        }
        widgetTwo = tw;
        widgetLayout().add(widget);
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
        if (this.align != align) {
            this.align = align;
            invalidateSizeHint();
            invalidateLayout();
        }
    }

    @Override
    public int pos() {
        return splitPos;
    }
    
    @Override
    public void setPos(int pos) {
        if (pos != splitPos) {
            splitPos = pos;
            moved().emit((cb)->cb.f(splitPos)); //TODO
            invalidateSizeHint();
            invalidateLayout();
        }
    }
    
    @Override
    public void setRatio(double ratio) {
        this.ratio = ratio;
        ratioSet = false;
        invalidateSizeHint();
        invalidateLayout();
    }
    
    @Override
    public Signal<Callback<Integer>> moved() {
        return moved;
    }
    
    // ------------------------------------------------------------------------
    
    @Override
    protected void paint(TGraphics g) {
        info().put("iSpacing",widgetLayout().spacing());
        SplitPanelRenderer.paint(this,g,info());
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
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT && isOnBorder(event.x,event.y)) {
            inAdjustState = true;
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = false;
            setCursor(TCursor.ARROW);
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseExit(TMouseEvent event) {
        setCursor(TCursor.ARROW);
        return true;
    }
    
    @Override
    protected boolean handleMouseMove(TMouseEvent event) {
        if (isOnBorder(event.x,event.y)) {
            setCursor(align==Align.HORIZONTAL ? TCursor.HRESIZE : TCursor.VRESIZE);
        } else if (!inAdjustState) {
            setCursor(TCursor.ARROW);
        }
        if (inAdjustState) {
            switch (align) {
            case HORIZONTAL:
                int w1Width = TLayoutManager.freeWidth(widgetOne);
                int w2Width = TLayoutManager.freeWidth(widgetTwo);
                int posH = event.x-layout.spacing()/2;
                posH = Mathi.clamp(posH,w1Width,width-layout.spacing()-w2Width);
                setPos(posH);
                break;
            case VERTICAL:
                int w1Height = TLayoutManager.freeHeight(widgetOne);
                int w2Height = TLayoutManager.freeHeight(widgetTwo);
                int posV = event.y-layout.spacing()/2;
                posV = Mathi.clamp(posV,w1Height,height-layout.spacing()-w2Height);
                setPos(posV);
                break;
            }
            return true;
        }
        return false;
    }
    
    int splitPosFromRatio(double ratio) {
        ratio = Mathd.clamp(ratio,0,1);
        switch (align) {
        case HORIZONTAL:
            int w1Width = TLayoutManager.freeWidth(widgetOne);
            int w2Width = TLayoutManager.freeWidth(widgetTwo);
            
            int midWidth = width - w1Width - w2Width - layout.spacing();
            int midX = (int) (ratio * midWidth) + w1Width;
            return Mathi.clamp(midX,w1Width,width-layout.spacing()-w2Width);
        case VERTICAL:
            int w1Height = TLayoutManager.freeHeight(widgetOne);
            int w2Height = TLayoutManager.freeHeight(widgetTwo);
            
            int midHeight = height - w1Height - w2Height - layout.spacing();
            int midY = (int) (ratio * midHeight) + w1Height;
            return Mathi.clamp(midY,w1Height,height-layout.spacing()-w2Height);
        }
        return 0;
    }
    
    void setRatioInternal() {
      setPos(splitPosFromRatio(ratio));
      ratioSet = true;
    }
    
    
    
    static class TSplitLayout extends TLayout {
        
        @Override
        public void layout() {
            TSplitPanel splitPane = (TSplitPanel) parent;
            
            TWidget w1 = splitPane.widgetOne();
            TWidget w2 = splitPane.widgetTwo();
            
            if (w1 != null && w2 != null) {
                int width = splitPane.width();
                int height = splitPane.height();
                
                if (!splitPane.ratioSet) {
                    splitPane.setRatioInternal();
                }
                
                // Using free allocate to get the minimum/preferred widths
                // Since the split pane can be adjustable free allocate enables us to get
                // adjustable sizes.
                switch (splitPane.align()) {
                case HORIZONTAL:
                    int midX = splitPane.pos();
                    int width1 = midX;
                    int width2 = width - (midX + spacing());
                    setPositionAndSize(w1, 0, 0, width1, height);
                    setPositionAndSize(w2, width-width2, 0, width2, height);
                    
                    break;
                case VERTICAL:
                    int midY = splitPane.pos();
                    int height1 = midY;
                    int height2 = height - (midY + spacing());
                    setPositionAndSize(w1, 0, 0, width, height1);
                    setPositionAndSize(w2, 0, height-height2, width, height2);
                    break;
                }
            }
        }
        
        @Override
        public TSizeHint calculateSizeHint() {
            TSplitPanel splitPane = (TSplitPanel) parent;
            
            if (splitPane.align() == Align.HORIZONTAL) {
                int width = freeAllocate(getWidthPreferences());
                int height = freeMaxAllocate(getHeightPreferences());
                width += spacing();
                return new TSizeHint(width,height);
            } else {
                int width = freeMaxAllocate(getWidthPreferences());
                int height = freeAllocate(getHeightPreferences());
                height += spacing();
                return new TSizeHint(width,height);
            }
        }
        
        
    }
    
    

}
