package jonl.aui.tea;

import jonl.vmath.Mathi;

public class TTabButton extends TRadioButton {

    private int border = 8;
    private int width = 70;
    private int height = 32;
    
    public TTabButton(String text) {
        super(text);
    }
    
    @Override
    protected TSizeHint sizeHint() {
        TSizeHint hint = new TSizeHint();
        if (icon()!=null) {
            hint.width = Math.max(hint.width, icon().width()) + border;
            hint.height = Math.max(hint.height, icon().height()) + border;
        }
        hint.width = Mathi.max(width, hint.width, (int) style().font().getWidth(text()) + 2*border);
        hint.height = Mathi.max(height, hint.height, (int) style().font().getHeight() + border);
        return hint;
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().tabButton().paint(this,info(),g);
        
        paint().emit(cb->cb.f(g));
    }
    
}
