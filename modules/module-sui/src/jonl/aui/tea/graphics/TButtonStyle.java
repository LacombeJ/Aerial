package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TSizePolicy;
import jonl.vmath.Vector4;

public class TButtonStyle extends TWidgetStyle<TButton> {
    
    private TColor textColor;
    private TColor buttonColor;
    private TColor hoverColor;
    private TColor toggleColor;
    private int border = 4;
    private float maxValue = 30;
    
    public TButtonStyle(TStyle style) {
        super(style);
        
        textColor = style.textColor();
        buttonColor = style.primary();
        hoverColor = style.secondary();
        toggleColor = style.tertiary();
        
        this.setPainter((button,i,g)->{
            TButtonInfo info = (TButtonInfo)i;
            
            if (info.isMouseWithin) {
                if (info.intensityValue<maxValue) {
                    info.intensityValue++;
                }
            } else {
                if (info.intensityValue>0) {
                    info.intensityValue--;
                }
            }
            
            float v = info.intensityValue / maxValue;
            Vector4 primary = (button.checked()) ? toggleColor.toVector() : buttonColor.toVector();
            Vector4 secondary = (button.checked()) ? toggleColor.toVector() : hoverColor.toVector();
            Vector4 color = primary.lerp(secondary, v);
            
            g.renderRect(0,0,button.width(),button.height(),TColor.fromVector(color));
            float x = button.width()/2;
            float y = button.height()/2;
            if (button.icon()!=null) {
                g.renderImage((TImage) button.icon(), x, y);
            }
            g.renderText(button.text(),x,y,HAlign.CENTER,VAlign.MIDDLE,style().font(),textColor);
        });
        
        this.setSizePolicy((button,i)->{
            TSizePolicy sp = new TSizePolicy();
            if (button.icon()!=null) {
                sp.minWidth = Math.max(sp.minWidth, button.icon().width()) + border;
                sp.minHeight = Math.max(sp.minHeight, button.icon().height()) + border;
                sp.prefWidth = Math.max(sp.prefWidth, button.icon().width()) + border;
                sp.prefHeight = Math.max(sp.prefHeight, button.icon().height()) + border;
            }
            sp.minWidth = Math.max(sp.minWidth, (int) style().font().getWidth(button.text()) + 2*border);
            sp.prefHeight = Math.max(sp.prefHeight, (int) style().font().getHeight() + border);
            return sp;
        });
        
    }
    
    public TColor textColor() { return textColor; }
    public TColor buttonColor() { return buttonColor; }
    public TColor hoverColor() { return hoverColor; }
    public TColor toggleColor() { return toggleColor; }
    
    public void textColor(TColor textColor) { this.textColor = textColor; }
    public void buttonColor(TColor buttonColor) { this.buttonColor = buttonColor; }
    public void hoverColor(TColor hoverColor) { this.hoverColor = hoverColor; }
    public void toggleColor(TColor toggleColor) { this.toggleColor = toggleColor; }
    

}
