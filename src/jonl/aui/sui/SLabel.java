package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.Label;
import jonl.aui.MouseButtonEvent;
import jonl.aui.VAlign;
import jonl.jgl.Input;
import jonl.vmath.Vector4;

public class SLabel extends SWidget implements Label {

    String text = "Label";
    
    SLabel() {
        
    }
    
    SLabel(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String name) {
        this.text = name;
    }
    
    @Override
    void fireMouseClicked(MouseButtonEvent e) {
        super.fireMouseClicked(e);
        if (e.button==Input.MB_LEFT) {
            fireWidgetActions();
        }
    }

    
    
    @Override
    void paint(Graphics g) {
        float x = getWidth()/2;
        float y = getHeight()/2;
        g.renderText(text,x,y,HAlign.CENTER,VAlign.MIDDLE,Style.calibri,new Vector4(0,0,0,1));
    }
    
    
}
