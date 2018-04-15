package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Menu;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;

public class TMenu extends TButton implements Menu  {

    //TODO do not extend button
    
    TMenuBar bar = new TMenuBar(Align.VERTICAL);
    
    public TMenu(String text) {
        super(text);
        
    }
    
    @Override
    public void add(Menu menu) {
        bar.add(menu);
    }

    @Override
    public void addSeparator() {
        bar.addSeparator();
    }
    
    @Override
    protected TSizeHint sizeHint() {
        return style().menuButton().getSizeHint(this,info());
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().menuButton().paint(this,info(),g);
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonClick(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            clicked().emit(cb->cb.f());
            rootPanel().add(bar,0,height(),this);
            if (checkable()) {
                checked = !checked;
                toggled().emit(cb->cb.f(checked));
            }
            return true;
        }
        return false;
    }

}
