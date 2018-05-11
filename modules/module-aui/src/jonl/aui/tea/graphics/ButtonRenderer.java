package jonl.aui.tea.graphics;

import jonl.aui.tea.TButton;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWidgetInfo;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class ButtonRenderer {

    public static Style style(TButton button, String selector, TGraphics g, TWidgetInfo info) {
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
    
    public static void paint(TButton button,  String selector, TGraphics g, TWidgetInfo info) {
        Style style = style(button, selector, g, info);
        
        TBox box = new TBox(0,0,button.width(),button.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, button.icon(), box, g);
        TextRenderer.paint(style, button.text(), box, g);
    }
    
    public static void paint(TButton button, TGraphics g, TWidgetInfo info) {
        paint(button, "Button", g, info);
    }
    
}
