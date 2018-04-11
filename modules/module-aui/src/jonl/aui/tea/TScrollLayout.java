package jonl.aui.tea;

import jonl.aui.Layout;

public class TScrollLayout extends TLayout implements Layout {

    @Override
    public void layout() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int width = parent.width;
        int height = parent.height;
        
        int horHeight = freeHeight(scrollPanel.horBar);
        int verWidth = freeWidth(scrollPanel.verBar);
        
        // Called updateScrollPanel twice intentionally, problem was that sometimes the bar wouldn't be at its right size
        // TODO have to track this down to call updateScrollPanel at the right time
        
        scrollPanel.updateScrollPanel();
        
        setPositionAndSize(scrollPanel.horBar, 0, height - horHeight, width - verWidth, horHeight );
        setPositionAndSize(scrollPanel.verBar, width - verWidth, 0, verWidth, height - horHeight);
        setPositionAndSize(scrollPanel.content, 0, 0, width - verWidth, height - horHeight);
        
        scrollPanel.updateScrollPanel();
        
    }

    @Override
    public TSizeHint calculateSizeHint() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int horHeight = freeHeight(scrollPanel.horBar);
        int verWidth = freeWidth(scrollPanel.verBar);
        
        return new TSizeHint(verWidth, horHeight);
    }

}
