package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.Info;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;
import jonl.vmath.Vector2;

public class ButtonBoxRenderer {

    public static void paint(TButton checkBox, String selector, TGraphics g, Info info) {
        Style style = ButtonRenderer.style(checkBox, selector, g, info);
        
        int size = JSS.asInt(style.get("box-size"));
        HAlign align = JSS.textAlign(style.get("box-align"));
        Vector2 offset = JSS.vec2(style.get("box-offset"));
        
        float w = checkBox.width();
        float h = checkBox.height();
        
        float x = w/2 - size/2;
        float y = h/2 - size/2;
        
        if (align==HAlign.LEFT) {
            x = 0;
        } else if (align==HAlign.RIGHT) {
            x = w - size;
        }
        
        x += offset.x;
        y += offset.y;
        
        int ix = (int)x;
        int iy = (int)y;
        
        TBox check = new TBox(ix,iy,size,size);
        
        TBox box = new TBox(0,0,checkBox.width(),checkBox.height());

        BoxRenderer.paint(style, check, g);
        ImageRenderer.paint(style, box, g);
        TextRenderer.paint(style, checkBox.text(), box, g);
    }
    
    
}
