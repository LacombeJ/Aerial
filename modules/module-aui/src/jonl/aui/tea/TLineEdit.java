package jonl.aui.tea;

import jonl.aui.HAlign;
import jonl.aui.LineEdit;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.VAlign;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jutils.func.Callback;
import jonl.vmath.Color;
import jonl.vmath.Vector4;

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
    
    @Override
    protected void paint(TGraphics g) {
        
        float fIntensityValue = info().get("fIntensityValue", 0f);
        
        float fMaxValue = info().get("fMaxValue", 30);
        Color cNormal = info().get("cNormal", Color.BLACK);
        Color cHover = info().get("cHover", Color.fromInt(0, 0, 255));
        
        if (info().get("bIsMouseWithin", false)) {
            if (fIntensityValue<fMaxValue) {
                fIntensityValue++;
                
            }
        } else {
            if (fIntensityValue>0) {
                fIntensityValue--;
            }
        }
        
        info().put("fIntensityValue", fIntensityValue);
        
        float v = fIntensityValue / fMaxValue;
        Vector4 primary = cNormal.toVector();
        Vector4 secondary = cHover.toVector();
        Vector4 color = primary.lerp(secondary, v);
        
        float x = border/2;
        float y = height/2;
        
        g.renderRect(0, 0, width, height, Color.WHITE);
        g.renderRectOutline(0, 0, width, height, color);
        g.renderText(text(),x,y,HAlign.LEFT,VAlign.MIDDLE,style().font(),Color.BLACK);
        
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
