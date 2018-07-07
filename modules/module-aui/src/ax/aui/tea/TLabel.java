package ax.aui.tea;

import ax.aui.Label;
import ax.aui.SizePolicy;
import ax.aui.tea.graphics.LabelRenderer;

public class TLabel extends TWidget implements Label {

    private String text = "";
    
    public TLabel() {
        super();
        setSizePolicy(new SizePolicy(SizePolicy.MINIMUM, SizePolicy.FIXED));
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
    protected TSizeHint sizeHint() {
        return TSizeReasoning.label(this);
    }
    
    protected void paint(TGraphics g) {
        LabelRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
