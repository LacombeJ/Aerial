package jonl.vmath;



public class Color {

    public final static Color RED           = Color.fromFloat(1, 0, 0);
    public final static Color GREEN         = Color.fromFloat(0, 1, 0);
    public final static Color BLUE          = Color.fromFloat(0, 0, 1);
    
    public final static Color YELLOW        = Color.fromFloat(1.00f, 1.00f, 0.00f);
    public final static Color ORANGE        = Color.fromFloat(1.00f, 0.50f, 0.00f);
    public final static Color PURPLE        = Color.fromFloat(0.50f, 0.00f, 1.00f);
    public final static Color PINK          = Color.fromFloat(1.00f, 0.00f, 1.00f);
    
    public final static Color CYAN          = Color.fromFloat(0, 1, 1);
    
    public final static Color WHITE         = Color.fromFloat(1, 1, 1);
    public final static Color BLACK         = Color.fromFloat(0, 0, 0);
    
    public final static Color LIGHT_GRAY    = Color.fromFloat(0.25f, 0.25f, 0.25f);
    public final static Color GRAY          = Color.fromFloat(0.5f, 0.5f, 0.5f);
    public final static Color DARK_GRAY     = Color.fromFloat(0.75f, 0.75f, 0.75f);
    
    public final static Color DARK_RED      = Color.fromFloat(0.5f, 0, 0);
    public final static Color DARK_GREEN    = Color.fromFloat(0, 0.5f, 0);
    public final static Color DARK_BLUE     = Color.fromFloat(0, 0, 0.5f);
    
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public Color(float r, float g, float b) {
        this(r,g,b,1);
    }
    
    public Vector4 toVector() {
        return new Vector4(r,g,b,a);
    }
    
    public static Color fromInt(int r, int g, int b, int a) {
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float A = a / 255f;
        return new Color(R,G,B,A);
    }
    
    public static Color fromInt(int r, int g, int b) {
        return fromInt(r,g,b,255);
    }
    
    public static Color fromFloat(float r, float g, float b, float a) {
        return new Color(r,g,b,a);
    }
    
    public static Color fromFloat(float r, float g, float b) {
        return new Color(r,g,b);
    }
    
    public static Color fromVector(Vector4 v) {
        return new Color(v.x,v.y,v.z,v.w);
    }
    
    public static Color fromColor(Color c) {
        return new Color(c.r,c.g,c.b,c.a);
    }
    
    public static Color fromColor(Color c, float alpha) {
        return new Color(c.r,c.g,c.b,alpha);
    }
    
    // https://github.com/mrdoob/three.js/blob/master/src/math/Color.js
    public static Color hsl2rgb(float h, float s, float l) {
        
        float r = 0;
        float g = 0;
        float b = 0;
        
        // h,s,l ranges are in 0.0 - 1.0
        h = Mathf.modulo( h, 1 );
        s = Mathf.clamp( s, 0, 1 );
        l = Mathf.clamp( l, 0, 1 );

        if ( s == 0 ) {

            r = g = b = l;

        } else {

            float p = l <= 0.5f ? l * ( 1f + s ) : l + s - ( l * s );
            float q = ( 2f * l ) - p;

            r = hue2rgb( q, p, h + 1f / 3f );
            g = hue2rgb( q, p, h );
            b = hue2rgb( q, p, h - 1f / 3f );

        }

        return Color.fromFloat(r,g,b);
    }
    
    // https://github.com/mrdoob/three.js/blob/master/src/math/Color.js
    public static float hue2rgb(float p, float q, float t) {
        if ( t < 0f ) t += 1f;
        if ( t > 1f ) t -= 1f;
        if ( t < 1f / 6f ) return p + ( q - p ) * 6f * t;
        if ( t < 1f / 2f ) return q;
        if ( t < 2f / 3f ) return p + ( q - p ) * 6f * ( 2f / 3f - t );
        return p;
    }
    
}
