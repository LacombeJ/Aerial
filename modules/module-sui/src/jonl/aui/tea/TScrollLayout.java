package jonl.aui.tea;

import jonl.aui.Layout;

public class TScrollLayout extends TLayout implements Layout {

    @Override
    void layout() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int width = parent.width;
        int height = parent.height;
        
        int horHeight = TLayoutManager.freeHeight(scrollPanel.horBar);
        int verWidth = TLayoutManager.freeWidth(scrollPanel.verBar);
        
        // Called updateScrollPanel twice intentionally, problem was that sometimes the bar wouldn't be at its right size
        // TODO have to track this down to call updateScrollPanel at the right time
        
        scrollPanel.updateScrollPanel();
        
        TLayoutManager.setPositionAndSize(scrollPanel.horBar, 0, height - horHeight, width - verWidth, horHeight );
        TLayoutManager.setPositionAndSize(scrollPanel.verBar, width - verWidth, 0, verWidth, height - horHeight);
        TLayoutManager.setPositionAndSize(scrollPanel.content, 0, 0, width - verWidth, height - horHeight);
        
        scrollPanel.updateScrollPanel();
        
    }

    @Override
    TSizeHint calculateSizeHint() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int horHeight = TLayoutManager.freeHeight(scrollPanel.horBar);
        int verWidth = TLayoutManager.freeHeight(scrollPanel.verBar);
        
        
        
        return new TSizeHint(verWidth, horHeight);
    }

}
