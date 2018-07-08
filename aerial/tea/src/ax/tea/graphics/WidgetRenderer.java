package ax.tea.graphics;

import ax.aui.Info;
import ax.commons.jss.Style;
import ax.tea.TGraphics;
import ax.tea.TWidget;
import ax.tea.spatial.TBox;

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
