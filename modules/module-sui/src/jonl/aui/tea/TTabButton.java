package jonl.aui.tea;

import jonl.vmath.Mathf;

public class TTabButton extends TRadioButton {

    private int border = 8;
    private int minWidth = 70;
    private int minHeight = 32;
    
    public TTabButton(String text) {
        super(text);
    }
    
    @Override
    protected TSizePolicy getSizePolicy() {
        // Expand the buffer policy
        TSizePolicy sp = new TSizePolicy();
        if (icon()!=null) {
            sp.minWidth = Math.max(sp.minWidth, icon().width()) + border;
            sp.minHeight = Math.max(sp.minHeight, icon().height()) + border;
            sp.prefWidth = Math.max(sp.prefWidth, icon().width()) + border;
            sp.prefHeight = Math.max(sp.prefHeight, icon().height()) + border;
        }
        sp.minWidth = Mathf.max(minWidth, sp.minWidth, (int) style().font().getWidth(text()) + 2*border);
        sp.prefHeight = Mathf.max(minHeight, sp.prefHeight, (int) style().font().getHeight() + border);
        return sp;
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().tabButton().paint(this,info,g);
        
        paint().emit(cb->cb.f(g));
    }
    
}
