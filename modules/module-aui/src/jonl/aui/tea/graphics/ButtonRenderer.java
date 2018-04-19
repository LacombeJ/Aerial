package jonl.aui.tea.graphics;

import jonl.aui.tea.TButton;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.spatial.TBox;
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
        style = StyleRenderer.name(button,style,g);
        
        TBox box = new TBox(0,0,button.width(),button.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
        TextRenderer.paint(style, button.text(), box, g);
    }
    
}
