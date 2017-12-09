package jonl.aui.logic;

import jonl.aui.Label;
import jonl.aui.MouseButtonEvent;
import jonl.jgl.Input;

public abstract class ALabel extends AWidget implements Label {

    String text = "Label";

    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String name) {
        this.text = name;
    }
    
    @Override
    protected void fireMouseClicked(MouseButtonEvent e) {
        super.fireMouseClicked(e);
        if (e.button==Input.MB_LEFT) {
            fireWidgetActions();
        }
    }
    
}
