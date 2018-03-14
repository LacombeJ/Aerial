package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.jutils.func.Wrapper;
import jonl.vmath.Mathf;

class TLayoutManager {

    static void setPositionAndRequestFire(TWidget w, int x, int y) {
        int prevX = w.x;
        int prevY = w.y;
        w.x = x;
        w.y = y;
        if (prevX!=x || prevY!=y) {
            TEventManager.firePositionChanged(w,new TMoveEvent(TEventType.Move,x,y,prevX,prevY));
        }
    }
    
    static void setSizeAndRequestFire(TWidget w, int width, int height) {
        int prevWidth = w.width;
        int prevHeight = w.height;
        w.width = width;
        w.height = height;
        if (prevWidth!=width || prevHeight!=height) {
            TEventManager.fireSizeChanged(w,new TResizeEvent(TEventType.Resize,width,height,prevWidth,prevHeight));
        }
    }
    
    static SizePreference getWidthPreference(TLayoutItem item) {
        return new SizePreference(item.minWidth(),item.maxWidth(),item.preferredWidth());
    }
    
    static SizePreference getHeightPreference(TLayoutItem item) {
        return new SizePreference(item.minHeight(),item.maxHeight(),item.preferredHeight());
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
    
    static int allocate(SizePreference pref, int dimension) {
        if (pref.pref <= 0) {
            return dimension;
        }
        return Mathf.clamp(pref.pref, pref.min, pref.max);
    }
    
    /**
     * Allocates sizes based on preferences such that added sizes are within their bounds and the
     * bounds of the given dimension<br>
     * Priority order: (prefs.min, prefs.max) , dimension, prefs.pref<br>
     * Assumes that min < pref < max
     * 
     */
    static int[] allocate(SizePreference[] prefs, int dimension, Wrapper<Integer> newDimension, Wrapper<Integer> totalDimension) {
        /*
        Algorithm:
        1) Get all sizes that have a preference and try to allocate sizes for those such that
        the closest preferences are chosen and there is enough space for the SizePreferences with
        an undefined pref.
        2) Allocate sizes with undefined pref to fit the newDimension
         */
        int[] size = new int[prefs.length];
        
        ArrayList<SizePreference> hasPref = new ArrayList<>();
        ArrayList<SizePreference> noPref = new ArrayList<>();
        
        int min = 0;
        
        int pref = 0;
        
        int noPrefMin = 0;
        
        for (int i=0; i<prefs.length; i++) {
            SizePreference sp = prefs[i];
            sp.i = i;
            min += sp.min;
            if (sp.pref>0) {
                hasPref.add(sp);
                pref += sp.pref;
            } else {
                noPref.add(sp);
                noPrefMin += sp.min;
            }
        }
        
        if (min>dimension) dimension = min;
        
        int targetPrefMax = Mathf.min(pref, dimension - noPrefMin);
        int actualPrefMax = 0;
        
        // Step 1
        
        for (int i=0; i<hasPref.size(); i++) {
            SizePreference sp = hasPref.get(i);
            double alpha = sp.pref / (double)pref;
            double prefSize = alpha * targetPrefMax;
            size[sp.i] = Mathf.clamp((int)prefSize, sp.min, sp.max);
            actualPrefMax += size[sp.i];
        }
        
        // Step 2
        double targetNoPref = dimension - actualPrefMax;
        int actualNoPref = 0;
        
        Wrapper<Integer> noPrefRemainding = new Wrapper<>((int)targetNoPref);
        int[] noPrefSizes = allocateNoPref(noPref, noPrefRemainding);
        for (int i=0; i<noPref.size(); i++) {
            SizePreference sp = noPref.get(i);
            size[sp.i] = noPrefSizes[i];
            actualNoPref += size[sp.i];
        }
        
        newDimension.x = dimension;
        totalDimension.x = actualPrefMax + actualNoPref;
        
        return size;
    }
    
    static final void allocateNoPrefHelper(ArrayList<SizePreference> prefs, int[] size, boolean handled[], long[] maxRemainder, Wrapper<Integer> remaining) {
        long totalMaxRemainders = 0;
        int maxAlphaIndex = -1;
        float maxAlpha = 0;
        
        for (int i=0; i<size.length; i++) {
            if (!handled[i]) {
                totalMaxRemainders += maxRemainder[i];
            }
        }
        
        float[] alpha = new float[maxRemainder.length];
        for (int i=0; i<maxRemainder.length; i++) {
            if (!handled[i]) {
                alpha[i] = (float) (maxRemainder[i] / (double)totalMaxRemainders);
            }
        }
        
        float[] inverseAlpha = Mathf.inverseAlphas(alpha);
        for (int i=0; i<inverseAlpha.length; i++) {
            if (!handled[i]) {
                if (inverseAlpha[i] > maxAlpha) {
                    maxAlpha = inverseAlpha[i];
                    maxAlphaIndex = i;
                }
            }
        }
        
        if (maxAlphaIndex != -1) {
            SizePreference sp = prefs.get(maxAlphaIndex);
            double a = inverseAlpha[maxAlphaIndex];
            int piece = (int) (a * remaining.x);
            if (piece > 0) {
                int nValue = size[maxAlphaIndex] + piece;
                int cValue = Mathf.clamp(nValue, sp.min, sp.max);
                int diff = cValue - size[maxAlphaIndex];
                if (diff > 0 && diff <= remaining.x) {
                    size[maxAlphaIndex] = cValue;
                    remaining.x -= diff;
                }
            }
            handled[maxAlphaIndex] = true;
        }
        
    }
    
    static int[] allocateNoPref(ArrayList<SizePreference> prefs, Wrapper<Integer> remaining) {
        int[] size = new int[prefs.size()];
        long[] maxRemainder = new long[size.length];
        
        for (int i=0; i<size.length; i++) {
            SizePreference sp = prefs.get(i);
            size[i] = sp.min;
            maxRemainder[i] = sp.max - sp.min;
            remaining.x -= size[i];
        }
        
        boolean[] handled = new boolean[maxRemainder.length];
        for (int i=0; i<handled.length; i++) {
            allocateNoPrefHelper(prefs, size, handled, maxRemainder, remaining);
        }
        
        return size;
    }
    
    static class SizePreference {
        int i;
        int min;
        int max;
        int pref;
        SizePreference(int min, int max, int pref) {
            this.min = min;
            this.max = max;
            this.pref = pref;
        }
    }
    
}
