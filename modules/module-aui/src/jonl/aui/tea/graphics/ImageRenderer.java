package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TIcon;
import jonl.aui.tea.TResource;
import jonl.aui.tea.TUIManager;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;
import jonl.vmath.Vector2;

public class ImageRenderer {

    public static void paint(Style style, TIcon icon, TBox box, TGraphics g) {
        String imageRes = JSS.innerString(style.get("image"));
        HAlign imageAlign = JSS.textAlign(style.get("image-align"));
        Vector2 offset = JSS.vec2(style.get("image-offset"));
        ColorValue imageColor = JSS.color(style.get("image-color"));
        
        float x = box.width/2;
        float y = box.height/2;
        
        if (icon == null) {
            if (imageRes!=null) {
                TResource iconRes = TUIManager.instance().resource(imageRes);
                if (iconRes!=null) {
                    icon = (TIcon) iconRes.data();
                }
            }
        }
        
        if (icon!=null) {
            float IX = x;
            if (imageAlign==HAlign.LEFT) {
                IX = icon.width()*0.5f;
            } else if (imageAlign==HAlign.RIGHT) {
                IX = box.width - icon.width()*0.5f;
            }
            if (style.get("image-color") != null) {
                g.renderImage(icon, offset.x+IX, offset.y+y, imageColor.color);
            } else {
                g.renderImage(icon, offset.x+IX, offset.y+y);
            }
        }
        
    }
    
    public static void paint(Style style, TBox box, TGraphics g) {
        paint(style,null,box,g);
    }
    
}
