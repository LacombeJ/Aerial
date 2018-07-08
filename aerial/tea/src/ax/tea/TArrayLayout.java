package ax.tea;

import java.util.ArrayList;
import java.util.HashMap;

import ax.aui.ArrayLayout;
import ax.aui.LayoutItem;
import ax.aui.Widget;
import ax.commons.func.Wrapper;
import ax.math.vector.Mathi;
import ax.tea.TLayoutManager.SizePreference;

/**
 * Layout items in a 2-dimensional array / grid. Each item will have the same width
 * as the items in the same column and the same height as the items in the same row.
 * An item can only take up the space of 1 grid box.
 * Although this layout uses a different algorithm than ListLayout,
 * this layout should be the same if all items are in a single column (vertical) or
 * a single row (horizontal).
 * 
 * @author Jonathan
 *
 */
public class TArrayLayout extends TLayout implements ArrayLayout {

    private int rows = 0;
    private int cols = 0;
    
    private HashMap<TLayoutItem,ArrayItem> gridMap = new HashMap<>();
    
    @Override
    public void add(Widget widget) {
        add(new TWidgetItem(widget));
    }
    
    @Override
    public void add(LayoutItem item) {
        super.add(item);
        gridMap.put((TLayoutItem) item, new ArrayItem(rows,Math.max(0,cols-1)));
        refreshGridSize();
    }


    @Override
    public void add(Widget widget, int row, int col) {
        add(new TWidgetItem(widget),row,col);
    }
    
    @Override
    public void add(LayoutItem item, int row, int col) {
        super.add(item);
        gridMap.put((TLayoutItem) item, new ArrayItem(row,col));
        refreshGridSize();
    }
    
    @Override
    public TWidget getWidget(int row, int col) {
        TLayoutItem item = getItem(row,col);
        if (item != null) {
            if (item instanceof TWidgetItem) {
                return item.asWidget();
            }
        }
        return null;
    }
    
    @Override
    public TLayoutItem getItem(int row, int col) {
        for (LayoutItem item : items()) {
            if (item instanceof TLayoutItem) {
                ArrayItem gridItem = gridMap.get(item);
                if (row==gridItem.row && col==gridItem.col) {
                    return (TLayoutItem) item;
                }
            }
        }
        return null;
    }

    @Override
    public void remove(LayoutItem item) {
        gridMap.remove(item);
        super.remove(item);
        refreshGridSize();
    }
    
    @Override
    public void remove(int index) {
        gridMap.remove(getItem(index));
        super.remove(index);
        refreshGridSize();
    }
    
    @Override
    public void removeAll() {
        super.removeAll();
        gridMap.clear();
        refreshGridSize();
    }
    
    @Override
    public int rowCount() {
        return rows;
    }

    @Override
    public int columnCount() {
        return cols;
    }
    
    private void refreshGridSize() {
        int rows = 0;
        int cols = 0;
        for (LayoutItem layoutItem : items()) {
            TLayoutItem item = (TLayoutItem)layoutItem;
            ArrayItem gridItem = gridMap.get(item);
            rows = Mathi.max(rows, gridItem.row+1);
            cols = Mathi.max(cols, gridItem.col+1);
        }
        this.rows = rows;
        this.cols = cols;
    }
    
    static class ArrayItem {
        int row;
        int col;
        ArrayItem(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    
    @Override
    public void layout() {

        if (!isEmpty()) {
            
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            int sx = margin().left;
            int sy = margin().top;
            
            width -= (cols-1) * spacing();
            height -= (rows-1) * spacing();
            
            // Allocating heights per row
            ArrayList<SizePreference> heightPrefs = new ArrayList<>();
            for (int i=0; i<rows; i++) {
                ArrayList<TLayoutItem> row = new ArrayList<>();
                for (int j=0; j<cols; j++) {
                    TLayoutItem item = getItem(i,j);
                    if (item!=null) {
                        row.add(item);
                    }
                }
                
                SizePreference[] prefs = getHeightPreferences(row);
                heightPrefs.add(stack(prefs));
            }
            Wrapper<Integer> extraHeight = new Wrapper<>(0);
            int[] allocateHeights = allocate(heightPrefs.toArray(new SizePreference[0]), height, extraHeight);
            
            // Allocating widths per column
            ArrayList<SizePreference> widthPrefs = new ArrayList<>();
            for (int j=0; j<cols; j++) {
                ArrayList<TLayoutItem> col = new ArrayList<>();
                for (int i=0; i<rows; i++) {
                    TLayoutItem item = getItem(i,j);
                    if (item!=null) {
                        col.add(item);
                    }
                }
                SizePreference[] prefs = getWidthPreferences(col);
                widthPrefs.add(stack(prefs));
            }
            Wrapper<Integer> extraWidth = new Wrapper<>(0);
            int[] allocateWidths = allocate(widthPrefs.toArray(new SizePreference[0]), width, extraWidth);
            
            
            // Place widgets into grid cells
            
            int extraWidthSpacing = extraWidth.x / (cols+1);
            int extraHeightSpacing = extraHeight.x / (rows+1);
            
            sx += extraWidthSpacing;
            sy += extraHeightSpacing;
            
            int y = sy;
            for (int i=0; i<rows; i++) {
                int x = sx;
                int h = allocateHeights[i];
                for (int j=0; j<cols; j++) {
                    TWidget widget = getWidget(i,j);
                    int w = allocateWidths[j];
                    if (widget!=null) {
                        // Allocate widget into space
                        // We don't want to set the dimensions directly because of size policy
                        int widgetWidth = allocate(getWidthPreference(widget), w);
                        int widgetHeight = allocate(getHeightPreference(widget), h);
                        int widgetX = x + w/2 - widgetWidth/2;
                        int widgetY = y + h/2 - widgetHeight/2;
                        this.setPositionAndSize(widget, widgetX, widgetY, widgetWidth, widgetHeight);
                    }
                    x += w + spacing() + extraWidthSpacing;
                }
                y += h + spacing() + extraHeightSpacing;
            }

        }

    }


    @Override
    public TSizeHint calculateSizeHint() {
        
        int widthMax = 0;
        int heightMax = 0;
        
        for (int j=0; j<cols; j++) {
            int maxWidth = 0;
            for (int i=0; i<rows; i++) {
                TLayoutItem item = getItem(i,j);
                if (item != null) {
                    maxWidth = Mathi.max(maxWidth, freeWidth(item));
                }
            }
            widthMax += maxWidth;
        }
        
        for (int i=0; i<rows; i++) {
            int maxHeight = 0;
            for (int j=0; j<cols; j++) {
                TLayoutItem item = getItem(i,j);
                if (item != null) {
                    maxHeight = Mathi.max(maxHeight, freeHeight(item));
                }
            }
            heightMax += maxHeight;
        }
                
        int width = widthMax + margin().width() + spacing()*(cols-1);
        int height = heightMax + margin().height() + spacing()*(rows-1);
        
        return new TSizeHint(width,height);
    }

}
