package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TSizeHint;
import jonl.vmath.Color;
import jonl.vmath.Vector4;

public class TButtonStyle extends TWidgetStyle<TButton> {
    
    private Color textColor;
    private Color buttonColor;
    private Color hoverColor;
    private Color toggleColor;
    private int border = 4;
    private float maxValue = 30;
    
    public TButtonStyle(TStyle style) {
        super(style);
        
        textColor = style.textColor();
        buttonColor = style.primary();
        hoverColor = style.secondary();
        toggleColor = style.tertiary();
        
        this.setPainter((button,info,g)->{
            
            float fIntensityValue = info.get("fIntensityValue", 0f);
            
            float fMaxValue = info.get("fMaxValue", maxValue);
            Color cButton = info.get("cButton", buttonColor);
            Color cHover = info.get("cHover", hoverColor);
            Color cToggle = info.get("cHover", toggleColor);
            HAlign halign = info.get("halign", HAlign.CENTER);
            VAlign valign = info.get("valign", VAlign.MIDDLE);
            float xoffset = info.get("xoffset", 0f);
            
            if (info.get("bIsMouseWithin", false)) {
                if (fIntensityValue<fMaxValue) {
                    fIntensityValue++;
                    
                }
            } else {
                if (fIntensityValue>0) {
                    fIntensityValue--;
                }
            }
            
            info.put("fIntensityValue", fIntensityValue);
            
            float v = fIntensityValue / fMaxValue;
            Vector4 primary = (button.checked()) ? cToggle.toVector() : cButton.toVector();
            Vector4 secondary = (button.checked()) ? cToggle.toVector() : cHover.toVector();
            Vector4 color = primary.lerp(secondary, v);
            
            g.renderRect(0,0,button.width(),button.height(),Color.fromVector(color));
            float x = button.width()/2;
            float y = button.height()/2;
            if (button.icon()!=null) {
                g.renderImage((TImage) button.icon(), x, y);
            }
            if (halign==HAlign.LEFT) {
                x = border;
            } else if (halign==HAlign.RIGHT) {
                x = button.width() - border;
            }
            g.renderText(button.text(),xoffset+x,y,halign,valign,style().font(),textColor);
        });
        
        this.setSizeHint((button,i)->{
            TSizeHint hint = new TSizeHint();
            if (button.icon()!=null) {
                hint.width = Math.max(hint.width, button.icon().width()) + border;
                hint.height = Math.max(hint.height, button.icon().height()) + border;
            }
            hint.width = Math.max(hint.width, (int) style().font().getWidth(button.text()) + 2*border);
            hint.height = Math.max(hint.height, (int) style().font().getHeight() + border);
            return hint;
        });
        
    }
    
    public Color textColor() { return textColor; }
    public Color buttonColor() { return buttonColor; }
    public Color hoverColor() { return hoverColor; }
    public Color toggleColor() { return toggleColor; }
    
    public void textColor(Color textColor) { this.textColor = textColor; }
    public void buttonColor(Color buttonColor) { this.buttonColor = buttonColor; }
    public void hoverColor(Color hoverColor) { this.hoverColor = hoverColor; }
    public void toggleColor(Color toggleColor) { this.toggleColor = toggleColor; }
    

}
