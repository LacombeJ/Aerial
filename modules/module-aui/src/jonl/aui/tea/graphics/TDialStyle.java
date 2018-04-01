package jonl.aui.tea.graphics;

import jonl.aui.tea.TDial;
import jonl.aui.tea.TSizeHint;
import jonl.vmath.Color;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

public class TDialStyle extends TWidgetStyle<TDial> {
    
    private Color handColor;
    private Color dialColor;
    private Color adjustColor;
    private int minDim = 64;
    private int border = 4;
    private int maxIntensityValue = 30;
    
    // If the following values add up to be 1, the edges of the dial hand and the dial should be connected
    private float dialHandDistancePercentage = 0.7f; // distance from dial hand to edge of dial over radius of dial
    private float dialHandSizePercentage = 0.2f; // percentage size of dial hand compared to dial
    
    public TDialStyle(TStyle style) {
        super(style);
        
        handColor = style.secondary();
        dialColor = style.primary();
        adjustColor = style.tertiary();
        
        this.setPainter((dial,info,g)->{
            
            boolean bInAdjustState = info.getBoolean("bInAdjustState");
            
            float fIntensityValue = info.get("fIntensityValue", 0f);
            float fMaxValue = info.get("fMaxIntensityValue", maxIntensityValue);
            
            float min = Mathf.min(dial.width(),dial.height());
            float ratio = 0.7f;
            float dim = min * ratio;
            float x = dial.width()/2f;
            float y = dial.height()/2f;
            
            if (bInAdjustState) {
                if (fIntensityValue < fMaxValue) {
                    fIntensityValue++;
                }
            } else {
                if (fIntensityValue > 0) {
                    fIntensityValue--;
                }
            }
            
            float value = fIntensityValue / fMaxValue;
            Vector4 borderColor = dialColor.toVector().lerp(adjustColor.toVector(), value);
            g.renderCircle(x,y,dim+border,dim+border,borderColor);
            
            info.put("fIntensityValue", fIntensityValue);
            
            g.renderCircle(x,y,dim,dim,dialColor.toVector());
            Matrix4 mat = Matrix4.identity();
            
            //Matrix operations are logically backwards
            //1) Scale to the correct size
            //2) Translate outwards by a distance
            //3) Rotate mesh by the correct angle
            //4) Translate mesh to be relative to the center of the dial
            
            mat.translate(dial.width()/2f,dial.height()/2f,0);
            float alpha = Mathf.alpha(dial.value(),dial.min(),dial.max());
            
            mat.rotateZ(alpha*Mathf.TWO_PI);
            
            mat.translate(dim*dialHandDistancePercentage*0.5f,0,0);
            mat.scale(dim*dialHandSizePercentage ,dim*dialHandSizePercentage, 1);
            
            // rotation is not noticeable with circle but I'll keep it here anyway
            g.renderCircle(mat, handColor.toVector());
        });
        
        this.setSizeHint((button,i)->{
            return new TSizeHint(minDim, minDim);
        });
        
    }
    
    public Color buttonColor() { return handColor; }
    public Color hoverColor() { return dialColor; }
    
    public void buttonColor(Color buttonColor) { this.handColor = buttonColor; }
    public void hoverColor(Color hoverColor) { this.dialColor = hoverColor; }
    

}
