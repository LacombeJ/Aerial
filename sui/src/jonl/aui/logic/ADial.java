package jonl.aui.logic;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Dial;
import jonl.aui.IntChangedListener;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.jgl.Input;
import jonl.jutils.func.Tuple2i;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;

public abstract class ADial extends AWidget implements Dial {

    int minValue = 0;
    int maxValue = 100;
    int value;
    boolean wrap = true;
    
    float ox = 0;
    float oy = 0;
    int ovalue = 0;
    boolean inDialState = false;
    
    List<IntChangedListener> valueChangedListeners = new ArrayList<>();
    
    @Override
    public int getValue() {
        return value;
    }
    
    @Override
    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public int getMinValue() {
        return minValue;
    }
    
    @Override
    public void setMinValue(int min) {
        minValue = min;;
    }
    
    @Override
    public int getMaxValue() {
        return maxValue;
    }
    
    @Override
    public void setMaxValue(int max) {
        maxValue = max;;
    }

    @Override
    public void addValueChangedListener(IntChangedListener vc) {
        valueChangedListeners.add(vc);
    }
    
    protected void fireValueChanged(int value, int oldValue) {
        for (IntChangedListener vc : valueChangedListeners) {
            vc.valueChanged(value,oldValue);
        }
    }
    
    @Override
    protected void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        if (e.button==Input.MB_LEFT) {
            inDialState = true;
            ox = e.x;
            oy = e.y;
            ovalue = value;
        }
    }
    
    @Override
    protected void fireGlobalMouseReleased(MouseButtonEvent e) {
        super.fireGlobalMouseReleased(e);
        if (e.button==Input.MB_LEFT) {
            inDialState = false;
        }
    }
    
    @Override
    protected void fireGlobalMouseMoved(MouseMotionEvent e) {
        super.fireGlobalMouseMoved(e);
        if (inDialState) {
            float x = getWidth()/2f;
            float y = getHeight()/2f;
            
            Tuple2i local = fromGlobalSpace(e.x,e.y);
            
            Vector2 u = new Vector2(ox-x,oy-y);
            Vector2 v = new Vector2(local.x-x,local.y-y);
            
            float ang = Vector2.rad(u,v);
            int oldValue = value;
            int diff = (int)((ang/Mathf.TWO_PI) * 100);
            value = ovalue + diff;
            
            if (value > maxValue) {
                value = (int) Mathf.pattern(value, minValue, maxValue);
            }
            else if (value < minValue) {
                value = (int) Mathf.pattern(value, minValue, maxValue);
            }
            if (value!=oldValue) {
                fireValueChanged(value,oldValue);
            }
        }
    }
    
    @Override
    protected void fireGlobalMouseExit(MouseMotionEvent e) {
        super.fireGlobalMouseExit(e);
        inDialState = false;
    }
    
}
