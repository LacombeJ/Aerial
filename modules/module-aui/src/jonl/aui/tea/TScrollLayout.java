package jonl.aui.tea;

import jonl.aui.Layout;

public class TScrollLayout extends TLayout implements Layout {

    @Override
    public void layout() {
        TScrollPanel scrollPanel = (TScrollPanel)parent;
        
        int width = parent.width;
        int height = parent.height;
        
        int contentWidgetWidth = freeWidth(scrollPanel.content.widget());
        int contentWidgetHeight = freeHeight(scrollPanel.content.widget());
        
        int horHeight = freeHeight(scrollPanel.horBar);
        int verWidth = freeWidth(scrollPanel.verBar);
        
        boolean hasHorBar = false;
        boolean hasVerBar = false;
        
        if (contentWidgetWidth > width) {
            hasHorBar = true;
        }
        
        if (contentWidgetHeight > height) {
            hasVerBar = true;
        }
        
        if (!hasHorBar) {
            horHeight = 0;
        }
        
        if (!hasVerBar) {
            verWidth = 0;
        }
        
        if (hasHorBar && !contains(scrollPanel.horBar)) {
            addNoInvalidate(scrollPanel.horBar);
        } else if (!hasHorBar && contains(scrollPanel.horBar)) {
            removeNoInvalidate(scrollPanel.horBar);
        }
        
        if (hasVerBar && !contains(scrollPanel.verBar)) {
            addNoInvalidate(scrollPanel.verBar);
        } else if (!hasVerBar && contains(scrollPanel.verBar)) {
            removeNoInvalidate(scrollPanel.verBar);
        }
            
        if (hasHorBar) {
            setPositionAndSize(scrollPanel.horBar, 0, height - horHeight, width - verWidth, horHeight );
        }
        
        if (hasVerBar) {
            setPositionAndSize(scrollPanel.verBar, width - verWidth, 0, verWidth, height - horHeight);
        }
        
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
