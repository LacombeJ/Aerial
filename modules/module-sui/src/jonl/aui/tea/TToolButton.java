package jonl.aui.tea;

import jonl.aui.Icon;
import jonl.aui.ToolButton;

public class TToolButton extends TButton implements ToolButton {
    
    public TToolButton() {
        super();
    }
    
    public TToolButton(Icon icon) {
        super();
        this.setIcon(icon);
    }
    
    @Override
    public TSizeHint sizeHint() {
        return new TSizeHint(32,32);
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().toolButton().paint(this,info,g);
        paint().emit(cb->cb.f(g));
    }
    
}
