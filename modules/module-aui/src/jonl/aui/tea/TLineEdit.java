package jonl.aui.tea;

import jonl.aui.LineEdit;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.LineEditRenderer;
import jonl.jutils.func.Callback;

public class TLineEdit extends TWidget implements LineEdit {

    private String text = "";
    
    private int border = 4;
    
    private Signal<Callback<String>> changed = new Signal<>();
    
    public TLineEdit() {
        super();
        setSizePolicy(new SizePolicy(SizePolicy.EXPANDING, SizePolicy.FIXED));
    }
    
    public TLineEdit(String text) {
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
        invalidateSizeHint();
    }
    
    @Override
    public Signal<Callback<String>> changed() { return changed; }

    // ------------------------------------------------------------------------
    
    @Override
    protected TSizeHint sizeHint() {
        TSizeHint hint = new TSizeHint();
        hint.width = style().font().getWidth(text) + border;
        hint.height = style().font().getHeight() + border;
        return hint;
    }
    
    protected void paint(TGraphics g) {
        LineEditRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseEnter(TMouseEvent event) {
        info().put("bIsMouseWithin", true);
        setCursor(TCursor.IBEAM);
        return true;
    }
    
    @Override
    protected boolean handleMouseExit(TMouseEvent event) {
        info().put("bIsMouseWithin", false);
        setCursor(TCursor.ARROW);
        return true;
    }
    
}
