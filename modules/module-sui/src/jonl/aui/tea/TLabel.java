package jonl.aui.tea;

import jonl.aui.HAlign;
import jonl.aui.Label;
import jonl.aui.VAlign;

public class TLabel extends TWidget implements Label {

    private String text = "";
    
    private int border = 4;
    
    public TLabel() {
        super();
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
    protected TSizePolicy getSizePolicy() {
        TSizePolicy sp = new TSizePolicy();
        sp.minWidth = style().font().getWidth(text) + border;
        sp.prefWidth = sp.minWidth;
        sp.prefHeight = style().font().getHeight() + border;
        return sp;
    }
    
    @Override
    protected void paint(TGraphics g) {
        float x = width/2;
        float y = height/2;
        g.renderText(text(),x,y,HAlign.CENTER,VAlign.MIDDLE,style().font(),style().textColor());
        
        paint().emit(cb->cb.f(g));
    }
    
}
