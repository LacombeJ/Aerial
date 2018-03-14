package jonl.aui.tea;

public class TTabButton extends TRadioButton {

    public TTabButton(String text) {
        super(text);
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().tabButton().paint(this,info,g);
        
        paint().emit(cb->cb.f(g));
    }
    
}
