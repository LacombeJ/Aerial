package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.jutils.func.Wrapper;
import jonl.vmath.Mathi;

public class TTree extends TWidget implements Tree {
    
    TWidget layoutWidget;
    ArrayList<TTreeItem> items = new ArrayList<>();
    
    static final int ITEM_SPACING = 24;
    
    static final int BUTTON_SIZE = 8; //TODO change this
    static final int ITEM_HEIGHT = 24; //TODO change this
    
    public TTree() {
        super();
        
        TFillLayout layout = new TFillLayout();
        TScrollPanel panel = new TScrollPanel();
        layoutWidget = new TPanel(new TreeLayout());
        
        panel.setWidget(layoutWidget);
        layout.add(panel);
        setWidgetLayout(layout);
        
    }
    
    @Override
    public TreeItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public void addItem(TreeItem item) {
        items.add((TTreeItem) item);
        layoutWidget.invalidateSizeHint();
    }

    @Override
    public void removeItem(TreeItem item) {
        items.remove(item);
        layoutWidget.invalidateSizeHint();
    }

    @Override
    public TreeItem removeItem(int i) {
        TreeItem item = items.remove(i);
        layoutWidget.invalidateSizeHint();
        return item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TreeItem[] getItems() {
        return items.toArray(new TreeItem[0]);
    }
    
    class TreeLayout extends TLayout {

        @Override
        public void layout() {
            
            int sx = margin().left;
            int sy = margin().top;
            
            removeAllNoInvalidate();
            
            ArrayList<TTreeItem> found = new ArrayList<TTreeItem>();
            
            int x = sx;
            Wrapper<Integer> y = new Wrapper<>(sy);
            
            for (TTreeItem item : items) {
                placeItem(item,x,y,found);
            }
            
        }
        
        private void placeItem(TTreeItem item, int x, Wrapper<Integer> y, ArrayList<TTreeItem> found) {
            int width = freeWidth(item.widget());
            int height = freeHeight(item.widget());
            
            if (!contains(item.widget())) {
                addNoInvalidate(item.widget());
            }
            
            setPositionAndSize(item.widget(),x,y.x,width,height);
            
            y.x += height;
            
            found.add(item);
            
            if (item.isExpanded()) {
                for (TTreeItem child : item.items) {
                    placeItem(child,x+ITEM_SPACING,y,found);
                }
            }
        }

        @Override
        public TSizeHint calculateSizeHint() {
            
            int sx = margin().left;
            int sy = margin().top;
            
            Wrapper<Integer> maxWidth = new Wrapper<>(sx);
            Wrapper<Integer> currentHeight = new Wrapper<>(sy);
            
            for (TTreeItem item : TTree.this.items) {
                adjustItem(item,sx,sy,maxWidth,currentHeight);
            }
            
            int width = maxWidth.x + margin().right;
            int height = currentHeight.x + margin().bottom;
            
            return new TSizeHint(width,height);
        }
        
        private void adjustItem(TTreeItem item, int x, int y, Wrapper<Integer> maxWidth, Wrapper<Integer> currentHeight) {
            int width = freeWidth(item.widget());
            int height = freeHeight(item.widget());
            
            maxWidth.x = Mathi.max(maxWidth.x, x + width);
            currentHeight.x += height;
            
            if (item.isExpanded()) {
                for (TTreeItem child : item.items) {
                    adjustItem(child,x+ITEM_SPACING,y,maxWidth,currentHeight);
                }
            }
        }
        
    }
    
    
    
    
    
}
