package ax.math.vector;



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
    
    public final static Color LIGHT_GRAY    = Color.fromFloat(0.75f, 0.75f, 0.75f);
    public final static Color GRAY          = Color.fromFloat(0.5f, 0.5f, 0.5f);
    public final static Color DARK_GRAY     = Color.fromFloat(0.25f, 0.25f, 0.25f);
    
    public final static Color DARK_RED      = Color.fromFloat(0.5f, 0, 0);
    public final static Color DARK_GREEN    = Color.fromFloat(0, 0.5f, 0);
    public final static Color DARK_BLUE     = Color.fromFloat(0, 0, 0.5f);
    
    public final static Color TRANSPARENT   = Color.fromFloat(0,0,0,0);
    
    // --------------------------------------------------------------------------------- //
    // ------------------------------------ Crayola ------------------------------------ //
    public final static Color BROWN         = Color.fromHex("#AF593E");
    public final static Color RED_ORANGE    = Color.fromHex("#ED0A3F");
    public final static Color SKY_BLUE      = Color.fromHex("#76D7EA");
    public final static Color VIOLET        = Color.fromHex("#8359A3");
    public final static Color YELLOW_GREEN  = Color.fromHex("#C5E17A");
    public final static Color AQUA_GREEN    = Color.fromHex("#03BB85");
    public final static Color GOLDEN_YELLOW = Color.fromHex("#FFDF00");
    public final static Color JADE_GREEN    = Color.fromHex("#0A6B0D");
    public final static Color LIGHT_BLUE    = Color.fromHex("8FD8D8");
    public final static Color LIGHT_BROWN   = Color.fromHex("#A36F40");
    public final static Color MAGENTA       = Color.fromHex("#F653A6");
    public final static Color MAHOGANY      = Color.fromHex("#CA3435");
    public final static Color PEACH         = Color.fromHex("#FFCBA4");
    public final static Color TAN           = Color.fromHex("#FA9D5A");
    public final static Color YELLOW_ORANGE = Color.fromHex("#FFAE42");
    public final static Color BRONZE_YELLOW = Color.fromHex("#A78B00");
    public final static Color COOL_GRAY     = Color.fromHex("#788193");
    public final static Color DARK_BROWN    = Color.fromHex("#514E49");
    public final static Color GREEN_BLUE    = Color.fromHex("#1164B4");
    public final static Color LEMON_YELLOW  = Color.fromHex("#F4FA9F");
    public final static Color LIGHT_ORANGE  = Color.fromHex("#FED8B1");
    public final static Color MAROON        = Color.fromHex("#C32148");
    public final static Color PINE_GREEN    = Color.fromHex("#01796F");
    public final static Color RASBERRY      = Color.fromHex("#E90067");
    public final static Color SALMON        = Color.fromHex("#FF91A4");
    public final static Color SLATE         = Color.fromHex("#404E5A");
    public final static Color TURQUOISE     = Color.fromHex("#6CDAE7");
    public final static Color BUBBLE_GUM    = Color.fromHex("#FFC1CC");
    public final static Color CERULEAN      = Color.fromHex("#006A93");
    public final static Color HARVEST_GOLD  = Color.fromHex("#E2B631");
    public final static Color LIME_GREEN    = Color.fromHex("#6EEB6E");
    public final static Color MANGO         = Color.fromHex("#FFC800");
    public final static Color MAUVE         = Color.fromHex("#CC99BA");
    public final static Color NAVY_BLUE     = Color.fromHex("#00003B");
    public final static Color ORCHID        = Color.fromHex("#BC6CAC");
    public final static Color PALE_ROSE     = Color.fromHex("#DCCCD7");
    public final static Color SAND          = Color.fromHex("#EBE1C2");
    public final static Color SILVER        = Color.fromHex("#A6AAAE");
    public final static Color TAUPE         = Color.fromHex("#B99685");
    public final static Color TEAL          = Color.fromHex("#0086A7");
    public final static Color COCOA         = Color.fromHex("#5E4330");
    // --------------------------------------------------------------------------------- //
    
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

    public ColorInt toInt() { return Color.toInt(this); }

    public ColorHSL toHSL() { return Color.toHSL(this); }

    @Override
    public String toString() {
        return "Color("+r+","+g+","+b+","+a+")";
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
    
    public static Color fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int r = Integer.parseInt(hex.substring(0,2), 16);
        int g = Integer.parseInt(hex.substring(2,4), 16);
        int b = Integer.parseInt(hex.substring(4,6), 16);
        int a = 255;
        if (hex.length()>=8) {
            a = Integer.parseInt(hex.substring(6,8), 16);
        }
        return fromInt(r,g,b,a);
    }
    


    public static Color fromHSL(ColorHSL hsl) {
        return fromHSL(hsl.h, hsl.s, hsl.l);
    }

    // https://github.com/mrdoob/three.js/blob/master/src/math/Color.js
    public static Color fromHSL(float h, float s, float l) {
        
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

    public static ColorInt toInt(Color c) {
        int R = (int) (c.r * 255);
        int G = (int) (c.g * 255);
        int B = (int) (c.b * 255);
        int A = (int) (c.a * 255);
        return new ColorInt(R,G,B,A);
    }

    // https://github.com/mrdoob/three.js/blob/master/src/math/Color.js
    public static ColorHSL toHSL(Color c) {

        float r = c.r, g = c.g, b = c.b;

        int maxi = Mathf.maxIndex( r, g, b );
        float max = Mathf.max( r, g, b );
        float min = Mathf.min( r, g, b );

        float hue=0;
        float saturation;
        float lightness = ( min + max ) / 2.0f;

        if ( min == max ) {

            hue = 0;
            saturation = 0;

        } else {

            float delta = max - min;

            saturation = lightness <= 0.5f ? delta / ( max + min ) : delta / ( 2 - max - min );

            switch ( maxi ) {

                case 0: hue = ( g - b ) / delta + ( g < b ? 6 : 0 ); break;
                case 1: hue = ( b - r ) / delta + 2; break;
                case 2: hue = ( r - g ) / delta + 4; break;

            }

            hue /= 6f;

        }
        return new ColorHSL(hue, saturation, lightness);
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

    public static class ColorInt {
        public final int r;
        public final int g;
        public final int b;
        public final int a;
        public ColorInt(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }
    }

    public static class ColorHSL {
        public final float h;
        public final float s;
        public final float l;
        public ColorHSL(float h, float s, float l) {
            this.h = h;
            this.s = s;
            this.l = l;
        }
    }
    
}
