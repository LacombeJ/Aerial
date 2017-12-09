package jonl.aui.sui;

import jonl.aui.Graphics;
import jonl.aui.logic.ADial;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;

public class SDial extends ADial {

    SDial() {
        
    }
    
    @Override
    protected void paint(Graphics g) {
        super.paint(g);
        
        float min = Mathf.min(getWidth(),getHeight());
        float ratio = 0.7f;
        float dim = min * ratio;
        float x = getWidth()/2f - dim/2;
        float y = getHeight()/2f - dim/2;
        g.renderCircle(x,y,dim,dim,Style.get(this).buttonColorHover);
        Matrix4 mat = Matrix4.identity();
        
        mat.translate(getWidth()/2f,getHeight()/2f,0);
        float alpha = Mathf.alpha(getValue(),getMinValue(),getMaxValue());
        
        mat.rotateZ(alpha*Mathf.TWO_PI);
        
        mat.translate(dim/2,0,0);
        mat.scale(dim/10,dim/10,1);
        g.renderRect(mat,Style.get(this).buttonColor);
    }
    
}
