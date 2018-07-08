package ax.tea.graphics;

import ax.aui.HAlign;
import ax.aui.Info;
import ax.aui.VAlign;
import ax.commons.jss.Style;
import ax.tea.TGraphics;
import ax.tea.TTitlePanel;
import ax.tea.graphics.JSS.ColorValue;
import ax.tea.spatial.TBox;

public class TitlePanelRenderer {

    public static void paint(TTitlePanel panel, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), "TitlePanel");
        style = JSS.name(panel,style,g);
        
        ColorValue color = JSS.color(style.get("color"));
        TFont font = JSS.font(style.get("font"));
        
        float width = font.getWidth(panel.title());
        float dx = 4f;
        float dxs = 4f;
        float toph = font.getHeight()/2f;;
        float lefth = 1;
        float both = 1;
        float righth = 1;
        float thick = 1f;
        
        g.renderLine(lefth,toph,lefth,panel.height()-both,color.color,thick);
        g.renderLine(panel.width()-righth,toph,panel.width()-righth,panel.height()-both,color.color,thick);
        g.renderLine(lefth,panel.height()-both,panel.width()-righth,panel.height()-both,color.color,thick);
        g.renderText(panel.title(),lefth+dx+dxs,toph,HAlign.LEFT,VAlign.MIDDLE,font,color.color);
        g.renderLine(lefth,toph,lefth+dx,toph,color.color,thick);
        g.renderLine(lefth+dx+width+dxs+dxs,toph,panel.width()-righth,toph,color.color,thick);
        
        TBox box = new TBox(0,0,panel.width(),panel.height());
        
        BoxRenderer.paint(style, box, g);
    }
    
}
