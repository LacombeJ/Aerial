package ax.math.vector;

/**
 * Barycentric Triangle
 * 
 * @author Jonathan Lacombe
 *
 */
public class BTriangle {
    
    Vector2 base;
    Vector2 v0;
    Vector2 v1;
    float d00;
    float d01;
    float d11;
    float invDenom;

    public BTriangle(Vector2 a, Vector2 b, Vector2 c) {
        
        base = a.get();
        
        v0 = Mathf.sub(b,base);
        v1 = Mathf.sub(c,base);
        
        d00 = Mathf.dot(v0, v0);
        d01 = Mathf.dot(v0, v1);
        d11 = Mathf.dot(v1, v1);
        
        invDenom = 1 / (d00 * d11 - d01 * d01);
        
    }
    
    public Vector3 toBarycentric(Vector2 p) {
        Vector2 v2 = Mathf.sub(p,base);
        float d20 = Mathf.dot(v2, v0);
        float d21 = Mathf.dot(v2, v1);
        float v = (d11 * d20 - d01 * d21) * invDenom;
        float w = (d00 * d21 - d01 * d20) * invDenom;
        float u = 1.0f - v - w;
        return new Vector3(u,v,w);
    }
    
    public Vector3 getBarycentric(Vector2 p) {
        Vector3 coord = toBarycentric(p);
        if (isWithin(coord)) return coord;
        return null;
    }
    
    public static float interpolate(Vector3 coord, float x, float y, float z) {
        return coord.x*x + coord.y*y + coord.z*z;
    }
    
    public static boolean isWithin(Vector3 coord) {
        if (coord.x<0 || coord.x>1) return false;
        if (coord.y<0 || coord.y>1) return false;
        if (coord.z<0 || coord.z>1) return false;
        return true;
    }
    
}
