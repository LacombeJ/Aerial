package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.SizePolicy;
import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.jutils.func.List;
import jonl.jutils.func.Wrapper;
import jonl.jutils.structs.ArrayList2D;
import jonl.vmath.Mathi;

/**
 * Utility class for managing layouts
 * 
 * @author Jonathan
 *
 */
class TLayoutManager {

    /*
     * How layouts and resizing widgets work:
     * 
     * * There are two "paths": The "layout path" and the "size policy path".
     *   The layout path changes when the size of a widget is changed.
     *     (For ex: window resizing, or adjusting a split panel)
     *   The size policy path is changed when its min,max,hint size is changed.
     *     (For ex: adding/removing a widget, or adjusting the text on a button or label)
     *     
     * * The layout path moves downward, and the size policy path moves upward (in terms of widget hiearchy)
     *     (For ex: changing the size of a window will cause the window to layout it's children again.)
     *     (also: modifying the text of a button will cause its size policy to change and its parents size policy to change and so forth)
     *   The size policy path may invalidate a layout so that children sizes can be adjusted.
     * 
     * Layout cycle:
     * 
     * 1) At the beginning of the UI lifecycle, the root widget's layout asks for
     *    it's children hint sizes. The children containers, not knowing
     *    their size hint, will in turn, recursively ask their children for their hint
     *    size to answer back to their parents. The root widget will set its size to the preferred size
     *    and optionally set size limits to its hint size
     * 2) Afterwards, the root widget will then layout it's children accordingly and it's children
     *    will also layout it's children and so forth.
     * 3) When the size policy of any widget changes, it will first
     *
     * Technical:
     * 
     * * A layout will have two functions invalidateLayout(), and invalidateSizeHint()
     *   - A widget will have a function invalidate() which will call it's layout parent invalidateSizeHint()
     *   - invalidateSizeHint() will call it's parent invalidateSizeHint() and so forth. When this reaches
     *     the root layout / widget, layout() will be peformed.
     *   - invalidateLayout()
     *   
     * Notes:
     * - Use long values to do size policy calculations since adding values to Integer.MAX_VALUE for ints will result in overflow
     * - Instead of calling widget min sizes or hint sizes. Use the freeWidth or freeHeight method or use the freeAllocate
     *   method to get there "true minimum" or preferred size for a widget. This is because the size hint may be set to a value
     *   but the layout manager will clamp that value to the min/max size value in its calculations.
     *   
     *
     */
    
    static void invalidateSizeHint(TLayout layout) {
        
        TSizeHint prevHint = layout.getSizeHint();
        TSizeHint newHint = layout.calculateSizeHint();
        
        if (!newHint.equals(prevHint)) {
            
            layout.validateSizeHint(newHint);
            
            TWidget widget = layout.parent;
            TWidget parentWidget = widget.parent();
            if (parentWidget != null) {
                invalidateSizeHint(parentWidget.widgetLayout());
            } else {
                invalidateLayout(layout);
            }
            
        } else {
            
            invalidateLayout(layout);
            
        }
        
    }
    
    static void invalidateLayout(TLayout layout) {
        
        layout.layout();
        
    }
    
    private static void invalidateWidgetLayout(TWidget widget) {
        TLayout layout = widget.widgetLayout();
        if (layout != null) {
            invalidateLayout(layout);
        }
    }
    
    // TODO for firePositionChanged and fireSizeChanged, only send events, don't call layout or anything else that should be handled by
    // this layout manager
    
    static void setPosition(TWidget w, int x, int y) {
        int prevX = w.x;
        int prevY = w.y;
        w.x = x;
        w.y = y;
        if (prevX!=x || prevY!=y) {
            invalidateWidgetLayout(w);
            w.manager().event().firePositionChanged(w,new TMoveEvent(TEventType.Move,x,y,prevX,prevY));
        }
    }
    
