package jonl.aui.logic;

import jonl.aui.Graphics;
import jonl.aui.logic.AContainer;
import jonl.aui.logic.AWidget;
import jonl.jutils.misc.ArrayUtils;

public abstract class AGraphics implements Graphics {
    
    protected void paintWidget(AWidget w) {
        w.paint(this);
    }
    
    protected int[] getBox(AWidget w) {
        int[] box = new int[]{w.getWindowX(),w.getWindowY(),w.getWidth(),w.getHeight()};
        AContainer c = (AContainer) w.getParent();
        if (c!=null) {
            box = cutOut(box, ArrayUtils.add( c.getBox(), new int[]{c.getWindowX(),c.getWindowY(),0,0} ));
        }
        return box;
    }
    
    protected int[] cutOut(int[] paper, int[] scissorBox) {
        int x = Math.max(paper[0],scissorBox[0]);
        int y = Math.max(paper[1],scissorBox[1]);
        
        int paperX1 = paper[0] + paper[2];
        int boxX1 = scissorBox[0] + scissorBox[2];
        int x1 = Math.min(paperX1,boxX1);
        int width = x1 - x;
        
        int paperY1 = paper[1] + paper[3];
        int boxY1 = scissorBox[1] + scissorBox[3];
        int y1 = Math.min(paperY1,boxY1);
        int height = y1 - y;
        
        return new int[]{x,y,width,height};
    }
    
    
}
