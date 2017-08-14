package jonl.aui.logic;

import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.ScrollPanel;
import jonl.aui.Widget;
import jonl.aui.logic.ScrollLayout;
import jonl.jgl.Input;
import jonl.jutils.math.Mathd;

public abstract class AScrollPanel extends ASingleSlot implements ScrollPanel {
    
    ScrollLayout layout = new ScrollLayout();
    
    float oa = 0;
    float ob = 0;
    
    double horScroll = 0;
    double verScroll = 0;
    
    boolean inHorState = false;
    boolean inVerState = false;
    
    //Horizontal ScrollBar
    int horBarHeight = 16;
    int horX = 0;
    int horWidth = 0;
    
    //Vertical ScrollBar
    int verBarWidth = 16;
    int verY = 0;
    int verHeight = 0;
    
    protected int horBarHeight()    { return horBarHeight; }
    protected int horX()            { return horX; }
    protected int horWidth()        { return horWidth; }
    
    protected int verBarWidth()     { return verBarWidth; }
    protected int verY()            { return verY; }
    protected int verHeight()       { return verHeight; }
    
    @Override
    public void setScroll(Widget w, int x, int y, int width, int height) {
        setWidget(w);
        setScrollX(x);
        setScrollY(y);
        setScrollWidth(width);
        setScrollHeight(height);
        updateScrollPos();
    }
    
    private void updateScrollPos() {
        int width = getWidth();
        int height = getHeight();
        horX = (int) (horScroll * (width - verBarWidth - horWidth));
        verY = (int) (height - (verScroll * (height - horBarHeight - verHeight)) - verHeight);
        setScrollX((int)Mathd.lerp(horScroll,0,getWidth()-verBarWidth-getWidget().getWidth()));
        setScrollY((int)Mathd.lerp(1-verScroll,horBarHeight,getHeight()-getWidget().getHeight()));
    }
    
    @Override
    protected void fireSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        super.fireSizeChanged(width,height,prevWidth,prevHeight);
        
        double horRatio = (double)(getWidth()-verBarWidth) / getWidget().getWidth();
        horRatio = Mathd.clamp(horRatio,0,1);
        horWidth = (int) Mathd.lerp(horRatio,horBarHeight,getWidth()-verBarWidth);
        
        double verRatio = (double)(getHeight()-horBarHeight) / getWidget().getHeight();
        verRatio = Mathd.clamp(verRatio,0,1);
        verHeight = (int) Mathd.lerp(verRatio,verBarWidth,getHeight()-horBarHeight);
        
        updateScrollPos();
    }
    
    @Override
    protected void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT) {
            if (Mathd.isWithinBounds(e.x,e.y,horX,0,horWidth,horBarHeight)) {
                inHorState = true;
                oa = e.x - horX;
                ob = e.x + (getWidth() - verBarWidth - horWidth - horX);
            } else if (Mathd.isWithinBounds(e.x,e.y,getWidth()-verBarWidth,verY,verBarWidth,verHeight)) {
                inVerState = true;
                oa = e.y - verY;
                ob = e.y + (getHeight() - verHeight - verY);
            }
        }
    }
    
    @Override
    protected void fireGlobalMouseReleased(MouseButtonEvent e) {
        super.fireGlobalMouseReleased(e);
        if (e.button==Input.MB_LEFT) {
            inHorState = false;
            inVerState = false;
        }
    }
    
    @Override
    protected void fireGlobalMouseMoved(MouseMotionEvent e) {
        super.fireGlobalMouseMoved(e);
        if (inHorState) {
            double r = Mathd.alpha(e.x, oa, ob);
            horScroll = Mathd.clamp(r,0,1);
            updateScrollPos();
        } else if (inVerState) {
            double r = Mathd.alpha(e.y, oa, ob);
            verScroll = Mathd.clamp(r,0,1);
            verScroll = 1 - verScroll;
            updateScrollPos();
        }
    }
    
    @Override
    protected void fireGlobalMouseExit(MouseMotionEvent e) {
        super.fireGlobalMouseExit(e);
        inHorState = false;
        inVerState = false;
    }
    
    @Override
    protected int[] getBox() {
        return new int[]{0,horBarHeight,getWidth()-verBarWidth,getHeight()};
    }
    
    @Override
    public void setScrollX(int x) {
        AWidget sw = getWidget();
        if (sw!=null) {
            sw.setXAndRequestFire(x);
        }
        layout();
    }
    
    @Override
    public void setScrollY(int y) {
        AWidget sw = getWidget();
        if (sw!=null) {
            sw.setYAndRequestFire(y);
        }
        layout();
    }
    
    @Override
    public void setScrollWidth(int width) {
        AWidget sw = getWidget();
        if (sw!=null) {
            sw.setWidthAndRequestFire(width);
        }
        layout();
    }
    
    @Override
    public void setScrollHeight(int height) {
        AWidget sw = getWidget();
        if (sw!=null) {
            sw.setHeightAndRequestFire(height);
        }
        layout();
    }
    
    @Override
    public void layout() {
        
    }
    

}
