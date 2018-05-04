package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Margin;
import jonl.aui.tea.graphics.ButtonRenderer;

public class TTabButton extends TRadioButton {

    TButton closeButton;
    private boolean closeButtonEnabled = false;
    
    public TTabButton(String text) {
        super(text);
        closeButton = new TButton("X");
        TLayout layout = new TListLayout(Align.HORIZONTAL,Margin.ZERO,0);
        layout.add(new TSpacer());
        setWidgetLayout(layout);
    }
    
    @Override
    protected TSizeHint sizeHint() {
        return TSizeReasoning.tabButton(this);
    }
    
    @Override
    protected void paint(TGraphics g) {
        ButtonRenderer.paint(this,"TabPanel.Button",g,info());
        paint().emit(cb->cb.f(g));
    }
    
    void setCloseButton(boolean enable) {
        if (closeButtonEnabled && !enable) {
            widgetLayout().remove(closeButton);
            closeButtonEnabled = false;
        } else if (!closeButtonEnabled && enable) {
            widgetLayout().add(closeButton);
            closeButtonEnabled = true;
        }
    }
    
}
