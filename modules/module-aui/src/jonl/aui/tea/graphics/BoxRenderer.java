package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class BoxRenderer {

    public static void paint(Style style, TBox box, TGraphics g) {
        
        ColorValue background = JSS.color(style.get("background"));
        ColorValue borderColor = JSS.color(style.get("border-color"));
        float border = JSS.asFloat(style.get("border"));
        float radius = JSS.asFloat(style.get("radius"));
        
        if (border!=0) {
            //TODO create border mesh with a cut out in middle
            StyleRenderer.rect(g,0,0,box.width,box.height,borderColor,radius);
        }
        StyleRenderer.rect(g,border,border,box.width-2*border,box.height-2*border,background,radius);
        
    }
    
}
