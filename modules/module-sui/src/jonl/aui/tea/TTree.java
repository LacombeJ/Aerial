package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.VAlign;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.vmath.Mathf;
import jonl.vmath.Vector4;

public class TTree extends TWidget implements Tree {

    ArrayList<TreeItem> items = new ArrayList<>();
    
    static final int BUTTON_SIZE = 8; //TODO change this
    static final int ITEM_HEIGHT = 24; //TODO change this
    
    public TTree() {
        super();
    }
    
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
    protected void paint(TGraphics g) {
        super.paint(g);
        int height = height();
        float y = height;
        for (TreeItem item : getItems()) {
            y = paintItem(g,item,ITEM_HEIGHT,y,ITEM_HEIGHT,ITEM_HEIGHT);
        }
    }
    
    private float paintItem(Graphics g, TreeItem t, float x, float y, float dx, float dy) {
        TGraphics tg = (TGraphics)g;
        tg.renderText(t.getText(),x,y,HAlign.LEFT,VAlign.TOP,style().font(),new Vector4(0,0,0,1));
        if (t.getItemCount()!=0) {
            float half = ITEM_HEIGHT / 2f;
            Vector4 col = t.isExpanded() ? new Vector4(0,0,0.2f,1) : new Vector4(0.8f,0.8f,0.8f,1);
            tg.renderRect(x-half-2,y-half-2,BUTTON_SIZE,BUTTON_SIZE,col);
        }
        x+=dx;
        y-=dy;
        for (TreeItem item : t.getItems()) {
            if (t.isExpanded()) {
                y = paintItem(g,item,x,y,dx,dy);
            }
        }
        return y;
    }
    
    @Override
    protected void handleMouseButtonClick(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            int height = height();
            float y = height;
            for (TreeItem item : getItems()) {
                y = findAndAct(event.x,event.y,item,ITEM_HEIGHT,y,ITEM_HEIGHT,ITEM_HEIGHT);
                if (y==-1) {
                    break;
                }
            }
        }
    }
    
    private float findAndAct(int ex, int ey, TreeItem t, float x, float y, float dx, float dy) {
        float half = ITEM_HEIGHT / 2f;
        if (Mathf.isWithinBounds(ex,ey,x-half-2,y-half-2,BUTTON_SIZE,BUTTON_SIZE)) {
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
    
    
    
}
