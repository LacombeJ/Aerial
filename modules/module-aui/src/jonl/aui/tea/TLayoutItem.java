package jonl.aui.tea;

import jonl.aui.Layout;
import jonl.aui.LayoutItem;

public abstract class TLayoutItem implements LayoutItem {

    TLayout layout = null;
    
    @Override
    public Layout layout() {
        return layout;
    }
    
    // ------------------------------------------------------------------------
    
    public TWidget asWidget() {
        return (TWidget) ((TWidgetItem)this).widget();
    }

}
