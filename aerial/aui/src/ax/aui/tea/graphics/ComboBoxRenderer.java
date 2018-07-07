package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TComboBox;
import ax.aui.tea.TGraphics;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;

public class ComboBoxRenderer {

    public static void paint(TComboBox comboBox, TGraphics g, Info info) {
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
