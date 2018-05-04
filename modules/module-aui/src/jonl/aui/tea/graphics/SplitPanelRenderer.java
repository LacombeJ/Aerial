package jonl.aui.tea.graphics;

import jonl.aui.Align;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TSplitPanel;
import jonl.aui.tea.TWidgetInfo;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class SplitPanelRenderer {

    public static void paint(TSplitPanel splitPanel, TGraphics g, TWidgetInfo info) {
        Style style = JSS.style(g.style(), "SplitPanel");
        style = JSS.name(splitPanel,style,g);
        
        TBox box = null;
        
        if (splitPanel.align()==Align.HORIZONTAL) {
            int spacing = splitPanel.widgetTwo().x() - splitPanel.widgetOne().width();
            int minX = splitPanel.widgetOne().width();
            box = new TBox(minX, 0, spacing, splitPanel.height());
        } else {
            int spacing = splitPanel.widgetTwo().y() - splitPanel.widgetOne().height();
            int minY = splitPanel.widgetOne().height();
            box = new TBox(0, minY, splitPanel.width(), spacing);
        }

        BoxRenderer.paint(style, box, g);
    }
    
}
