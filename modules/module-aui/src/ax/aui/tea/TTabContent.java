package ax.aui.tea;

import ax.aui.tea.graphics.TabContentRenderer;

public class TTabContent extends TPanel {

    public TTabContent() {
        super();
        TFillLayout tabContentLayout = new TFillLayout();
        tabContentLayout.setMargin(2,2,2,2);
        setWidgetLayout(tabContentLayout);
    }
    
    @Override
    protected void paint(TGraphics g) {
        TabContentRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
