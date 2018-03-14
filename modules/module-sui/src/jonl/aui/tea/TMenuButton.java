package jonl.aui.tea;

import jonl.aui.MenuButton;

public class TMenuButton extends TButton implements MenuButton {

    public TMenuButton(String text) {
        super(text);
    }
    
    public TMenuButton() {
        this("");
    }
    
    @Override
    protected TSizePolicy getSizePolicy() {
        return style().menuButton().getSizePolicy(this,info);
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().menuButton().paint(this,info,g);
        paint().emit(cb->cb.f(g));
    }
    
}
