package ax.tea;

import ax.aui.Align;
import ax.aui.Margin;
import ax.tea.graphics.ButtonRenderer;

public class TTabButton extends TRadioButton {

    TCloseTabButton closeButton;
    private boolean closeButtonEnabled = false;
    
    public TTabButton(String text) {
        super(text);
        closeButton = new TCloseTabButton();
        closeButton.setSizeConstraint(20,20);
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
    
    static class TCloseTabButton extends TButton {
        @Override
        protected void paint(TGraphics g) {
            ButtonRenderer.paint(this,"TabPanel.Close.Button",g,info());
            paint().emit(cb->cb.f(g));
        }
    }
    
}
