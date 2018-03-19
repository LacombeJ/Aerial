package jonl.vmath;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class Vector4 extends Vector<Vector4> {

    public float x;
    public float y;
    public float z;
    public float w;
    
    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4(Vector4 v) {
        this(v.x,v.y,v.z,v.w);
    }
    
    public Vector4(Vector3 v, float w) {
        this(v.x,v.y,v.z,w);
    }
    
    public Vector4(Vector2 v, float z, float w) {
        this(v.x,v.y,z,w);
    }
    
    public Vector4(float f) {
        this(f,f,f,f);
    }
    
    public Vector4() { }
    
    @Override
    public int size() {
        return 4;
    }
    
    @Override
    public float get(int i) {
        switch(i) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        case 3: return w;
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
        case 3: w = v; break;
        default:
            throw new IndexOutOfBoundsException(getExceptionString(size(),i));
        }
    }

    @Override
    public Vector4 getEmptyVector() {
        return new Vector4();
    }
    
    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector2 xy() {
        return new Vector2(x,y);
    }
    
    public Vector3 xyz() {
        return new Vector3(x,y,z);
    }
    
    public void add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
    }
    
    public static Vector4 random() {
        return new Vector4(Mathf.random(),Mathf.random(),Mathf.random(),Mathf.random());
    }
    
    public static Vector4 random(float min, float max) {
        return new Vector4(Mathf.random(min,max),Mathf.random(min,max),Mathf.random(min,max),Mathf.random(min,max));
    }
    
    public static final Vector4 forward()   { return new Vector4(Vector3.forward(), 1); }
    public static final Vector4 up()        { return new Vector4(Vector3.up(),      1); }
    public static final Vector4 right()     { return new Vector4(Vector3.right(),   1); }
    
    public static List<Vector4> pack(float[] values) {
        List<Vector4> vectors = new ArrayList<>();
        for (int i=0; i<values.length/4; i++) {
            Vector4 v = new Vector4(
                values[i],
                values[i*4+1],
                values[i*4+2],
                values[i*4+3]
            );
            vectors.add(v);
        }
        return vectors;
    }
    
    public static float[] unpack(List<Vector4> vectors) {
        float[] values = new float[vectors.size()*4];
        for (int i=0; i<vectors.size(); i++) {
            values[i*4] = vectors.get(i).x;
            values[i*4+1] = vectors.get(i).y;
            values[i*4+2] = vectors.get(i).z;
            values[i*4+3] = vectors.get(i).w;
        }
        return values;
    }
    
}
