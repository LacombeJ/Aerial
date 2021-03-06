package ax.tea.graphics;

import ax.aui.Info;
import ax.commons.jss.Style;
import ax.tea.TGraphics;
import ax.tea.TLineEdit;
import ax.tea.spatial.TBox;

public class LineEditRenderer {

    public static void paint(TLineEdit lineEdit, TGraphics g, Info info) {
        boolean hover = info.get("bIsMouseWithin", false);
        
        Style style = JSS.style(g.style(), "LineEdit");
        if (hover) {
            style = JSS.style(style,"hover");
        }
        style = JSS.name(lineEdit,style,g);
        
        TBox box = new TBox(0,0,lineEdit.width(),lineEdit.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
    }
    
    public static void paint(TLineEdit.TextArea area, TGraphics g, Info info) {
        boolean hover = info.get("bIsMouseWithin", false);
        
        Style style = JSS.style(g.style(), "LineEdit");
        if (hover) {
            style = JSS.style(style,"hover");
        }
        style = JSS.name(area,style,g);
        
        TBox box = new TBox(0,0,area.width(),area.height());
        
        TextRenderer.paint(style, area.edit().text(), box, g);
        if (area.hasFocus()) {
            CaretRenderer.paint(style, area.caretX(), 0, area.caretHeight(), g, info);
        }
    }
    
}
