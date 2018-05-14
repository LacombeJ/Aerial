package jonl.aui.tea.graphics;

import jonl.aui.Info;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.graphics.JSS.ColorValue;
import jonl.jutils.jss.Style;

public class CaretRenderer {

    public static void paint(Style style, int caretX, int caretY, int caretHeight, TGraphics g, Info info) {
        ColorValue color = JSS.color(style.get("color"));
        
        int blinkInterval = info.get("iBlinkInterval",30);
        int blinkValue = info.get("iBlinkValue",0);
        boolean caretVisible = info.get("bCaretVisible", true);
        
        blinkValue++;
        
        if (blinkValue > blinkInterval) {
            blinkValue = 0;
            caretVisible = !caretVisible;
            info.put("bCaretVisible", caretVisible);
        }
        info.put("iBlinkValue",blinkValue);
        
        if (caretVisible) {
            g.renderLine(caretX,caretY,caretX,caretY+caretHeight,color.color,1);
        }
    }
    
}
