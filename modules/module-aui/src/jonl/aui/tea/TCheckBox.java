package jonl.aui.tea;

import jonl.aui.CheckBox;

public class TCheckBox extends TButton implements CheckBox {

    public TCheckBox() {
        super();
        setCheckable(true);
    }
    
    public TCheckBox(String text) {
        super(text);
        setCheckable(true);
    }
    
    @Override
    protected TSizeHint sizeHint() {
        return style().checkBox().getSizeHint(this,info());
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().checkBox().paint(this,info(),g);
        paint().emit(cb->cb.f(g));
    }
    
}
