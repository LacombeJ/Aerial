package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.logic.ALabel;
import jonl.vmath.Vector4;

public class SLabel extends ALabel {

    SLabel() {
        
    }
    
    SLabel(String text) {
        setText(text);
    }
    
    @Override
    protected void paint(Graphics g) {
        super.paint(g);
        
        float x = getWidth()/2;
        float y = getHeight()/2;
        g.renderText(getText(),x,y,HAlign.CENTER,VAlign.MIDDLE,Style.get(this).calibri,new Vector4(0,0,0,1));
    }
    
    
}
