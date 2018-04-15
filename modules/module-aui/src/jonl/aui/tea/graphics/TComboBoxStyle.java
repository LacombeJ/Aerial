package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TComboBox;
import jonl.aui.tea.TSizeHint;
import jonl.vmath.Color;
import jonl.vmath.Vector4;

public class TComboBoxStyle extends TWidgetStyle<TComboBox> {
    
    private Color textColor;
    private Color buttonColor;
    private Color hoverColor;
    private Color toggleColor;
    private int border = 4;
    private float maxValue = 30;
    
    public TComboBoxStyle(TStyle style) {
        super(style);
        
        textColor = style.textColor();
        buttonColor = style.primary();
        hoverColor = style.secondary();
        toggleColor = style.tertiary();
        
        this.setPainter((comboBox,info,g)->{
            
            float fIntensityValue = info.get("fIntensityValue", 0f);
            
            float fMaxValue = info.get("fMaxValue", maxValue);
            Color cButton = info.get("cButton", buttonColor);
            Color cHover = info.get("cHover", hoverColor);
            HAlign halign = info.get("halign", HAlign.LEFT);
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
            Vector4 primary = cButton.toVector();
            Vector4 secondary = cHover.toVector();
            Vector4 color = primary.lerp(secondary, v);
            
            //Color darken = Color.fromVector(color.scale(0.5f));
            //g.renderGradient(0,0,comboBox.width(),comboBox.height(),Color.fromVector(color),Color.WHITE);
            Color lighten = Color.fromVector(new Vector4(color.get().scale(1.2f).clamp(0,1).xyz(),color.w));
            Color darken = Color.fromVector(new Vector4(color.get().scale(0.7f).xyz(),color.w));
            g.renderGradient(0,0,comboBox.width(),comboBox.height(), darken, lighten);
            float x = comboBox.width()/2;
            float y = comboBox.height()/2;
            if (halign==HAlign.LEFT) {
                x = border;
            } else if (halign==HAlign.RIGHT) {
                x = comboBox.width() - border;
            }
            g.renderText(comboBox.text(),xoffset+x,y,halign,valign,style().font(),textColor);
            g.renderCaret(comboBox.width()-y,y,textColor);
        });
        
        this.setSizeHint((button,i)->{
            TSizeHint hint = new TSizeHint();
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
