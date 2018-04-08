package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TCheckBox;
import jonl.aui.tea.TSizeHint;
import jonl.vmath.Color;
import jonl.vmath.Vector4;

public class TCheckBoxStyle extends TWidgetStyle<TCheckBox> {
    
    private Color textColor;
    private Color buttonColor;
    private Color hoverColor;
    private Color toggleColor;
    private int border = 4;
    private float maxValue = 30;
    int boxDim = 40;
    int boxD = 4;
    
    public TCheckBoxStyle(TStyle style) {
        super(style);
        
        textColor = style.textColor();
        buttonColor = style.primary();
        hoverColor = style.secondary();
        toggleColor = style.tertiary();
        
        this.setPainter((button,info,g)->{
            
            float fIntensityValue = info.get("fIntensityValue", 0f);
            
            float fMaxValue = info.get("fMaxValue", maxValue);
            Color cButton = info.get("cButton", buttonColor);
            Color cHover = info.get("cHover", Color.fromInt(0, 0, 255));
            
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
            Vector4 primary = cButton.toVector();
            Vector4 secondary = cHover.toVector();
            Vector4 color = primary.lerp(secondary, v);
            
            float y = button.height()/2;
            
            g.renderRect(boxD, y - boxDim/2, boxDim, boxDim, Color.WHITE);
            g.renderRectOutline(boxD, y - boxDim/2, boxDim, boxDim, color, boxD/2);
            g.renderText(button.text(),boxDim+boxD+border,y,HAlign.LEFT,VAlign.MIDDLE,style().font(),textColor);
            
            if (button.checked()) {
                g.renderCheck(boxD+6, y - boxDim/2+6);
            }
            
        });
        
        this.setSizeHint((button,i)->{
            TSizeHint hint = new TSizeHint();
            if (button.icon()!=null) {
                hint.width = Math.max(hint.width, button.icon().width()) + border;
                hint.height = Math.max(hint.height, button.icon().height()) + border;
            }
            hint.width = Math.max(hint.width, (int) style().font().getWidth(button.text()) + 2*border + boxDim + boxD);
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
