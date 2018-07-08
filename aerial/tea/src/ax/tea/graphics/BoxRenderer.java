package ax.tea.graphics;

import ax.commons.jss.Style;
import ax.tea.TGraphics;
import ax.tea.graphics.JSS.ColorValue;
import ax.tea.spatial.TBox;

public class BoxRenderer {

    public static void paint(Style style, TBox box, TGraphics g) {
        
        ColorValue background = JSS.color(style.get("background"));
        ColorValue borderColor = JSS.color(style.get("border-color"));
        float border = JSS.asFloat(style.get("border"));
        float radius = JSS.asFloat(style.get("radius"));
        
        JSS.rect(g,box.x,box.y,box.width,box.height,border,radius,background,borderColor);
    }
    
}