    static void setSize(TWidget w, int width, int height) {
        int prevWidth = w.width;
        int prevHeight = w.height;
        w.width = width;
        w.height = height;
        if (prevWidth!=width || prevHeight!=height) {
            invalidateWidgetLayout(w);
            w.manager().event().fireSizeChanged(w,new TResizeEvent(TEventType.Resize,width,height,prevWidth,prevHeight));
        }
    }
    
    static void setPositionAndSize(TWidget w, int x, int y, int width, int height) {
        int prevX = w.x;
        int prevY = w.y;
        int prevWidth = w.width;
        int prevHeight = w.height;
        w.x = x;
        w.y = y;
        w.width = width;
        w.height = height;
        if (prevX!=x || prevY!=y || prevWidth!=width || prevHeight!=height) {
            invalidateWidgetLayout(w);
            //TEventManager.firePositionChanged(w,new TMoveEvent(TEventType.Move,x,y,prevX,prevY));
        }
    }
    
    
    
    
    // ------------------------------------------------------------------------
    
    // Size allocations
    
    static class SizePreference {
        int policy;
        int hint;
        int min;
        int max;
        
        int i;
        int minHint;
        
        SizePreference(int policy, int hint, int min, int max) {
            this.policy = policy;
            this.hint = hint;
            this.min = min;
            this.max = max;
        }
        @Override
        public String toString() {
            return "SizePreference("+policy+","+min+","+max+","+hint+")";
        }
    }
    
    static int freeWidth(TWidget widget) {
        return Mathi.max(widget.sizeHint().width, widget.minWidth());
    }
    
    static int freeHeight(TWidget widget) {
        return Mathi.max(widget.sizeHint().height, widget.minHeight());
    }
    
    static int freeWidth(TLayoutItem item) {
        return Mathi.max(item.hintWidth(), item.minWidth());
    }
    
    static int freeWidth(ArrayList<TLayoutItem> items) {
        int finalDimension = 0;
        for (int i=0; i<items.size(); i++) {
            finalDimension += freeWidth(items.get(i));
        }
        return finalDimension;
    }
    
    static int freeHeight(TLayoutItem item) {
        return Mathi.max(item.hintHeight(), item.minHeight());
    }
    
    static int freeHeight(ArrayList<TLayoutItem> items) {
        int finalDimension = 0;
        for (int i=0; i<items.size(); i++) {
            finalDimension += freeHeight(items.get(i));
        }
        return finalDimension;
    }
    
    static int freeAllocate(SizePreference pref) {
        SizePreference[] prefs = {pref};
        return freeAllocate(prefs);
    }
    
    static int freeAllocate(SizePreference[] prefs) {
        int finalDimension = 0;
        for (int i=0; i<prefs.length; i++) {
            SizePreference pref = prefs[i];
            pref.i = i;
            
            int minHint = Mathi.max(pref.hint, pref.min);
            finalDimension += minHint;
        }
        return finalDimension;
    }
    
    static int freeMaxAllocate(SizePreference[] prefs) {
        int max = 0;
        for (SizePreference p : prefs) {
            max = Math.max(max, freeAllocate(p));
        }
        return max;
    }
    
    static int allocate(SizePreference pref, int dimension) {
        SizePreference[] prefs = {pref};
        Wrapper<Integer> extra = new Wrapper<>(0);
        int[] size = allocate(prefs, dimension, extra);
        return size[0];
    }
    
