package jonl.aui.sui;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Align;
import jonl.aui.DoubleChangedListener;
import jonl.aui.Graphics;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.SplitPanel;
import jonl.aui.Widget;
import jonl.jgl.Input;
import jonl.vmath.Vector4;

public class SSplitPanel extends AbstractDoubleSlot implements SplitPanel {

    SplitLayout layout = new SplitLayout();
    
    boolean inAdjustState;
    
    double min = 0.05;
    
    List<DoubleChangedListener> doubleChangedListeners = new ArrayList<>();
    
    SSplitPanel() {
        
    }
    
    SSplitPanel(Widget w1, Widget w2, Align align, double ratio) {
        setSplit(w1,w2,align,ratio);
    }
    
    @Override
    public void setSplit(Widget w1, Widget w2, Align align, double ratio) {
        setWidgetOne(w1);
        setWidgetTwo(w2);
        setAlign(align);
        setRatio(ratio);
    }
    
    @Override
    public void addRatioChangedListener(DoubleChangedListener vc) {
        doubleChangedListeners.add(vc);
    }
    
    void fireValueChanged(double value, double oldValue) {
        for (DoubleChangedListener vc : doubleChangedListeners) {
            vc.valueChanged(value,oldValue);
        }
    }
    
    private boolean isOnBorder(int x, int y) {
        switch (layout.align) {
        case HORIZONTAL:
            int minX = getWidgetOne().getWidth();
            int maxX = minX + layout.border;
            if (x>=minX && x<=maxX) {
                return true;
            }
            break;
        case VERTICAL:
            int minY = getWidgetOne().getHeight();
            int maxY = minY + layout.border;
            if (y>=minY && y<=maxY) {
                return true;
            }
            break;
        }
        return false;
    }
    
    @Override
    void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT && isOnBorder(e.x,e.y)) {
            inAdjustState = true;
        }
    }
    
    @Override
    void fireMouseReleased(MouseButtonEvent e) {
        super.fireMouseReleased(e);
        if (e.button==Input.MB_LEFT) {
            inAdjustState = false;
        }
    }
    
    @Override
    void fireMouseMoved(MouseMotionEvent e) {
        super.fireMouseMoved(e);
        double ratio = layout.ratio;
        if (inAdjustState) {
            switch (layout.align) {
            case HORIZONTAL:
                ratio = (double)e.x / getWidth();
                break;
            case VERTICAL:
                ratio = (double)e.y / getHeight();
                break;
            }
        }
        if (ratio<min) ratio = min;
        if (ratio>(1-min)) ratio = (1-min);
        setRatio(ratio);
    }
    
    @Override
    void fireMouseExit(MouseMotionEvent e) {
        super.fireMouseExit(e);
        inAdjustState = false;
    }
    
    @Override
    public void setAlign(Align align) {
        layout.align = align;
        layout();
    }
    
    @Override
    public void setRatio(double ratio) {
        double oldRatio = layout.ratio;
        layout.ratio = ratio;
        if (ratio!=oldRatio) {
            fireValueChanged(ratio,oldRatio);
        }
        layout();
    }

    @Override
    public void layout() {
        layout.layout(this);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        switch (layout.align) {
        case HORIZONTAL:
            int minX = getWidgetOne().getWidth();
            g.renderRect(minX,0,layout.border,getHeight(),new Vector4(0.1f,0.1f,0.3f,1));
            break;
        case VERTICAL:
            int minY = getWidgetOne().getHeight();
            g.renderRect(0,minY,getWidth(),layout.border,new Vector4(0.1f,0.1f,0.3f,1));
            break;
        }
    }

    

}
