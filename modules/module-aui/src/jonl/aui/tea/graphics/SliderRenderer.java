package jonl.aui.tea.graphics;

import jonl.aui.Align;
import jonl.aui.Info;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TSlider;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;

public class SliderRenderer {

    public static void paint(TSlider slider, TGraphics g, Info info) {
        Style style = JSS.style(g.style(), "Slider");
        style = JSS.name(slider,style,g);
        
        int width = JSS.asInt(style.get("width"));
        int half = width/2;
        
        TBox box = null;
        
        if (slider.align()==Align.HORIZONTAL) {
            box = new TBox(0, slider.height()/2 -half, slider.width(), width);
        } else {
            box = new TBox(slider.width()/2 - half, 0, width, slider.height());
        }

        BoxRenderer.paint(style, box, g);
    }
    
}
