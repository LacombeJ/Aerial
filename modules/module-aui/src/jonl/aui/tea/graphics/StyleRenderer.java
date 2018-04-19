package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWidget;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.jutils.jss.Style;

public class StyleRenderer {

    static Style name(TWidget widget, Style style, TGraphics g) {
        String name = widget.name();
        if (!name.equals("")) {
            String id = "#"+name;
            Style idStyle = g.style().style(id);
            if (idStyle!=null) {
                Style append = style.copy();
                append.append(idStyle);
                return append;
            }
        }
        return style;
    }
    
    static void rect(TGraphics g, float x, float y, float width, float height, ColorValue value, float radius) {
        if (value.type==ColorValue.COLOR) {
            if (radius==0) {
                g.renderRect(x,y,width,height,value.color);
            } else {
                g.renderRect(x,y,width,height,value.color,radius);
            }
        } else if (value.type==ColorValue.LINEAR_GRADIENT) {
            if (radius==0) {
                g.renderGradient(x,y,width,height,value.color,value.top);
            } else {
                g.renderGradient(x,y,width,height,value.color,value.top,radius);
            }
        }
    }
    
}
