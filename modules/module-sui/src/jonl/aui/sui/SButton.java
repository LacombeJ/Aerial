package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.logic.AButton;
import jonl.vmath.Vector4;

public class SButton extends AButton {
    
    SButton() {
        
    }
    
    SButton(String text) {
        setText(text);
    }

    int buttonEnterTime = 0;
    final float time = 30;
    
    @Override
    protected void paint(Graphics g) {
        super.paint(g);
        
        if (isMouseHover()) {
            if (buttonEnterTime<time) {
                buttonEnterTime++;
            }
        } else {
            if (buttonEnterTime>0) {
                buttonEnterTime--;
            }
        }
        float v = buttonEnterTime / time;
        Vector4 col = Style.get(this).buttonColor.get().lerp(Style.get(this).buttonColorHover,v);
        g.renderRect(0,0,getWidth(),getHeight(),col);
        float x = getWidth()/2;
        float y = getHeight()/2;
        g.renderText(getText(),x,y,HAlign.CENTER,VAlign.MIDDLE,Style.get(this).calibri,new Vector4(0,0,0,1));
    }
    
    
}
