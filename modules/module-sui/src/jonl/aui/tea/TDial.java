package jonl.aui.tea;

import jonl.aui.Dial;
import jonl.aui.Signal;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;

public class TDial extends TWidget implements Dial {

    private int minValue = 0;
    private int maxValue = 100;
    private int value;
    
    private float ox = 0;
    private float oy = 0;
    private int ovalue = 0;
    private boolean inDialState = false;
    
    private final Signal<Callback<Integer>> moved = new Signal<>();
    private final Signal<Callback<Integer>> changed = new Signal<>();
    private final Signal<Callback0D> pressed = new Signal<>();
    private final Signal<Callback0D> released = new Signal<>();
    
    public TDial() {
        setMouseFocusSupport(true);
    }
    
    @Override
    public int value() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int min() {
        return minValue;
    }

    @Override
    public void setMin(int min) {
        minValue = min;
    }

    @Override
    public int max() {
        return maxValue;
    }

    @Override
    public void setMax(int max) {
        maxValue = max;
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
    protected void paint(TGraphics g) {
        super.paint(g);
        
        float min = Mathf.min(width(),height());
        float ratio = 0.7f;
        float dim = min * ratio;
        float x = width()/2f - dim/2;
        float y = height()/2f - dim/2;
        g.renderCircle(x,y,dim,dim,TStyle.get(this).buttonColorHover);
        Matrix4 mat = Matrix4.identity();
        
        mat.translate(width()/2f,height()/2f,0);
        float alpha = Mathf.alpha(value(),min(),max());
        
        mat.rotateZ(alpha*Mathf.TWO_PI);
        
        mat.translate(dim/2,0,0);
        mat.scale(dim/10,dim/10,1);
        g.renderRect(mat,TStyle.get(this).buttonColor);
    }
    
    @Override
    protected void handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inDialState = true;
            ox = event.x;
            oy = event.y;
            ovalue = value;
            pressed().emit(cb->cb.f());
        }
    }
    
    @Override
    protected void handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inDialState = false;
            released().emit(cb->cb.f());
        }
    }
    
    @Override
    protected void handleMouseMove(TMouseEvent event) {
        if (inDialState) {
            float x = width()/2f;
            float y = height()/2f;
            
            Vector2 u = new Vector2(ox-x,oy-y);
            Vector2 v = new Vector2(event.x-x, event.y-y);
            
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
                moved().emit(cb->cb.f(value));
                changed().emit(cb->cb.f(value));
            }
        }
    }

}
