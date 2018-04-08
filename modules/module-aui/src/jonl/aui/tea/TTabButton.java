package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Margin;
import jonl.vmath.Mathi;

public class TTabButton extends TRadioButton {

    private int border = 4;
    private int width = 70;
    private int height = 24;
    
    TButton closeButton;
    private boolean closeButtonEnabled = false;
    
    public TTabButton(String text) {
        super(text);
        closeButton = new TButton("X");
        TLayout layout = new TListLayout(Align.HORIZONTAL,Margin.ZERO,0);
        layout.add(new TSpacer());
        setWidgetLayout(layout);
    }
    
    @Override
    protected TSizeHint sizeHint() {
        TSizeHint hint = new TSizeHint();
        if (icon()!=null) {
            hint.width = Math.max(hint.width, icon().width()) + border;
            hint.height = Math.max(hint.height, icon().height()) + border;
        }
        hint.width = Mathi.max(width, hint.width, (int) style().font().getWidth(text()) + 4*border + 16); //For possible close button
        hint.height = Mathi.max(height, hint.height, (int) style().font().getHeight() + border);
        return hint;
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().tabButton().paint(this,info(),g);
        
        paint().emit(cb->cb.f(g));
    }
    
    void setCloseButton(boolean enable) {
        if (closeButtonEnabled && !enable) {
            widgetLayout().remove(closeButton);
            closeButtonEnabled = false;
        } else if (!closeButtonEnabled && enable) {
            widgetLayout().add(closeButton);
            closeButtonEnabled = true;
        }
    }
    
}
