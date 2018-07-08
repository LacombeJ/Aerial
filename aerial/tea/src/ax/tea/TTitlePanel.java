package ax.tea;

import ax.aui.Align;
import ax.aui.Layout;
import ax.aui.TitlePanel;
import ax.tea.graphics.TitlePanelRenderer;

public class TTitlePanel extends TPanel implements TitlePanel {

    String title = "";
    
    public TTitlePanel(String title, Layout layout) {
        super(layout);
        setTitle(title);
        layout.setMargin(9,9,27,9);
    }
    
    public TTitlePanel(String title) {
        this(title, new TListLayout(Align.VERTICAL));
    }
    
    @Override
    public String title() {
        return title;
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void paint(TGraphics g) {
        TitlePanelRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
