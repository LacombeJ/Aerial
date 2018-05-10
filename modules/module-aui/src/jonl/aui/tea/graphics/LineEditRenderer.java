package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TLineEdit;
import jonl.aui.tea.TWidgetInfo;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class LineEditRenderer {

    public static void paint(TLineEdit lineEdit, TGraphics g, TWidgetInfo info) {
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
    
    public static void paint(TLineEdit.TextArea area, TGraphics g, TWidgetInfo info) {
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
