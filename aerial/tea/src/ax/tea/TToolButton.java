package ax.tea;

import ax.aui.Icon;
import ax.aui.Resource;
import ax.aui.ToolButton;
import ax.tea.graphics.ButtonRenderer;

public class TToolButton extends TButton implements ToolButton {
    
    public TToolButton() {
        super();
    }
    
    public TToolButton(Icon icon) {
        super();
        this.setIcon(icon);
    }
    
    public TToolButton(Resource icon) {
        this((Icon)icon.data());
    }
    
    @Override
    public TSizeHint sizeHint() {
        return new TSizeHint(32,32);
    }
    
    @Override
    protected void paint(TGraphics g) {
        ButtonRenderer.paint(this,"ToolButton",g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
