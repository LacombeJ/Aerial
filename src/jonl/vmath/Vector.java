package jonl.vmath;

/**
 * Class representing an abstract vector
 * 
 * @author Jonathan Lacombe
 *
 */
public abstract class Vector<V extends Vector<V>> {

    /** @return the number of elements in this vector */
    public abstract int size();
    
    /** @return the value at the given index */
    public abstract float get(int i);
    
    /** Sets the value at the given index*/
    public abstract void set(int i, float v);
    
    /** @return a vector filled with 0s */
    public abstract V getEmptyVector();
    
    /** @return a copy of this vector */
    public V get() {
        V v = getEmptyVector();
        for (int i=0; i<size(); i++) {
            v.set(i,get(i));
        }
        return v;
    }
    
    /** Sets the values of this vector from the given vector */
    public void set(V v) {
        for (int i=0; i<size(); i++) {
            set(i,v.get(i));
        }
    }
    
    /** @return the dot product of this and the given vector */
    public float dot(V v) {
        float sum = 0;
        for (int i=0; i<size(); i++) {
            sum += get(i)*v.get(i);
        }
        return sum;
    }
    
    /** @return magnitude squared */
    public float magnitude2() {
        float sum = 0;
        for (int i=0; i<size(); i++) {
            sum += get(i) * get(i);
        }
        return sum;
    }
    
    /** @return the magnitude of this vector */
    public float magnitude() {
        return Mathf.sqrt(magnitude2());
    }
    
    public float mag2() { return magnitude2(); }
    public float mag() { return magnitude(); }
    
    /** @return the distance from the given vector */
    public float distance(V v) {
        float sum = 0;
        for (int i=0; i<size(); i++) {
            sum += Mathf.pow(v.get(i)-get(i),2);
        }
        return Mathf.abs(Mathf.sqrt(sum));
    }
    
    public float dist(V v) { return distance(v); }
    
    /** @return this vector after it has been scaled */
    @SuppressWarnings("unchecked")
    public V scale(float scalar) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)*scalar);
        }
        return (V) this;
    }
    
    public V multiply(float scalar) { return scale(scalar); }
    
    public V divide(float scalar) { return scale(1f/scalar); }
    
    /**
     * Multiplies the components of this vector by the corresponding
     * components of the given vector
     * @return this Vector after it has been multiplied
     */
    @SuppressWarnings("unchecked")
    public V multiply(V v) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)*v.get(i));
        }
        return (V) this;
    }
    
    /**
     * Divides the components of this vector by the corresponding
     * components of the given vector
     * @return this Vector after it has been divided
     */
    @SuppressWarnings("unchecked")
    public V divide(V v) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)/v.get(i));
        }
        return (V) this;
    }
    
    /** @return this vector after it has been normalized */
    public V normalize() {
        float mag = magnitude();
        if (mag==0) return scale(0);
        return scale(1/mag);
    }
    
    /** @see #normalize() */
    public V norm() { return normalize(); }
    
    /**
     * @return this vector after it is projected onto
     * the given vector
     */
    @SuppressWarnings("unchecked")
    public V proj(V v) {
        V vec = v.get();
        float ratio = dot(v)/v.dot(v);
        vec.scale(ratio);
        set(vec);
        return (V) this;
    }
    
    /** @return this vector as the sum of two vectors */
    @SuppressWarnings("unchecked")
    public V add(V vector) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)+vector.get(i));
        }
        return (V) this;
    }
    
    /**
     * Adds the scaled version of the given vector to
     * this vector
     * @return this vector as the sum of two vectors
     */
    @SuppressWarnings("unchecked")
    public V add(V vector, float scalar) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)+(vector.get(i)*scalar));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V add(float scalar) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)+scalar);
        }
        return (V) this;
    }
    
    /** @return this vector as the difference of two vectors */
    @SuppressWarnings("unchecked")
    public V sub(V vector) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)-vector.get(i));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V sub(float scalar) {
        for (int i=0; i<size(); i++) {
            set(i,get(i)-scalar);
        }
        return (V) this;
    }
    
    /** @return this vector negated */
    @SuppressWarnings("unchecked")
    public V negate() {
        for (int i=0; i<size(); i++) {
            set(i,-get(i));
        }
        return (V) this;
    }
    
    public V neg() { return negate(); }
    
    /** @return this vector with positive elements */
    @SuppressWarnings("unchecked")
    public V absolute() {
        for (int i=0; i<size(); i++) {
            set(i,Mathf.abs(get(i)));
        }
        return (V) this;
    }
    
    public V abs() { return absolute(); }
    
    @SuppressWarnings("unchecked")
    public V min(V v) {
        for (int i=0; i<size(); i++) {
            set(i,get(i) < v.get(i) ? get(i) : v.get(i));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V max(V v) {
        for (int i=0; i<size(); i++) {
            set(i,get(i) > v.get(i) ? get(i) : v.get(i));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V min(float a) {
        for (int i=0; i<size(); i++) {
            set(i,get(i) < a ? get(i) : a);
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V max(float a) {
        for (int i=0; i<size(); i++) {
            set(i,get(i) > a ? get(i) : a);
        }
        return (V) this;
    }
    
    public float min() {
        float min = get(0);
        for (int i=1; i<size(); i++) {
            if (get(i) < min) {
                min = get(i);
            }
        }
        return min;
    }
    
    public float max() {
        float min = get(0);
        for (int i=1; i<size(); i++) {
            if (get(i) < min) {
                min = get(i);
            }
        }
        return min;
    }
    
    @SuppressWarnings("unchecked")
    public V floor() {
        for (int i=1; i<size(); i++) {
            set(i, Mathf.floor(get(i)));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V ceil() {
        for (int i=1; i<size(); i++) {
            set(i, Mathf.ceil(get(i)));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V round() {
        for (int i=1; i<size(); i++) {
            set(i, Mathf.round(get(i)));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V fract() {
        for (int i=1; i<size(); i++) {
            set(i, Mathf.fract(get(i)));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V mod(float v) {
        for (int i=1; i<size(); i++) {
            set(i, Mathf.mod(get(i),v));
        }
        return (V) this;
    }
    
    @SuppressWarnings("unchecked")
    public V lerp(V v, float alpha) {
        for (int i=0; i<size(); i++) {
            float A = get(i);
            float B = v.get(i);
            float D = B - A;
            set(i,A + D*alpha);
        }
        return (V) this;
    }
    
    public V slerp(V v, float alpha) {
        return lerp(v,Mathf.sin(alpha*Mathf.PI_OVER_2));
    }
    
    /** @return a float array with the same elements */
    public float[] toArray() {
        float[] array = new float[size()];
        for (int i=0; i<array.length; i++) {
            array[i] = get(i);
        }
        return array;
    }
    
    /** @return exception string for index out of bounds */
    protected String getExceptionString(int size, int targ) {
        return "Index out of bounds.. Size: "
                +size+" Target: "+targ;
    }
    
    /** Prints this vector to the standard output stream */
    public void print() {
        System.out.println(this);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("< ");
        for (int i=0; i<size(); i++) {
            sb.append(get(i)+" ");
        }
        sb.append(">");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector<?>) {
            return equals((Vector<?>) obj,0);
        }
        return false;
    }
    
    public boolean equals(Vector<?> vector, float epsilon) {
        if (size()!=vector.size()) return false;
        for (int i=0; i<size(); i++) {
            if (Mathf.abs(get(i)-vector.get(i))>epsilon) return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int sum = 0;
        for (int i=0; i<size(); i++) {
            int v = (int) get(i);
            sum += v*Mathf.pow(10,i);
        }
        return sum;
    }
    
}

