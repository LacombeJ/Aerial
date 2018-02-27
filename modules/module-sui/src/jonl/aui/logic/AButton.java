package jonl.aui.logic;

import jonl.aui.Action;
import jonl.aui.Button;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.jgl.Input;

public abstract class AButton extends AWidget implements Button {

    String text = "Button";
    
    boolean isMouseHover = false;

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
        action().connect(action);
    }
    
    @Override
    protected void fireMouseHover(MouseMotionEvent e) {
        super.fireMouseHover(e);
        isMouseHover = true;
    }
    
    @Override
    protected void fireMouseExit(MouseMotionEvent e) {
        super.fireMouseExit(e);
        isMouseHover = false;
    }
    
    @Override
    protected void fireMouseClicked(MouseButtonEvent e) {
        super.fireMouseClicked(e);
        if (e.button==Input.MB_LEFT) {
            fireWidgetActions();
        }
    }
    
    protected boolean isMouseHover() {
        return isMouseHover;
    }

}
