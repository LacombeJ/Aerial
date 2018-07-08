package ax.tea.graphics;

import ax.aui.Align;
import ax.aui.Info;
import ax.commons.jss.Style;
import ax.tea.TGraphics;
import ax.tea.TSplitPanel;
import ax.tea.spatial.TBox;

public class SplitPanelRenderer {

    public static void paint(TSplitPanel splitPanel, TGraphics g, Info info) {
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
