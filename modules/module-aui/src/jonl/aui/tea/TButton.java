package jonl.aui.tea;

import jonl.aui.Button;
import jonl.aui.Icon;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;

public class TButton extends TWidget implements Button {

    protected String text = "";
    protected TIcon icon = null;
    protected boolean checkable = false;
    protected boolean checked = false;
    
    private final Signal<Callback0D> pressed = new Signal<>();
    private final Signal<Callback0D> released = new Signal<>();
    private final Signal<Callback0D> clicked = new Signal<>();
    private final Signal<Callback<Boolean>> toggled = new Signal<>();
    
    public TButton() {
        super();
        this.setSizePolicy(new SizePolicy(SizePolicy.MINIMUM, SizePolicy.FIXED));
    }
    
    public TButton(String text) {
        this();
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
    public TIcon icon() {
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
        this.checked = checked;;
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
    protected TSizeHint sizeHint() {
        return style().button().getSizeHint(this,info());
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().button().paint(this,info(),g);
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonClick(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            clicked().emit(cb->cb.f());
            if (checkable()) {
                checked = !checked;
                toggled().emit(cb->cb.f(checked));
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            pressed().emit(cb->cb.f());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            released().emit(cb->cb.f());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseEnter(TMouseEvent event) {
        info().put("bIsMouseWithin", true);
        return true;
    }
    
    @Override
    protected boolean handleMouseExit(TMouseEvent event) {
        info().put("bIsMouseWithin", false);
        return true;
    }
    
}
