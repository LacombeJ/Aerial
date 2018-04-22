package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TLabel;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class LabelRenderer {

    public static void paint(TLabel label, TGraphics g, TWidgetInfo info) {
        Style style = JSS.style(g.style(), "Label");
        style = JSS.name(label,style,g);
        
        TBox box = new TBox(0,0,label.width(),label.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
        TextRenderer.paint(style, label.text(), box, g);
    }
    
}
