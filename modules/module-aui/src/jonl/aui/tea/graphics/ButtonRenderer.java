package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.jutils.jss.Style;

public class ButtonRenderer {

    public static void paint(TButton button, TGraphics g, TWidgetInfo info) {
        boolean hover = info.get("bIsMouseWithin", false);
        boolean down = info.get("bIsMouseDown", false);
        
        Style jss = g.style();
        
        Style style = jss.style("Button");
        if (hover) {
            style = style.style("hover");
            if (down) {
                style = style.style("down");
            }
        }
        
        ColorValue background = JSS.color(style.get("background"));
        ColorValue color = JSS.color(style.get("color"));
        HAlign textAlign = JSS.textAlign(style.get("text-align"));
        TFont font = JSS.font(style.get("font"));
        int offsetx = JSS.pixels(style.get("text-offsetx"));
        
        if (background.type==ColorValue.COLOR) {
            g.renderRect(0,0,button.width(),button.height(),background.color);
        } else if (background.type==ColorValue.LINEAR_GRADIENT) {
            g.renderGradient(0,0,button.width(),button.height(),background.color,background.top);
        }
        
        float x = button.width()/2;
        float y = button.height()/2;
        if (button.icon()!=null) {
            g.renderImage((TImage) button.icon(), x, y);
        }
        if (textAlign==HAlign.LEFT) {
            x = 0;
        } else if (textAlign==HAlign.RIGHT) {
            x = button.width() - 0;
        }
        
        g.renderText(button.text(),offsetx+x,y,textAlign,VAlign.MIDDLE,font,color.color);
        
    }
    
}
