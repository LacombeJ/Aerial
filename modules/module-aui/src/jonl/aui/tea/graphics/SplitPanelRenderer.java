package jonl.aui.tea.graphics;

import jonl.aui.Align;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TSplitPanel;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class SplitPanelRenderer {

    public static void paint(TSplitPanel slider, TGraphics g, TWidgetInfo info) {
        Style style = JSS.style(g.style(), "SplitPanel");
        style = JSS.name(slider,style,g);
        
        int spacing = slider.widgetTwo().x() - slider.widgetOne().width();
        
        TBox box = null;
        
        if (slider.align()==Align.HORIZONTAL) {
            int minX = slider.widgetOne().width();
            box = new TBox(minX, 0, spacing, slider.height());
        } else {
            int minY = slider.widgetTwo().height();
            box = new TBox(0, minY, slider.width(), spacing);
        }

        BoxRenderer.paint(style, box, g);
    }
    
}
