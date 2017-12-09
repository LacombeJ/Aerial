package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.HAlign;
import jonl.aui.TreeItem;
import jonl.aui.VAlign;
import jonl.aui.logic.ATree;
import jonl.vmath.Vector4;

public class STree extends ATree {
    
    STree() {
        
    }
    
    @Override
    protected void paint(Graphics g) {
        super.paint(g);
        int height = getHeight();
        float y = height;
        for (TreeItem item : getItems()) {
            y = paintItem(g,item,itemHeight(),y,itemHeight(),itemHeight());
        }
    }
    
    float paintItem(Graphics g, TreeItem t, float x, float y, float dx, float dy) {
        g.renderText(t.getText(),x,y,HAlign.LEFT,VAlign.TOP,Style.get(this).calibri,new Vector4(0,0,0,1));
        if (t.getItemCount()!=0) {
            float half = itemHeight() / 2f;
            Vector4 col = t.isExpanded() ? new Vector4(0,0,0.2f,1) : new Vector4(0.8f,0.8f,0.8f,1);
            g.renderRect(x-half-2,y-half-2,buttonSize(),buttonSize(),col);
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
