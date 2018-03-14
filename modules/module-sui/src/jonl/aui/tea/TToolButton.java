package jonl.aui.tea;

import jonl.aui.Icon;
import jonl.aui.ToolButton;

public class TToolButton extends TButton implements ToolButton {
    
    public TToolButton() {
        super();
        // Set manual style policy
        this.setMinSize(32, 32);
        this.setPreferredSize(32, 32);
        this.setMaxSize(32, 32);
    }
    
    public TToolButton(Icon icon) {
        super();
        this.setIcon(icon);
        // Set manual style policy
        this.setMinSize(32, 32);
        this.setPreferredSize(32, 32);
        this.setMaxSize(32, 32);
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().toolButton().paint(this,info,g);
        paint().emit(cb->cb.f(g));
    }
    
}
