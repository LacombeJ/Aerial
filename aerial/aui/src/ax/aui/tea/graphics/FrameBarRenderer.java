package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TFrameBar;
import ax.aui.tea.TGraphics;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;

public class FrameBarRenderer {

    public static void paint(TFrameBar panel, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), "Frame");
        style = JSS.name(panel,style,g);
        
        //ColorValue color = JSS.color(style.get("color"));
        //TFont font = JSS.font(style.get("font"));
        
        //g.renderText(panel.frame().title(), panel.width()/2f, panel.height()/2f, HAlign.CENTER, VAlign.MIDDLE, font, color.color);
        
        TBox box = new TBox(0,0,panel.width(),panel.height());
        TextRenderer.paint(style,panel.frame().title(),box,g);
    }
    
}
