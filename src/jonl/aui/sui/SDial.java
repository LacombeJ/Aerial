package jonl.aui.sui;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Dial;
import jonl.aui.Graphics;
import jonl.aui.IntChangedListener;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.jgl.Input;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;

public class SDial extends SWidget implements Dial {

    int minValue = 0;
    int maxValue = 100;
    int value;
    boolean wrap = true;
    
    boolean inDialState = false;
    
    List<IntChangedListener> valueChangedListeners = new ArrayList<>();
    
    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void addValueChangedListener(IntChangedListener vc) {
        valueChangedListeners.add(vc);
    }
    
    void fireValueChanged(int value, int oldValue) {
        for (IntChangedListener vc : valueChangedListeners) {
            vc.valueChanged(value,oldValue);
        }
    }
    
    @Override
    void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT) {
            inDialState = true;
        }
    }
    
    @Override
    void fireMouseReleased(MouseButtonEvent e) {
        super.fireMouseReleased(e);
        if (e.button==Input.MB_LEFT) {
            inDialState = false;
        }
    }
    
    @Override
    void fireMouseMoved(MouseMotionEvent e) {
        super.fireMouseMoved(e);
        if (inDialState) {
            //TODO change this logic for better dial controls
            int dx = e.x - e.prevX;
            int oldValue = value;
            value += dx;
            if (value > maxValue) {
                value = wrap ? minValue : maxValue;
            }
            else if (value < minValue) {
                value = wrap ? maxValue : minValue;
            }
            if (value!=oldValue) {
                fireValueChanged(value,oldValue);
            }
        }
    }
    
    @Override
    void fireMouseExit(MouseMotionEvent e) {
        super.fireMouseExit(e);
        inDialState = false;
    }

    @Override
    void paint(Graphics g) {
        super.paint(g);
        
        float min = Mathf.min(getWidth(),getHeight());
        float ratio = 0.7f;
        float dim = min * ratio;
        float x = getWidth()/2f - dim/2;
        float y = getHeight()/2f - dim/2;
        g.renderCircle(x,y,dim,dim,Style.get(this).buttonColorHover);
        Matrix4 mat = Matrix4.identity();
        
        mat.translate(getWidth()/2f,getHeight()/2f,0);
        float alpha = Mathf.alpha(getValue(),minValue,maxValue);
        
        mat.rotateZ(alpha*Mathf.TWO_PI);
        
        mat.translate(dim/2,0,0);
        mat.scale(dim/10,dim/10,1);
        g.renderRect(mat,Style.get(this).buttonColor);
    }
    
}
