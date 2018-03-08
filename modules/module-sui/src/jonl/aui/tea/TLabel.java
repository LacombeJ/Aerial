package jonl.aui.tea;

import jonl.aui.HAlign;
import jonl.aui.Label;
import jonl.aui.VAlign;
import jonl.vmath.Vector4;

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
        sp.minWidth = (int) TOldStyle.get(this).calibri.getWidth(text) + border;
        sp.prefWidth = sp.minWidth;
        sp.prefHeight = (int) TOldStyle.get(this).calibri.getHeight() + border;
        return sp;
    }
    
    @Override
    protected void paint(TGraphics g) {
        super.paint(g);
        float x = width/2;
        float y = height/2;
        g.renderText(text(),x,y,HAlign.CENTER,VAlign.MIDDLE,TOldStyle.get(this).calibri,new Vector4(0,0,0,1));
    }
    
}
