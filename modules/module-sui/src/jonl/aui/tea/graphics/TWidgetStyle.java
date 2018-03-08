package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TSizePolicy;
import jonl.aui.tea.TWidget;
import jonl.jutils.func.Callback3D;
import jonl.jutils.func.Function2D;

public abstract class TWidgetStyle<T extends TWidget> {

    private TStyle style = null;
    
    private Callback3D<T,TWidgetInfo,TGraphics> painter = (w,i,g) -> { };
    
    private Function2D<T,TWidgetInfo,TSizePolicy> sizePolicy = (w,i) -> new TSizePolicy();
    
    public TWidgetStyle(TStyle style) {
        this.style = style;
    }
    
    public TStyle style() {
        return style;
    }
    
    public void setPainter(Callback3D<T,TWidgetInfo,TGraphics> painter) {
        this.painter = painter;
    }
    
    /**
     * This function should only be called internally by the widget
     * itself in its paint method
     */
    public void paint(T widget, TWidgetInfo info, TGraphics g) {
        painter.f(widget, info, g);
    }
    
    public void setSizePolicy(Function2D<T,TWidgetInfo,TSizePolicy> sizePolicy) {
        this.sizePolicy = sizePolicy;
    }
    
    public TSizePolicy getSizePolicy(T widget, TWidgetInfo info) {
        return sizePolicy.f(widget, info);
    }
    
}
