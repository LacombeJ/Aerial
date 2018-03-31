package jonl.vmath;

// https://github.com/mrdoob/three.js/blob/master/src/math/Color.js

public class Color {

    public static Vector3 hsl2rgb(float h, float s, float l) {
        
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

        return new Vector3(r,g,b);
    }
    
    public static float hue2rgb(float p, float q, float t) {
        if ( t < 0f ) t += 1f;
        if ( t > 1f ) t -= 1f;
        if ( t < 1f / 6f ) return p + ( q - p ) * 6f * t;
        if ( t < 1f / 2f ) return q;
        if ( t < 2f / 3f ) return p + ( q - p ) * 6f * ( 2f / 3f - t );
        return p;
    }
    
}
