package jonl.aui.logic;

import jonl.aui.Layout;
import jonl.aui.MultiSlot;
import jonl.aui.Panel;
import jonl.aui.logic.ListLayout;

public abstract class APanel extends AMultiSlot implements Panel {

    Layout<MultiSlot> layout = new ListLayout();
    
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
