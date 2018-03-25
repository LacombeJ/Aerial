package jonl.aui.tea;

import jonl.aui.Align;

public class TSplitLayout extends TLayout {
    
    @Override
    public void layout() {
        TSplitPanel splitPane = (TSplitPanel) parent;
        
        TWidget w1 = splitPane.widgetOne();
        TWidget w2 = splitPane.widgetTwo();
        
        if (w1 != null && w2 != null) {
            int width = splitPane.width();
            int height = splitPane.height();
            
            // Using free allocate to get the minimum/preferred widths
            // Since the split pane can be adjustable free allocate enables us to get
            // adjustable sizes. 
            switch (splitPane.align()) {
            case HORIZONTAL:
                int w1Width = TLayoutManager.freeWidth(w1);
                int w2Width = TLayoutManager.freeWidth(w2);
                
                int midWidth = width - w1Width - w2Width - spacing();
                int midX = (int) (splitPane.ratio() * midWidth) + w1Width;
                int width1 = midX;
                int width2 = width - (midX + spacing());
                TLayoutManager.setPositionAndSize(w1, 0, 0, width1, height);
                TLayoutManager.setPositionAndSize(w2, width-width2, 0, width2, height);
                
                break;
            case VERTICAL:
                int w1Height = TLayoutManager.freeHeight(w1);
                int w2Height = TLayoutManager.freeHeight(w2);
                
                int midHeight = height - w1Height - w2Height - spacing();
                int midY = (int) (splitPane.ratio() * midHeight) + w1Height;
                int height1 = midY;
                int height2 = height - (midY + spacing());
                TLayoutManager.setPositionAndSize(w1, 0, 0, width, height1);
                TLayoutManager.setPositionAndSize(w2, 0, height-height2, width, height2);
                break;
            }
        }
    }
    
    @Override
    TSizeHint calculateSizeHint() {
        TSplitPanel splitPane = (TSplitPanel) parent;
        
        if (splitPane.align() == Align.HORIZONTAL) {
            int width = TLayoutManager.freeAllocate(TLayoutManager.getWidthPreferences(this));
            int height = TLayoutManager.freeMaxAllocate(TLayoutManager.getHeightPreferences(this));
            width += spacing();
            return new TSizeHint(width,height);
        } else {
            int width = TLayoutManager.freeMaxAllocate(TLayoutManager.getWidthPreferences(this));
            int height = TLayoutManager.freeAllocate(TLayoutManager.getHeightPreferences(this));
            height += spacing();
            return new TSizeHint(width,height);
        }
    }
    
    
}
