package jonl.aui.sui;

import java.util.ArrayList;

import jonl.aui.Align;
import jonl.aui.MultiSlot;

public class WeightedListLayout extends ListLayout {
    
    ArrayList<Integer> weights = new ArrayList<>();
    
    public WeightedListLayout(Align type, int... weights) {
        super(type);
        setWeights(weights);
    }
    
    public WeightedListLayout() {
        super();
    }
    
    @Override
    void horizontal(MultiSlot container) {
        int width = container.getWidth();
        int height = container.getHeight();
        int sum = getTotal(container.getChildrenCount());
        int x = 0;
        int restWidth = width;
        for (int i=0; i<container.getChildrenCount(); i++) {
            int size = (int)(((float)weights.get(i)/sum) * width);
            if (i==container.getChildrenCount()-1) {
                size = restWidth;
            }
            SWidget w = (SWidget) container.getChild(i);
            w.setPositionAndRequestFire(x,0);
            w.setSizeAndRequestFire(size,height);
            x += size;
            restWidth -= size;
        }
    }
    
    @Override
    void vertical(MultiSlot container) {
        int width = container.getWidth();
        int height = container.getHeight();
        int sum = getTotal(container.getChildrenCount());
        int y = 0;
        int restHeight = height;
        for (int i=0; i<container.getChildrenCount(); i++) {
            int size = (int)(((float)weights.get(i)/sum) * height);
            if (i==container.getChildrenCount()-1) {
                size = restHeight;
            }
            SWidget w = (SWidget) container.getChild(i);
            w.setPositionAndRequestFire(0,y);
            w.setSizeAndRequestFire(width,size);
            y += size;
            restHeight -= size;
        }
    }
    
    private void setWeights(int... list) {
        weights = new ArrayList<>();
        for (int i : list) {
            weights.add(i);
        }
    }
    
    private int getTotal(int m) {
        int sum = 0;
        while (weights.size()<m) {
            weights.add(1);
        }
        for (int i : weights) {
            sum += i;
        }
        return sum;
    }

}
