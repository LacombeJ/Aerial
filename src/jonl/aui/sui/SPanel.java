package jonl.aui.sui;

import jonl.aui.Layout;
import jonl.aui.MultiSlot;
import jonl.aui.Panel;

public class SPanel extends AbstractMultiSlot implements Panel {

    Layout<MultiSlot> layout = new ListLayout();
    
    SPanel() {
        
    }
    
    SPanel(Layout<MultiSlot> layout) {
        setLayout(layout);
    }
    
    @Override
    public void layout() {
        layout.layout(this);
    }
    
    @Override
    public Layout<MultiSlot> getLayout() {
        return layout;
    }

    @Override
    public void setLayout(Layout<MultiSlot> layout) {
        this.layout = layout;
        layout();
    }

    

}
