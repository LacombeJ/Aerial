package jonl.aui.tea;

import jonl.aui.Dial;
import jonl.aui.Signal;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.vmath.Mathf;
import jonl.vmath.Mathi;
import jonl.vmath.Vector2;

public class TDial extends TWidget implements Dial {

    private int minValue = 0;
    private int maxValue = 100;
    private int value;
    
    private float ox = 0;
    private float oy = 0;
    private int ovalue = 0;
    private boolean inAdjustState = false;
    
    private final Signal<Callback<Integer>> moved = new Signal<>();
    private final Signal<Callback<Integer>> changed = new Signal<>();
    private final Signal<Callback0D> pressed = new Signal<>();
    private final Signal<Callback0D> released = new Signal<>();
    
    public TDial() {
        super();
        setMouseFocusSupport(true);
    }
    
    @Override
    public int value() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = Mathi.clamp(value, minValue, maxValue);
    }

    @Override
    public int min() {
        return minValue;
    }

    @Override
    public void setMin(int min) {
        minValue = min;
        setValue(minValue);
    }

    @Override
    public int max() {
        return maxValue;
    }

    @Override
    public void setMax(int max) {
        maxValue = max;
        setValue(maxValue);
    }

    @Override
    public Signal<Callback<Integer>> moved() { return moved; }

    @Override
    public Signal<Callback<Integer>> changed() { return changed; }

    @Override
    public Signal<Callback0D> pressed() { return pressed; }

    @Override
    public Signal<Callback0D> released() { return released; }
    
    // ------------------------------------------------------------------------
    
    @Override
    protected TSizeHint sizeHint() {
        return style().dial().getSizeHint(this,info());
    }
    
    @Override
    protected void paint(TGraphics g) {
        info.put("bInAdjustState", inAdjustState);
        style().dial().paint(this, info(), g);
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = true;
            ox = event.x;
            oy = event.y;
            ovalue = value;
            pressed().emit(cb->cb.f());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = false;
            released().emit(cb->cb.f());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseMove(TMouseEvent event) {
        if (inAdjustState) {
            float x = width()/2f;
            float y = height()/2f;
            
            Vector2 u = new Vector2(ox-x,oy-y);
            Vector2 v = new Vector2(event.x-x, event.y-y);
            
            float ang = Vector2.rad(u,v);
            int oldValue = value;
            int diff = (int)((ang/Mathf.TWO_PI) * (maxValue-minValue));
            
            value = ovalue + diff;
            
            value = (int) Mathf.pattern(value, minValue, maxValue);
            
            if (value!=oldValue) {
                moved().emit(cb->cb.f(value));
                changed().emit(cb->cb.f(value));
            }
            return true;
        }
        return false;
    }

}
