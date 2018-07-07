package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TFrame;
import ax.aui.tea.TGraphics;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;

public class FrameRenderer {

    public static void paint(TFrame widget, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), "Frame");
        style = JSS.name(widget,style,g);
        
        Style windowStyle = JSS.style(g.style(), "Window");
        
        TBox box = new TBox(0,0,widget.width(),widget.height());
        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
        
        if (JSS.asBoolean(style.get("window-override"))) {
            TBox inner = new TBox(
                widget.insets().left,
                widget.insets().top,
                widget.width()-widget.insets().width(),
                widget.height()-widget.insets().height());
            
            BoxRenderer.paint(windowStyle, inner, g);        
        }
    }

}