    /**
     * Allocates sizes based on preferences such that added sizes are within their bounds and the
     * bounds of the given dimension<br>
     * 
     */
    static int[] allocate(SizePreference[] prefs, int dimension, Wrapper<Integer> extra) {
        /*
        Algorithm:
        1) For each size preference calculate max(hint,min) and save these as minHint
        2) Calculate the sum of the minHints and save them as minDimension
        3) Set extraDimension = dimension - minDimension
        4) while extraDimension != 0 and for each policy [EXPANDING, PREFERRED, MINIMUM] in order:
           A) Find all preferences of the same policy and allocate the remaining extraDimension
              space such that: (in priority)
              - as much space is filled as possible
              - allocated sizes are as equal as possible
              - allocated sizes are not greater than max size
              If there is still extraDimensions left, continue to next policy
        5) Return size arrays and newDimension and totalDimension values
        */
        
        int[] size = new int[prefs.length];
        
        int minDimension = 0;
        
        ArrayList<SizePreference> expanding = new ArrayList<>();
        ArrayList<SizePreference> preferred = new ArrayList<>();
        ArrayList<SizePreference> minimum = new ArrayList<>();
        
        ArrayList2D<SizePreference> policies = new ArrayList2D<>();
        policies.add(expanding);
        policies.add(preferred);
        policies.add(minimum);
        
        for (int i=0; i<prefs.length; i++) {
            SizePreference pref = prefs[i];
            pref.i = i;
            
            int minHint = Mathi.max(pref.hint, pref.min);
            size[i] = minHint;
            minDimension += minHint;
            
            if (pref.policy==SizePolicy.EXPANDING) expanding.add(pref);
            if (pref.policy==SizePolicy.PREFERRED) preferred.add(pref);
            if (pref.policy==SizePolicy.MINIMUM) minimum.add(pref);
        }
        
        Wrapper<Integer> extraDimension = new Wrapper<>(0);
        extraDimension.x = dimension - minDimension;
        
        int index = 0;
        while (extraDimension.x > 0 && index < policies.size()) {
            ArrayList<SizePreference> policy = policies.get(index);
            allocateExtra(policy, extraDimension, size);
            index++;
        }
        
        extra.x = extraDimension.x;
        
        return size;
    }
    
    static void allocateExtra(ArrayList<SizePreference> policy, Wrapper<Integer> extraDimension, int[] size) {
        ArrayList<SizePreference> rest = List.sort(policy, (a,b) -> Integer.compare(a.max, b.max));
        
        while (rest.size() > 0) {
            SizePreference p = List.first(rest);
            
            int ideal = extraDimension.x / rest.size();
            int actual = Mathi.min(ideal, p.max-size[p.i]); //max-size (actual difference)
            
            size[p.i] += actual;
            extraDimension.x -= actual;
            
            if (rest.size()>1) {
                rest = List.tail(rest);
            } else {
                // Try to add rest of extraDimension if possible
                // TODO maybe better to add 1 to multiple sizes instead of all to one? (7,7,9) vs (7,8,8)
                if (extraDimension.x > 0) {
                    int freeSpace = p.max - size[p.i];
                    size[p.i] += Mathi.min(extraDimension.x, freeSpace);
                }
                break;
            }
        }
    }
    
    static SizePreference getWidthPreference(TLayoutItem item) {
        return new SizePreference(item.sizePolicy().horizontal(), item.hintWidth(), item.minWidth(),item.maxWidth());
    }
    
    static SizePreference getHeightPreference(TLayoutItem item) {
        return new SizePreference(item.sizePolicy().vertical(), item.hintHeight(), item.minHeight(),item.maxHeight());
    }
    
    static SizePreference getWidthPreference(TWidget widget) {
        return new SizePreference(widget.sizePolicy().horizontal(), widget.sizeHint().width, widget.minWidth(),widget.maxWidth());
    }
    
    static SizePreference getHeightPreference(TWidget widget) {
        return new SizePreference(widget.sizePolicy().vertical(), widget.sizeHint().height, widget.minHeight(),widget.maxHeight());
    }
    
    static SizePreference[] getWidthPreferences(TLayout layout) {
        SizePreference[] prefs = new SizePreference[layout.count()];
        for (int i=0; i<prefs.length; i++) {
            TLayoutItem item = layout.getItem(i);
            prefs[i] = getWidthPreference(item);
        }
        return prefs;
    }
    
    static SizePreference[] getHeightPreferences(TLayout layout) {
        SizePreference[] prefs = new SizePreference[layout.count()];
        for (int i=0; i<prefs.length; i++) {
            TLayoutItem item = layout.getItem(i);
            prefs[i] = getHeightPreference(item);
        }
        return prefs;
    }
    
    
    
    
    
}
