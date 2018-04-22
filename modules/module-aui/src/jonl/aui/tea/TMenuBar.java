package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Menu;
import jonl.aui.MenuBar;
import jonl.aui.tea.graphics.WidgetRenderer;

public class TMenuBar extends TButtonBar implements MenuBar {
    
    public TMenuBar(Align align) {
        super(align);
    }
    
    public TMenuBar() {
        super(Align.HORIZONTAL);
    }
    
    @Override
    public void add(Menu menu) {
        super.add((TMenu)menu);
    }

    @Override
    public void addSeparator() {
        super.addSeparator();
    }
    
    @Override
    protected void paint(TGraphics g) {
        WidgetRenderer.paint(this,"Menu.Bar",g,info());
        paint().emit(cb->cb.f(g));
    }
    
}
