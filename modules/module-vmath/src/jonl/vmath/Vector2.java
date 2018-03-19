package jonl.vmath;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Vector2 extends Vector<Vector2> {

    public float x;
    public float y;
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2(Vector2 v) {
        this(v.x,v.y);
    }
    
    public Vector2(float f) {
        this(f,f);
    }
    
    public Vector2() { }
    
    @Override
    public int size() {
        return 2;
    }
    
    @Override
    public float get(int i) {
        switch(i) {
        case 0: return x;
        case 1: return y;
        default:
            throw new IndexOutOfBoundsException(getExceptionString(size(),i));
        }
    }
    
    @Override
    public void set(int i, float v) {
        switch(i) {
        case 0: x = v; break;
        case 1: y = v; break;
        default:
            throw new IndexOutOfBoundsException(getExceptionString(size(),i));
        }
    }

    @Override
    public Vector2 getEmptyVector() {
        return new Vector2();
    }
    
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }
    
    @Override
    public Vector2 add(Vector2 v) {
        x+=v.x;
        y+=v.y;
        return this;
    }
    
    @Override
    public Vector2 sub(Vector2 v) {
        x-=v.x;
        y-=v.y;
        return this;
    }
    
    @Override
    public float magnitude() {
        return Mathf.sqrt(x*x + y*y);
    }
    
    public float slope() {
        return y/x;
    }
    
    /**
     * Returns the z value of the cross product between these two vectors
     * <p>
     * For any two vectors on the z-plane the x and y values of the
     * cross product are 0 so we only need to find the z value
     * <p>
     * Returns a positive value if vector v is on the right side of vector u
     * (right-hand rule)
     * @param u 1st vector
     * @param v 2nd vector
     * @return the z value of the cross between these two vectors
     */
    public static float cross(Vector2 u, Vector2 v) {
        return u.x*v.y - u.y*v.x;
    }
    
    public Vector2 rotate(float rad) {
        float cos = Mathf.cos(rad);
        float sin = Mathf.sin(rad);
        x = cos*x - sin*y;
        y = sin*x + cos*y;
        return this;
    }
    
    /**
     * @return the normal rotation of this vector of 90 degrees
     */
    public static Vector2 normal(Vector2 u) {
        float mag = Mathf.sqrt(u.x*u.x + u.y*u.y);
        return new Vector2(-u.y/mag,u.x/mag);
    }
    
    public boolean slopeEquals(Vector2 v, float epsilon) {
        float costheta = dot(v)/(magnitude()*v.magnitude());
        if (Mathf.eq(costheta,1,epsilon)) return true;
        if (Mathf.eq(costheta,-1,epsilon)) return true;
        return false;
    }
    
    public static float magnitude(Vector2 v) {
        return Mathf.sqrt(v.x*v.x + v.y*v.y);
    }
    
    public static Vector2 add(Vector2 u, Vector2 v) {
        return new Vector2(u.x+v.x,u.y+v.y);
    }
    
    public static Vector2 sub(Vector2 u, Vector2 v) {
        return new Vector2(u.x-v.x,u.y-v.y);
    }
    
    public static Vector2 norm(Vector2 v) {
        float mag = Mathf.sqrt(v.x*v.x + v.y*v.y);
        return new Vector2(v.x/mag,v.y/mag);
    }
    
    public static Vector2 multiply(Vector2 v, float scalar) {
        return new Vector2(v.x*scalar,v.y*scalar);
    }
    
    public static Vector2 negate(Vector2 v) {
        return new Vector2(-v.x,-v.y);
    }
    
    public static float rad(Vector2 u, Vector2 v) {
        float cross = Vector2.cross(u, v);
        int sign = cross>0 ? 1 : -1;
        return sign * Mathf.acos(u.dot(v) / (u.mag() * v.mag()));
    }
    
    public static Vector2 random() {
        return new Vector2(Mathf.random(),Mathf.random());
    }
    
    public static Vector2 random(float min, float max) {
        return new Vector2(Mathf.random(min,max),Mathf.random(min,max));
    }
    
    public static List<Vector2> pack(float[] values) {
        List<Vector2> vectors = new ArrayList<>();
        for (int i=0; i<values.length/2; i++) {
            Vector2 v = new Vector2(
                values[i],
                values[i*2+1]
            );
            vectors.add(v);
        }
        return vectors;
    }
    
    public static float[] unpack(List<Vector2> vectors) {
        float[] values = new float[vectors.size()*2];
        for (int i=0; i<vectors.size(); i++) {
            values[i*2] = vectors.get(i).x;
            values[i*2+1] = vectors.get(i).y;
        }
        return values;
    }
    
}
