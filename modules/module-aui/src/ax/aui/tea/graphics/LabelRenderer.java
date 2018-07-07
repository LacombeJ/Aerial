package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TGraphics;
import ax.aui.tea.TLabel;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;

public class LabelRenderer {

    public static void paint(TLabel label, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), "Label");
        style = JSS.name(label,style,g);
        
        TBox box = new TBox(0,0,label.width(),label.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
        TextRenderer.paint(style, label.text(), box, g);
    }
    
}
