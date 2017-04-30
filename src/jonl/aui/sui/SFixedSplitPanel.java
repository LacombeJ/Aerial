package jonl.aui.sui;

import jonl.aui.Border;
import jonl.aui.FixedSplitPanel;
import jonl.aui.Widget;

public class SFixedSplitPanel extends AbstractDoubleSlot implements FixedSplitPanel {

    FixSplitLayout layout = new FixSplitLayout();
    
    SFixedSplitPanel() {
        
    }
    
    SFixedSplitPanel(Widget w1, Widget w2, Border type, int i) {
        setSplit(w1,w2,type,i);
    }
    
    @Override
    public void setSplit(Widget w1, Widget w2, Border type, int fix) {
        setWidgetOne(w1);
        setWidgetTwo(w2);
        setType(type);
        setFixedSize(fix);
    }
    
    @Override
    public void setType(Border type) {
        layout.type = type;
    }
    
    @Override
    public void setFixedSize(int i) {
        layout.fix = i;
    }

    @Override
    public void layout() {
        layout.layout(this);
    }

    
    

}
