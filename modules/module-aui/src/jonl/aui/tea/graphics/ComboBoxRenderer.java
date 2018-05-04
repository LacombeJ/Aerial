package jonl.aui.tea.graphics;

import jonl.aui.tea.TComboBox;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWidgetInfo;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class ComboBoxRenderer {

    public static void paint(TComboBox comboBox, TGraphics g, TWidgetInfo info) {
        boolean hover = info.get("bIsMouseWithin", false);
        
        Style style = JSS.style(g.style(), "ComboBox");
        if (hover) {
            style = JSS.style(style,"hover");
        }
        style = JSS.name(comboBox,style,g);
        
        TBox box = new TBox(0,0,comboBox.width(),comboBox.height());

        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
        TextRenderer.paint(style, comboBox.text(), box, g);
    }
    
}
