package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;
import jonl.vmath.Vector2;

public class TextRenderer {

    public static void paint(Style style, String text, TBox box, TGraphics g) {
        ColorValue color = JSS.color(style.get("color"));
        HAlign textAlign = JSS.textAlign(style.get("text-align"));
        TFont font = JSS.font(style.get("font"));
        Vector2 offset = JSS.vec2(style.get("text-offset"));
        
        float x = box.width/2;
        float y = box.height/2;
        
        float TX = x;
        if (textAlign==HAlign.LEFT) {
            TX = 0;
        } else if (textAlign==HAlign.RIGHT) {
            TX = box.width - 0;
        }
        
        g.renderText(text,offset.x+TX,y,textAlign,VAlign.MIDDLE,font,color.color);
        
    }
    
}
