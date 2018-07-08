package ax.tea;

import ax.aui.Align;
import ax.aui.ToolBar;
import ax.aui.ToolButton;
import ax.tea.graphics.WidgetRenderer;

public class TToolBar extends TButtonBar implements ToolBar {
    
    public TToolBar() {
        super(Align.HORIZONTAL);
        widgetLayout().setMargin(4, 4, 4, 4);
        widgetLayout().setSpacing(4);
    }
    
    @Override
    public TToolButton get(int index) {
        return (TToolButton) super.get(index);
    }

    @Override
    public void add(ToolButton button) {
        super.add(button);
    }

    @Override
    public void remove(ToolButton button) {
        super.remove(button);
    }

    @Override
    protected void paint(TGraphics g) {
        WidgetRenderer.paint(this,"ToolBar",g,info());
        paint().emit(cb->cb.f(g));
    }

}
