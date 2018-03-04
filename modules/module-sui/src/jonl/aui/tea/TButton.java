package jonl.aui.tea;

import jonl.aui.Button;
import jonl.aui.HAlign;
import jonl.aui.Icon;
import jonl.aui.Signal;
import jonl.aui.VAlign;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.vmath.Vector4;

public class TButton extends TWidget implements Button {

    private String text = "";
    private TIcon icon = null;
    private boolean checkable = false;
    private boolean checked = false;
    
    private final Signal<Callback0D> pressed = new Signal<>();
    private final Signal<Callback0D> released = new Signal<>();
    private final Signal<Callback0D> clicked = new Signal<>();
    private final Signal<Callback<Boolean>> toggled = new Signal<>();
    
    private boolean isMouseWithin = false;
    private float intensityValue = 0;
    private float maxValue = 30;
    
    public TButton() {
        
    }
    
    public TButton(String text) {
        this.text = text;
    }
    
    @Override
    public String text() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Icon icon() {
        return icon;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = (TIcon) icon;
    }

    @Override
    public boolean checkable() {
        return checkable;
    }

    @Override
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    @Override
    public boolean checked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = true;
    }

    @Override
    public void toggle() {
        if (checkable()) {
            checked = !checked;
        }
    }

    @Override
    public Signal<Callback0D> pressed() { return pressed; }

    @Override
    public Signal<Callback0D> released() { return released; }

    @Override
    public Signal<Callback0D> clicked() { return clicked; }

    @Override
    public Signal<Callback<Boolean>> toggled() { return toggled; }

    // ------------------------------------------------------------------------
    
    @Override
    protected void paint(TGraphics g) {
        super.paint(g);
        
        if (isMouseWithin) {
            if (intensityValue<maxValue) {
                intensityValue++;
            }
        } else {
            if (intensityValue>0) {
                intensityValue--;
            }
        }
        
        float v = intensityValue / maxValue;
        Vector4 col = TStyle.get(this).buttonColor.get().lerp(TStyle.get(this).buttonColorHover,v);
        g.renderRect(0,0,width,height,col);
        float x = width/2;
        float y = height/2;
        g.renderText(text(),x,y,HAlign.CENTER,VAlign.MIDDLE,TStyle.get(this).calibri,new Vector4(0,0,0,1));
    }
    
    @Override
    protected void handleMouseButtonClick(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            clicked().emit(cb->cb.f());
            if (checkable()) {
                checked = !checked;
                toggled().emit(cb->cb.f(checked));
            }
        }
    }
    
    @Override
    protected void handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            pressed().emit(cb->cb.f());
        }
    }
    
    @Override
    protected void handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            released().emit(cb->cb.f());
        }
    }
    
    @Override
    protected void handleMouseEnter(TMouseEvent event) {
        isMouseWithin = true;
    }
    
    @Override
    protected void handleMouseExit(TMouseEvent event) {
        isMouseWithin = false;
    }
    
}
