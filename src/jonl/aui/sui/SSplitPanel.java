package jonl.aui.sui;

import jonl.aui.Align;
import jonl.aui.Graphics;
import jonl.aui.Widget;
import jonl.aui.logic.ASplitPanel;
import jonl.vmath.Vector4;

public class SSplitPanel extends ASplitPanel {

    SSplitPanel() {
        
    }
    
    SSplitPanel(Widget w1, Widget w2, Align align, double ratio) {
        setSplit(w1,w2,align,ratio);
    }
    
    @Override
    protected void paint(Graphics g) {
        super.paint(g);
        switch (getAlign()) {
        case HORIZONTAL:
            int minX = getWidgetOne().getWidth();
            g.renderRect(minX,0,layoutBorder(),getHeight(),new Vector4(0.1f,0.1f,0.3f,1));
            break;
        case VERTICAL:
            int minY = getWidgetOne().getHeight();
            g.renderRect(0,minY,getWidth(),layoutBorder(),new Vector4(0.1f,0.1f,0.3f,1));
            break;
        }
    }
    
}
