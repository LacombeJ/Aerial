package jonl.aui.tea.graphics;

import jonl.aui.tea.TDial;
import jonl.aui.tea.TSizePolicy;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;

public class TDialStyle extends TWidgetStyle<TDial> {
    
    private TColor handColor;
    private TColor dialColor;
    private int minDim = 64;
    
    public TDialStyle(TStyle style) {
        super(style);
        
        handColor = style.primary();
        dialColor = style.secondary();
        
        this.setPainter((dial,i,g)->{
            float min = Mathf.min(dial.width(),dial.height());
            float ratio = 0.7f;
            float dim = min * ratio;
            float x = dial.width()/2f - dim/2;
            float y = dial.height()/2f - dim/2;
            g.renderCircle(x,y,dim,dim,dialColor.toVector());
            Matrix4 mat = Matrix4.identity();
            
            mat.translate(dial.width()/2f,dial.height()/2f,0);
            float alpha = Mathf.alpha(dial.value(),dial.min(),dial.max());
            
            mat.rotateZ(alpha*Mathf.TWO_PI);
            
            mat.translate(dim/2,0,0);
            mat.scale(dim/10,dim/10,1);
            g.renderRect(mat,handColor.toVector());
        });
        
        this.setSizePolicy((button,i)->{
            TSizePolicy sp = new TSizePolicy();
            sp.minWidth = minDim;
            sp.minHeight = minDim;
            return sp;
        });
        
    }
    
    public TColor buttonColor() { return handColor; }
    public TColor hoverColor() { return dialColor; }
    
    public void buttonColor(TColor buttonColor) { this.handColor = buttonColor; }
    public void hoverColor(TColor hoverColor) { this.dialColor = hoverColor; }
    

}
