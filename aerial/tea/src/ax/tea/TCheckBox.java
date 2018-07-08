package ax.tea;

import ax.aui.CheckBox;
import ax.tea.graphics.CheckBoxRenderer;

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
        return TSizeReasoning.checkBox(this);
    }
    
    @Override
    protected void paint(TGraphics g) {
        CheckBoxRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
