package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TDial;
import ax.aui.tea.TGraphics;
import ax.aui.tea.spatial.TBox;
import ax.commons.jss.Style;
import ax.math.vector.Mathf;
import ax.math.vector.Mathi;

public class DialRenderer {
    
    public static void paint(TDial dial, TGraphics g, Info info) {
        boolean hover = info.get("bIsMouseWithin", false);
        boolean down = info.get("bIsMouseDown", false);
        
        Style style = JSS.style(g.style(), "Dial");
        if (hover) {
            style = JSS.style(style,"hover");
            if (down) {
                style = JSS.style(style,"down");
            }
        }
        style = JSS.name(dial,style,g);
        
        double dialSize = JSS.percent(style.get("dial-size"));
        double knobSize = JSS.percent(style.get("knob-size"));
        double knobDistance = JSS.percent(style.get("knob-distance"));
        
        int w = (int) (dial.width() * dialSize);
        int h = (int) (dial.height() * dialSize);
        int x = dial.width()/2 - w/2;
        int y = dial.height()/2 - h/2;
        
        
        TBox box = new TBox(x,y,w,h);

        CircleRenderer.paint(style, box, g);
        
        
        // Dial knob
        float dialDiameter = Mathi.min(w,h);
        float alpha = Mathf.alpha(dial.value(),dial.min(),dial.max());
        float rad = (alpha*Mathf.TWO_PI);
        float knobDiameter = (float) (dialDiameter * knobSize);
        float knobDisplacement = (float) (dialDiameter/2 * knobDistance);
        float knobRadius = knobDiameter / 2;
        
        float knobDx = Mathf.cos(rad) * knobDisplacement;
        float knobDy = Mathf.sin(rad) * knobDisplacement;
        
        float knobX = dial.width()/2 + knobDx - knobRadius;
        float knobY = dial.height()/2 + knobDy - knobRadius;
        
        int ix = (int) knobX;
        int iy = (int) knobY;
        int id = (int) knobDiameter;
        
        TBox knobBox = new TBox(ix,iy,id,id);
        CircleRenderer.paint(style,"knob-color","knob-border-color","knob-border", knobBox, g);
    }
    
}
