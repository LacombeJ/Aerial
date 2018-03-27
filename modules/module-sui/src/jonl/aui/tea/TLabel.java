package jonl.aui.tea;

import jonl.aui.HAlign;
import jonl.aui.Label;
import jonl.aui.SizePolicy;
import jonl.aui.VAlign;

public class TLabel extends TWidget implements Label {

    private String text = "";
    
    private int border = 4;
    
    public TLabel() {
        super();
        setSizePolicy(new SizePolicy(SizePolicy.MINIMUM, SizePolicy.FIXED));
    }
    
    public TLabel(String text) {
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
        float x = border/2;
        float y = height/2;
        g.renderText(text(),x,y,HAlign.LEFT,VAlign.MIDDLE,style().font(),style().textColor());
        
        paint().emit(cb->cb.f(g));
    }
    
}
