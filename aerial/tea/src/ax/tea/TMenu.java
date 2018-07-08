package ax.tea;

import ax.aui.Align;
import ax.aui.Menu;
import ax.graphics.Input;
import ax.tea.event.TMouseEvent;
import ax.tea.graphics.ButtonRenderer;
import ax.tea.graphics.WidgetRenderer;

public class TMenu extends TButton implements Menu  {

    //TODO do not extend button
    
    TSubMenuBar parentBar;
    TSubMenuBar bar = new TSubMenuBar(Align.VERTICAL);
    
    public TMenu(String text) {
        super(text);
        
    }
    
    @Override
    public void add(Menu menu) {
        bar.add(menu);
        ((TMenu)menu).parentBar = bar;
    }

    @Override
    public void addSeparator() {
        bar.addSeparator();
    }
    
    @Override
    protected void paint(TGraphics g) {
        ButtonRenderer.paint(this,"Menu.Button",g,info());
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonClick(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            clicked().emit(cb->cb.f());
            if (bar.count()!=0) {
                rootPanel().add(bar,0,height(),this);
            } else if (parentBar!=null) {
                rootPanel().remove(parentBar);
            }
            if (checkable()) {
                checked = !checked;
                toggled().emit(cb->cb.f(checked));
            }
            return true;
        }
        return false;
    }
    
    class TSubMenuBar extends TMenuBar {

        public TSubMenuBar(Align align) {
            super(align);
            widgetLayout().setMargin(2,2,2,2);
        }
        
        @Override
        protected void paint(TGraphics g) {
            WidgetRenderer.paint(this,"Menu",g,info());
            paint().emit(cb->cb.f(g));
        }
        
    }

}
