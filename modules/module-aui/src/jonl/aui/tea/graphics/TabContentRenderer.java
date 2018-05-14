package jonl.aui.tea.graphics;

import jonl.aui.Info;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TTabContent;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class TabContentRenderer {

    public static void paint(TTabContent widget, TGraphics g, Info info) {
        Style style = JSS.style(g.style(),"TabPanel.Content");
        style = JSS.name(widget,style,g);
        
        TBox box = new TBox(0,0,widget.width(),widget.height());
        
        BoxRenderer.paint(style, box, g);
        ImageRenderer.paint(style, box, g);
    }
    
}
