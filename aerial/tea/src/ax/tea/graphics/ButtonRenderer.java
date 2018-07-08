package ax.tea.graphics;

import ax.aui.Info;
import ax.commons.jss.Style;
import ax.tea.TButton;
import ax.tea.TGraphics;
import ax.tea.spatial.TBox;

public class ButtonRenderer {

    public static Style style(TButton button, String selector, TGraphics g, Info info) {
        boolean hover = info.get("bIsMouseWithin", false);
        boolean down = info.get("bIsMouseDown", false);
        boolean checked = button.checked();
        boolean disabled = !button.enabled();
        
        Style style = JSS.style(g.style(), selector);
        if (disabled) {
            style = JSS.style(style,"disabled");
        } else if (checked) {
            style = JSS.style(style,"checked");
            if (hover) {
                style = JSS.style(style,"hover");
                if (down) {
                    style = JSS.style(style,"down");
                }
            }
        } else if (hover) {
            style = JSS.style(style,"hover");
            if (down) {
                style = JSS.style(style,"down");
            }
        }
        style = JSS.name(button,style,g);
        
        return style;
    }
    
    public static void paint(TButton button,  String selector, TGraphics g, Info info) {
        Style style = style(button, selector, g, info);
        
        TBox box = new TBox(0,0,button.width(),button.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, button.icon(), box, g);
        TextRenderer.paint(style, button.text(), box, g);
    }
    
    public static void paint(TButton button, TGraphics g, Info info) {
        paint(button, "Button", g, info);
    }
    
}
