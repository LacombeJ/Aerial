package jonl.aui.tea.graphics;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TSizePolicy;
import jonl.vmath.Vector4;

public class TButtonStyle extends TWidgetStyle<TButton> {
    
    private int border = 4;
    private float maxValue = 30;
    
    public TButtonStyle(TStyle style) {
        super(style);
        
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
            Vector4 primary = style().primary().toVector();
            Vector4 secondary = style().secondary().toVector();
            Vector4 color = primary.lerp(secondary, v);
            
            g.renderRect(0,0,button.width(),button.height(),TColor.fromVector(color));
            float x = button.width()/2;
            float y = button.height()/2;
            g.renderText(button.text(),x,y,HAlign.CENTER,VAlign.MIDDLE,style().calibri,style().textColor());
        });
        
        this.setSizePolicy((button,i)->{
            TSizePolicy sp = new TSizePolicy();
            sp.minWidth = (int) style().calibri.getWidth(button.text()) + border;
            sp.prefHeight = (int) style().calibri.getHeight() + border;
            return sp;
        });
        
    }
    
    
    

}
