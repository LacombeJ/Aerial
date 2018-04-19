package jonl.aui.tea.graphics;

import jonl.aui.tea.TGraphics;
import jonl.aui.tea.graphics.JSS.ColorValue;

public class StyleRenderer {

    static void rect(TGraphics g, float x, float y, float width, float height, ColorValue value) {
        if (value.type==ColorValue.COLOR) {
            g.renderRect(x,y,width,height,value.color);
        } else if (value.type==ColorValue.LINEAR_GRADIENT) {
            g.renderGradient(x,y,width,height,value.color,value.top);
        }
    }
    
}
