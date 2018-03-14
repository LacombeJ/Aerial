package jonl.aui.tea;

import jonl.aui.Margin;
import jonl.aui.tea.graphics.TColor;

public class TTabContent extends TPanel {

    public TTabContent() {
        super();
        TFillLayout tabContentLayout = new TFillLayout();
        tabContentLayout.setMargin(9, 9, 9, 9);
        setWidgetLayout(tabContentLayout);
    }
    
    @Override
    protected void paint(TGraphics g) {
        TGraphics tg = (TGraphics)g;
        TLayout layout = widgetLayout();
        Margin margin = layout.margin();
        tg.renderRect(0, 0, width(), height(), style().tabButton().toggleColor());
        tg.renderRect(margin.left, margin.bottom, width()-margin.width(), height()-margin.height(), TColor.WHITE);
    }
    
}
