package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TGraphics;
import ax.aui.tea.TTabContent;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;

public class TabContentRenderer {

    public static void paint(TTabContent widget, TGraphics g, Info info) {
        Style style = JSS.style(g.style(),"TabPanel.Content");
        style = JSS.name(widget,style,g);
        
        TBox box = new TBox(0,0,widget.width(),widget.height());
        
        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
    }
    
}
