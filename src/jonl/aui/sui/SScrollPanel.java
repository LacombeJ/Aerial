package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.ScrollPanel;
import jonl.aui.Widget;
import jonl.jgl.Input;
import jonl.jutils.math.Mathd;

public class SScrollPanel extends AbstractSingleSlot implements ScrollPanel {
    
    ScrollLayout layout = new ScrollLayout();
    
    double horScroll = 0;
    double verScroll = 0;
    int barOffset = 0;
    
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
    
    SScrollPanel() {
        
    }
    
    SScrollPanel(Widget w) {
        setWidget(w);
    }
    
    SScrollPanel(Widget w, int x, int y, int width, int height) {
        setScroll(w,x,y,width,height);
    }
    
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
    void fireSizeChanged(int width, int height, int prevWidth, int prevHeight) {
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
    void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT) {
            if (Mathd.isWithinBounds(e.x,e.y,horX,0,horWidth,horBarHeight)) {
                inHorState = true;
                barOffset = (e.x - horX);
            } else if (Mathd.isWithinBounds(e.x,e.y,getWidth()-verBarWidth,verY,verBarWidth,verHeight)) {
                inVerState = true;
                barOffset = (e.y - verY);
            }
        }
    }
    
    @Override
    void fireMouseReleased(MouseButtonEvent e) {
        super.fireMouseReleased(e);
        if (e.button==Input.MB_LEFT) {
            inHorState = false;
            inVerState = false;
        }
    }
    
    @Override
    void fireMouseMoved(MouseMotionEvent e) {
        super.fireMouseMoved(e);
        if (inHorState) {
            double r = ((double)(e.x-barOffset) / (getWidth()-verBarWidth-horWidth));
            horScroll = Mathd.clamp(r,0,1);
            updateScrollPos();
        } else if (inVerState) {
            double r = ((double)(e.y-barOffset) / (getHeight()-horBarHeight-verHeight));
            verScroll = Mathd.clamp(r,0,1);
            verScroll = 1 - verScroll;
            updateScrollPos();
        }
        
    }
    
    @Override
    void fireMouseExit(MouseMotionEvent e) {
        super.fireMouseExit(e);
        inHorState = false;
        inVerState = false;
    }
    
    @Override
    int[] getBox() {
        return new int[]{0,horBarHeight,getWidth()-verBarWidth,getHeight()};
    }
    
    @Override
    public void setScrollX(int x) {
        SWidget sw = getWidget();
        if (sw!=null) {
            sw.setXAndRequestFire(x);
        }
        layout();
    }
    
    @Override
    public void setScrollY(int y) {
        SWidget sw = getWidget();
        if (sw!=null) {
            sw.setYAndRequestFire(y);
        }
        layout();
    }
    
    @Override
    public void setScrollWidth(int width) {
        SWidget sw = getWidget();
        if (sw!=null) {
            sw.setWidthAndRequestFire(width);
        }
        layout();
    }
    
    @Override
    public void setScrollHeight(int height) {
        SWidget sw = getWidget();
        if (sw!=null) {
            sw.setHeightAndRequestFire(height);
        }
        layout();
    }
    
    @Override
    public void layout() {
        
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.renderRect(horX,0,horWidth,horBarHeight,Style.get(this).buttonColorHover);
        g.renderRect(getWidth()-verBarWidth,verY,verBarWidth,verHeight,Style.get(this).buttonColorHover);
        
    }

    
    

}
