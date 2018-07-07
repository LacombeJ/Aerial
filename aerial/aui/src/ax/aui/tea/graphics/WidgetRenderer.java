package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TGraphics;
import ax.aui.tea.TWidget;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;

public class WidgetRenderer {

    public static void paint(TWidget widget, String selector, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), selector);
        style = JSS.name(widget,style,g);
        
        TBox box = new TBox(0,0,widget.width(),widget.height());
        
        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
    }
    
    public static void paint(TWidget widget, TGraphics g, Info info) {
        paint(widget,"Widget",g,info);
    }
    
}
