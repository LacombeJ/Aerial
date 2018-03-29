package jonl.aui.tea;

import jonl.aui.Layout;

public class TScrollLayout extends TLayout implements Layout {

    @Override
    void layout() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int width = parent.width;
        int height = parent.height;
        
        int horHeight = TManagerLayout.freeHeight(scrollPanel.horBar);
        int verWidth = TManagerLayout.freeWidth(scrollPanel.verBar);
        
        // Called updateScrollPanel twice intentionally, problem was that sometimes the bar wouldn't be at its right size
        // TODO have to track this down to call updateScrollPanel at the right time
        
        scrollPanel.updateScrollPanel();
        
        manager().layout().setPositionAndSize(scrollPanel.horBar, 0, height - horHeight, width - verWidth, horHeight );
        manager().layout().setPositionAndSize(scrollPanel.verBar, width - verWidth, 0, verWidth, height - horHeight);
        manager().layout().setPositionAndSize(scrollPanel.content, 0, 0, width - verWidth, height - horHeight);
        
        scrollPanel.updateScrollPanel();
        
    }

    @Override
    TSizeHint calculateSizeHint() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int horHeight = TManagerLayout.freeHeight(scrollPanel.horBar);
        int verWidth = TManagerLayout.freeHeight(scrollPanel.verBar);
        
        
        
        return new TSizeHint(verWidth, horHeight);
    }

}
