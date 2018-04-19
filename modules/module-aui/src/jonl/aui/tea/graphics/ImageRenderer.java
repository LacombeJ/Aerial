package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TIcon;
import jonl.aui.tea.TUIManager;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class ImageRenderer {

    public static void paint(Style style, TBox box, TGraphics g) {
        String imageRes = JSS.innerString(style.get("image"));
        HAlign imageAlign = JSS.textAlign(style.get("image-align"));

        TIcon icon = null;
        if (imageRes!=null) {
            icon = (TIcon) TUIManager.instance().resource(imageRes).data();
        }
        
        float x = box.width/2;
        float y = box.height/2;
        
        if (icon!=null) {
            float IX = x;
            if (imageAlign==HAlign.LEFT) {
                IX = icon.width()*0.5f;
            } else if (imageAlign==HAlign.RIGHT) {
                IX = box.width - icon.width()*0.5f;
            }
            g.renderImage(icon, IX, y);
        }
        
    }
    
}
