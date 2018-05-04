package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWidget;
import jonl.aui.tea.TWidgetInfo;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class WidgetRenderer {

    public static void paint(TWidget widget, String selector, TGraphics g, TWidgetInfo info) {
        Style style = JSS.style(g.style(), selector);
        style = JSS.name(widget,style,g);
        
        TBox box = new TBox(0,0,widget.width(),widget.height());
        
        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
    }
    
    public static void paint(TWidget widget, TGraphics g, TWidgetInfo info) {
        paint(widget,"Widget",g,info);
    }
    
}
