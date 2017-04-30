package jonl.vmath;

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
    
    public static Vector2 toThetaPhi(Vector3 u) {
        float theta = -Mathf.toDegrees(Mathf.atan(u.y/Mathf.sqrt(u.x*u.x+u.z*u.z)));
        float phi = Mathf.deg(-u.x,-u.z)-90;
        return new Vector2(theta,phi);
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
    
}
