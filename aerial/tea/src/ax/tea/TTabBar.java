package ax.tea;

import ax.aui.Align;
import ax.aui.Button;
import ax.tea.graphics.ButtonRenderer;
import ax.tea.graphics.WidgetRenderer;

public class TTabBar extends TButtonBar {

    TNewTabButton newTabButton = null;
    boolean hasNewTabButton = false;
    
    public TTabBar() {
        super(Align.HORIZONTAL);
        newTabButton = new TNewTabButton();
        newTabButton.setMaxSize(30,Integer.MAX_VALUE);
        newTabButton.setCheckable(false);
    }
    
    @Override
    public void add(Button button) {
        if (hasNewTabButton) {
            super.remove(newTabButton);
        }
        super.add(button);
        if (hasNewTabButton) {
            super.add(newTabButton);
        }
    }
    
    void addNewTabButton() {
        if (!hasNewTabButton) {
            super.add(newTabButton);
            hasNewTabButton = true;
        }
    }
    
    void removeNewTabButton() {
        if (hasNewTabButton) {
            super.remove(newTabButton);
            hasNewTabButton = false;
        }
    }
    
    @Override
    protected void paint(TGraphics g) {
        WidgetRenderer.paint(this,"TabPanel.Bar",g,info());
        paint().emit(cb->cb.f(g));
    }
    
    static class TNewTabButton extends TTabButton {
        public TNewTabButton() {
            super("");
        }
        @Override
        protected void paint(TGraphics g) {
            ButtonRenderer.paint(this,"TabPanel.New.Button",g,info());
            paint().emit(cb->cb.f(g));
        }
    }
    

}
