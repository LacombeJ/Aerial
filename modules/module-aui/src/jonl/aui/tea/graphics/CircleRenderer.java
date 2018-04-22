package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;
import jonl.vmath.Mathf;

public class CircleRenderer {

    public static void paint(Style style, String backgroundId, String borderColorId, String borderId, TBox box, TGraphics g) {
        
        ColorValue background = JSS.color(style.get(backgroundId));
        ColorValue borderColor = JSS.color(style.get(borderColorId));
        float border = JSS.asFloat(style.get(borderId));
        
        float s = Mathf.min(box.width, box.height);
        
        float radius = s/2;
        
        float x = box.x + box.width/2 - radius;
        float y = box.y + box.height/2 - radius;
        
        JSS.rect(g,x,y,s,s,border,radius,background,borderColor);
        
    }
    
    public static void paint(Style style, TBox box, TGraphics g) {
        paint(style,"background","border-color","border",box,g);
    }
    
}
