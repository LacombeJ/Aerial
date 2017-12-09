package jonl.aui.logic;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.MouseButtonEvent;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.jgl.Input;
import jonl.jutils.math.Mathd;

public abstract class ATree extends AWidget implements Tree {

    List<TreeItem> items = new ArrayList<>();
    
    static final int BUTTON_SIZE = 8; //TODO change this
    static final int ITEM_HEIGHT = 24; //TODO change this
    
    @Override
    public TreeItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public void addItem(TreeItem item) {
        items.add(item);
    }

    @Override
    public void removeItem(TreeItem item) {
        items.remove(item);
    }

    @Override
    public TreeItem removeItem(int i) {
        return items.remove(i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TreeItem[] getItems() {
        return items.toArray(new TreeItem[0]);
    }
    
    @Override
    protected void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT) {
            int height = getHeight();
            float y = height;
            for (TreeItem item : getItems()) {
                y = findAndAct(e.x,e.y,item,ITEM_HEIGHT,y,ITEM_HEIGHT,ITEM_HEIGHT);
                if (y==-1) {
                    break;
                }
            }
        }
    }
    
    float findAndAct(int ex, int ey, TreeItem t, float x, float y, float dx, float dy) {
        float half = ITEM_HEIGHT / 2f;
        if (Mathd.isWithinBounds(ex,ey,x-half-2,y-half-2,BUTTON_SIZE,BUTTON_SIZE)) {
            if (t.isExpanded() && t.getItemCount()!=0) {
                t.collapse();
            } else {
                t.expand();
            }
        }
        x+=dx;
        y-=dy;
        for (TreeItem item : t.getItems()) {
            if (t.isExpanded()) {
                y = findAndAct(ex,ey,item,x,y,dx,dy);
                if (y==-1) {
                    return -1;
                }
            }
        }
        return y;
    }
    
    protected int buttonSize() {
        return BUTTON_SIZE;
    }
    
    protected int itemHeight() {
        return ITEM_HEIGHT;
    }

}
