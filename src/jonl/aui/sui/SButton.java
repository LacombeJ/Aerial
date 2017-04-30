package jonl.aui.sui;

import jonl.aui.Action;
import jonl.aui.Button;
import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.VAlign;
import jonl.jgl.Input;
import jonl.vmath.Vector4;

public class SButton extends SWidget implements Button {

    String text = "Button";
    
    boolean isMouseHover = false;
    
    SButton() {
        
    }
    
    SButton(String text) {
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
    public void addAction(Action action) {
        addWidgetAction(action);
    }
    
    @Override
    void fireMouseHover(MouseMotionEvent e) {
        super.fireMouseHover(e);
        isMouseHover = true;
    }
    
    @Override
    void fireMouseExit(MouseMotionEvent e) {
        super.fireMouseExit(e);
        isMouseHover = false;
    }
    
    @Override
    void fireMouseClicked(MouseButtonEvent e) {
        super.fireMouseClicked(e);
        if (e.button==Input.MB_LEFT) {
            fireWidgetActions();
        }
    }

    
    
    int buttonEnterTime = 0;
    final float time = 100;
    
    @Override
    void paint(Graphics g) {
        super.paint(g);
        
        if (isMouseHover) {
            if (buttonEnterTime<time) {
                buttonEnterTime++;
            }
        } else {
            if (buttonEnterTime>0) {
                buttonEnterTime--;
            }
        }
        float v = buttonEnterTime / time;
        Vector4 col = Style.buttonColor.get().lerp(Style.buttonColorHover,v);
        g.renderRect(0,0,getWidth(),getHeight(),col);
        float x = getWidth()/2;
        float y = getHeight()/2;
        g.renderText(text,x,y,HAlign.CENTER,VAlign.MIDDLE,Style.calibri,new Vector4(0,0,0,1));
    }
    
    
}
