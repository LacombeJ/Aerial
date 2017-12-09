package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.Widget;
import jonl.aui.logic.AScrollPanel;

public class SScrollPanel extends AScrollPanel {
    
    SScrollPanel() {
        
    }
    
    SScrollPanel(Widget w) {
        setWidget(w);
    }
    
    SScrollPanel(Widget w, int x, int y, int width, int height) {
        setScroll(w,x,y,width,height);
    }
    
    @Override
    protected void paint(Graphics g) {
        super.paint(g);
        g.renderRect(horX(),0,horWidth(),horBarHeight(),Style.get(this).buttonColorHover);
        g.renderRect(getWidth()-verBarWidth(),verY(),verBarWidth(),verHeight(),Style.get(this).buttonColorHover);
        
    }

    
    

}
