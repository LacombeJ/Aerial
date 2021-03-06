package ax.math.vector;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Vector3 extends Vector<Vector3> {
    
    public float x;
    public float y;
    public float z;
    
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(Vector3 v) {
        this(v.x,v.y,v.z);
    }
    
    public Vector3(Vector2 v, float z) {
        this(v.x,v.y,z);
    }
    
    public Vector3(float f) {
        this(f,f,f);
    }
    
    public Vector3() { }

    @Override
    public int size() {
        return 3;
    }
    
    @Override
    public float get(int i) {
        switch(i) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        default:
            throw new IndexOutOfBoundsException(getExceptionString(size(),i));
        }
    }
    
    @Override
    public void set(int i, float v) {
        switch(i) {
        case 0: x = v; break;
        case 1: y = v; break;
        case 2: z = v; break;
        default:
            throw new IndexOutOfBoundsException(getExceptionString(size(),i));
        }
    }

    @Override
    public Vector3 getEmptyVector() {
        return new Vector3();
    }
    
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector2 xy() {
        return new Vector2(x,y);
    }
    
    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    /** @return this vector after it has been transformed by the given matrix */
    public Vector3 transform(Matrix4 mat) {
        Vector3 mul = mat.multiply(new Vector4(this,1)).xyz();
        set(mul);
        return this;
    }
    
    /**
     * Returns the angle from the ground, a value from [-pi/2 to pi/2]<br>
     * Examples:
     * <pre>
     * Vector3(1,0,0).theta() = 0
     * Vector3(1,1,0).theta() = pi/4
     * Vector3(1,-1,0).theta() = -pi/4
     * Vector3(0,1,0).theta() = pi/2
     * Vector3(0,-1,0).theta() = -pi/2
     * </pre>
     * @return the angle from the ground, a value from [-pi/2 to pi/2]
     */
    public float theta() {
    	return Mathf.atan( y / Mathf.sqrt(x*x+z*z) );
    }
    
    /**
     * Returns the angle of the x,z component of the vector, a value of [0,2pi)<br>
     * Examples:
     * <pre>
     * Vector3(1,0,0).phi() = 0
     * Vector3(0,0,1).phi() = pi/2
     * Vector3(-1,0,0).phi() = pi
     * Vector3(0,0,-1).phi() = 3pi/2
     * Vector3(5,10,5).phi() = pi/4
     * </pre>
     * @return the angle of the x,z component of the vector, a value of [0,2pi)
     */
    public float phi() {
    	return Mathf.rad(x,z);
    }
    
    public static float thetaBetween(Vector3 u, Vector3 v) {
    	return v.get().sub(u).theta();
    }
    
    public static float phiBetween(Vector3 u, Vector3 v) {
    	return v.get().sub(u).phi();
    }
    
    /**
     * Returns the cross product
     * <p>
     * Does nothing to this vector
     * <p>
     * mag(u.cross(v)) = mag(u)*mag(v)*cos(a)
     * where a is the angle between u and v.
     * <p>
     * u and v are parallel if u.cross(v)==vec3(0,0,0)
     * @param v Vector to cross with
     * @return a vector which is the cross product
     * of this and the given vector parameter
     */
    public static Vector3 cross(Vector3 u, Vector3 v) {
        float cx = u.y*v.z - u.z*v.y;
        float cy = u.x*v.z - u.z*v.x;
        float cz = u.x*v.y - u.y*v.x;
        return new Vector3(cx,-cy,cz);
    }
    
    /**
     * Returns a perpendicular vector
     * <p>
     * Does nothing to this vector
     * @param v
     * @return the perp of this vector
     */
    public static Vector3 perp(Vector3 u, Vector3 v){
        Vector3 proj = u.get().proj(v);
        return proj.negate().add(u);
    }
    
    /**
     * <p>
     * http://math.stackexchange.com/questions/511370/how-to-rotate-one-vector-about-another
     * @param v
     * @param rad
     * @return
     */
    public static Vector3 rotationAround(Vector3 u, Vector3 v, float rad) {
        Vector3 proj = u.get().proj(v);
        Vector3 perp = perp(u,v);
        Vector3 w = cross(v,perp);
        float pM = perp.magnitude();
        float x0 = Mathf.cos(rad)/pM;
        float x1 = Mathf.sin(rad)/w.magnitude();
        Vector3 perpRad = perp.scale(x0).add(w.scale(x1)).scale(pM);
        return perpRad.add(proj);
    }
    
    public static Vector3 min(Vector3 u, Vector3 v) {
        return u.get().min(v);
    }
    
    public static Vector3 max(Vector3 u, Vector3 v) {
        return u.get().max(v);
    }
    
    public static Vector3 random() {
        return new Vector3(Mathf.random(),Mathf.random(),Mathf.random());
    }
    
    public static Vector3 random(float min, float max) {
        return new Vector3(Mathf.random(min,max),Mathf.random(min,max),Mathf.random(min,max));
    }
    
    //TODO Make sure these align with Matrix4 and Quaternion definition of forward, up, right
    public static final Vector3 forward()   { return new Vector3(0,0,-1); }
    public static final Vector3 up()        { return new Vector3(0,1,0); }
    public static final Vector3 right()     { return new Vector3(1,0,0); }
    
    public static Vector3[] lerp(float alpha, Vector3[] a, Vector3[] b) {
        Vector3[] lerp = new Vector3[a.length];
        Util.lerp(alpha,a,b,lerp);
        return lerp;
    }
    
    public static Vector3[] slerp(float alpha, Vector3[] a, Vector3[] b) {
        Vector3[] slerp = new Vector3[a.length];
        Util.slerp(alpha,a,b,slerp);
        return slerp;
    }
    
    public static float[] pack(List<Vector3> vectors) {
        return Util.pack(vectors);
    }
    
    public static float[] pack(Vector3... vectors) {
        return Util.pack(vectors);
    }
    
    public static ArrayList<Vector3> unpack(float[] values) {
        return Util.unpack(values,new Vector3());
    }
    
    public static Vector3[] unpackArray(float[] values) {
        return Util.unpackArray(values,new Vector3(),new Vector3[values.length/3]);
    }
    
}
