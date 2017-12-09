package jonl.vmath;

import java.util.Arrays;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class VectorN extends Vector<VectorN> {

    public final float[] vector;
    
    public VectorN(float[] v) {
        vector = Arrays.copyOf(v,v.length);
    }
    
    public VectorN(VectorN v) {
        this(v.vector);
    }
    
    public VectorN() {
        vector = new float[0];
    }
    
    @Override
    public int size() {
        return vector.length;
    }
    @Override
    public float get(int i) {
        return vector[i];
    }
    @Override
    public void set(int i, float v) {
        vector[i] = v;
    }

    @Override
    public VectorN getEmptyVector() {
        return new VectorN(new float[vector.length]);
    }
    
}
