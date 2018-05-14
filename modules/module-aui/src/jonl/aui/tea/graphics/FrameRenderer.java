package jonl.aui.tea.graphics;

import jonl.aui.Info;
import jonl.aui.tea.TFrame;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class FrameRenderer {

    public static void paint(TFrame widget, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), "Frame");
        style = JSS.name(widget,style,g);
        
        Style windowStyle = JSS.style(g.style(), "Window");
        
        TBox box = new TBox(0,0,widget.width(),widget.height());
        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
        
        if (JSS.asBoolean("window-override")) {
            TBox inner = new TBox(
                widget.insets().left,
                widget.insets().top,
                widget.width()-widget.insets().width(),
                widget.height()-widget.insets().height());
            
            BoxRenderer.paint(windowStyle, inner, g);        
        }
    }

}
