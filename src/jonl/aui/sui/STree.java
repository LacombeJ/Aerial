package jonl.aui.sui;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.MouseButtonEvent;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.VAlign;
import jonl.jgl.Input;
import jonl.vmath.Mathd;
import jonl.vmath.Vector4;

public class STree extends SWidget implements Tree {

    List<TreeItem> items = new ArrayList<>();
    
    static final int BUTTON_SIZE = 8;
    
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
    void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT) {
            int height = getHeight();
            float fontHeight = Style.get(this).calibri.getHeight();
            float y = height;
            for (TreeItem item : getItems()) {
                y = findAndAct(e.x,e.y,item,fontHeight,y,fontHeight,fontHeight);
                if (y==-1) {
                    break;
                }
            }
        }
    }
    
    float findAndAct(int ex, int ey, TreeItem t, float x, float y, float dx, float dy) {
        float half = Style.get(this).calibri.getHeight() / 2f;
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
    
    @Override
    void paint(Graphics g) {
        int height = getHeight();
        float fontHeight = Style.get(this).calibri.getHeight();
        float y = height;
        for (TreeItem item : getItems()) {
            y = paintItem(g,item,fontHeight,y,fontHeight,fontHeight);
        }
    }
    
    float paintItem(Graphics g, TreeItem t, float x, float y, float dx, float dy) {
        g.renderText(t.getText(),x,y,HAlign.LEFT,VAlign.TOP,Style.get(this).calibri,new Vector4(0,0,0,1));
        if (t.getItemCount()!=0) {
            float half = Style.get(this).calibri.getHeight() / 2f;
            Vector4 col = t.isExpanded() ? new Vector4(0,0,0.2f,1) : new Vector4(0.8f,0.8f,0.8f,1);
            g.renderRect(x-half-2,y-half-2,BUTTON_SIZE,BUTTON_SIZE,col);
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

}
