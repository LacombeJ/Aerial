package jonl.aui.tea.graphics;

import jonl.vmath.Vector4;

public class TColor {

    public final static TColor RED      = TColor.fromFloat(1, 0, 0);
    public final static TColor GREEN    = TColor.fromFloat(0, 1, 0);
    public final static TColor BLUE     = TColor.fromFloat(0, 0, 1);
    
    public final static TColor WHITE    = TColor.fromFloat(1, 1, 1);
    public final static TColor BLACK    = TColor.fromFloat(0, 0, 0);
    
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    
    public TColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public TColor(float r, float g, float b) {
        this(r,g,b,1);
    }
    
    public Vector4 toVector() {
        return new Vector4(r,g,b,a);
    }
    
    public static TColor fromInt(int r, int g, int b, int a) {
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float A = a / 255f;
        return new TColor(R,G,B,A);
    }
    
    public static TColor fromInt(int r, int g, int b) {
        return fromInt(r,g,b,255);
    }
    
    public static TColor fromFloat(float r, float g, float b, float a) {
        return new TColor(r,g,b,a);
    }
    
    public static TColor fromFloat(float r, float g, float b) {
        return new TColor(r,g,b);
    }
    
    public static TColor fromVector(Vector4 v) {
        return new TColor(v.x,v.y,v.z,v.w);
    }
    
    public static TColor fromColor(TColor c) {
        return new TColor(c.r,c.g,c.b,c.a);
    }
    
    public static TColor fromColor(TColor c, float alpha) {
        return new TColor(c.r,c.g,c.b,alpha);
    }
    
}
