package jonl.aui.tea;

import jonl.aui.CheckBox;
import jonl.aui.tea.graphics.CheckBoxRenderer;

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
        CheckBoxRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
