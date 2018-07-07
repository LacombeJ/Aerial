package ax.math.vector;

import java.util.Arrays;



/**
 * The Mathf class for single-precision math <p>
 * Extension to java math class<p>
 * Some methods from libGDX and THREE.js
 * https://github.com/mrdoob/three.js/blob/master/src/math/Math.js
 * 
 * @author Jonathan Lacombe
 */
public final class Mathf {

    /**
     * The constant <i>e</i>
     * <p>
     * Known as Euler's number / Napier's Constant
     */
    public static final float E = (float) Math.E;
    
    /**
     * The constant &#960 (pi)<pre>
     */
    public static final float PI = (float) Math.PI;
    
    public static final float PI_OVER_4            = PI/4f;
    public static final float PI_OVER_3            = PI/3f;
    public static final float PI_OVER_2            = PI/2f;
    public static final float THREE_PI_OVER_4      = PI*3f/4f;
    public static final float FIVE_PI_OVER_4       = PI*5f/4f;
    public static final float THREE_PI_OVER_2      = PI*3f/2f;
    public static final float SEVEN_PI_OVER_4      = PI*7f/4f;
    public static final float TWO_PI               = PI*2f;
    public static final float TWO_PI_OVER_3        = PI*2f/3f;
    
    public static final float DEGREES_TO_RADIANS   = PI/180f;
    public static final float RADIANS_TO_DEGREES   = 180f/PI;
    
    public static final float LN2 = 0.6931471805599453f;
    
    /**
     * A constant epsilon for comparing floats
     */
    public static final float EPSILON = 0.000001f;

    public static final float POSITIVE_INFINITY = Float.POSITIVE_INFINITY;
    public static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;
    
    public static final float NaN = Float.NaN;
    
    /* ******************************************************************************** */
    /* *****************************    Simple METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static float abs(float a) {
        return Math.abs(a);
    }
    
    public static float acos(float a) {
        return (float) Math.acos(a);
    }
    
    public static float asin(float a) {
        return (float) Math.asin(a);
    }
    
    public static float atan(float a) {
        return (float) Math.atan(a);
    }
    
    public static float atan2(float a, float b) {
        return (float) Math.atan2(a, b);
    }
    
    public static float ceil(float a) {
        return (float) Math.ceil(a);
    }
    
    public static float cos(float rad) {
        return (float) Math.cos(rad);
    }
    
    public static float cosh(float rad) {
        return (float) Math.cosh(rad);
    }
    
    public static float exp(float a) {
        return (float) Math.exp(a);
    }
    
    public static float floor(float a) {
        return (float) Math.floor(a);
    }
    
    /**
     * Returns the natural of a given value<p>
     * Uses the following equation to determine the natural log:
     * <pre>ln(a) = log(a)/log(E)
     * where E is Euler's number</pre>
     * @param a value
     * @return natural log of value
     */
    public static float ln(float a) {
        return log(a,E);
    }
    
    public static float log(float a) {
        return (float) Math.log(a);
    }
    
    /**
     * Returns log<sub>b</sub>a
     * @param a value
     * @param b base
     * @return log<sub>b</sub>a
     */
    public static float log(float a, float b) {
        return (float) (Math.log(a) / Math.log(b));
    }
    
    public static float log10(float a) {
        return (float) Math.log10(a);
    }
    
    public static float max(float a, float b) {
        return (a>b) ? a : b;
    }
    
    public static float max(float... a) {
        float max = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]>max) max = a[i];
        }
        return max;
    }
    
    public static float min(float a, float b) {
        return (a<b) ? a : b;
    }
    
    public static float min(float... a) {
        float min = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]<min) min = a[i];
        }
        return min;
    }
    
    public static float pow(float a, float b) {
        return (float) Math.pow(a, b);
    }
    
    public static float rand() {
        return (float) Math.random();
    }
    
    /**
     * Returns a float greater than or equal to 0.0 and less than a
     * @param a upper bound
     * @return 0.0 &#8804 x < a
     */
    public static float rand(float a) {
        return rand() * a;
    }
    
    public static float rand(float min, float max) {
        return rand(max-min) + min;
    }
    
    public static float random() { return rand(); }
    public static float random(float a) { return rand(a); }
    public static float random(float min, float max) { return rand(min,max); }
    
    public static float[] rand(int len, float min, float max) {
        float[] r = new float[len];
        for (int i=0; i<len; i++) {
            r[i] = rand(min,max);
        }
        return r;
    }
    
    public static float[] random(int len, float min, float max) { return rand(len,min,max); }
    
    public static int round(float a) {
        return Math.round(a);
    }
    
    /**
     * Round to the nearest number location to a value with the increment starting from start
     * 
     * @param a
     * @param start
     * @param increment
     * @return
     */
    public static float round(float a, float start, float increment) {
        return round((a-start)/increment)*increment + start;
    }
    
    public static float sin(float rad) {
        return (float) Math.sin(rad);
    }
    
    public static float sinh(float rad) {
        return (float) Math.sinh(rad);
    }
    
    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }
    
    public static float invSqrt(float a) {
        return (float) Math.pow(a,-0.5f);
    }
    
    public static float tan(float rad) {
        return (float) Math.tan(rad);
    }
    
    public static float tanh(float rad) {
        return (float) Math.tanh(rad);
    }
    
    public static float toDegrees(float rad) {
        return rad * RADIANS_TO_DEGREES;
    }
    
    public static float deg(float rad) {
        return toDegrees(rad);
    }
    
    public static float toRadians(float deg) {
        return deg * DEGREES_TO_RADIANS;
    }
    
    public static float rad(float deg) {
        return toRadians(deg);
    }
    
    
    /* ******************************************************************************** */
    /* ***************************    Extended METHODS      *************************** */
    /* ******************************************************************************** */
    
    
    public static float average(float... values) {
        float sum = sum(values);
        return sum/values.length;
    }
    public static float avg(float... values) { return average(values); }
    
    public static float sum(float... values) {
        float sum = 0;
        for (float v : values) {
            sum+=v;;
        }
        return sum;
    }
    
    public static float fract(float a) {
        return a - Mathf.floor(a);
    }
    
    public static float mod(float x, float y) {
        return x - y * floor(x/y);
    }
    
    /**
     * Returns x or min or max if x is out of bounds
     * @param x value to clamp
     * @param min minimum value to return
     * @param max maximum value to return
     * @return clamped x
     */
    public static float clamp(float x, float min, float max) {
        return x < min ? min : (x > max ? max : x);
    }
    
    /**
     * Returns isClamp with geq and leq parameters true
     * @return isClamp with geq and leq parameters true
     * @see #isClamp(float, float, float, boolean, boolean)
     */
    public static boolean isClamped(float x, float min, float max) {
        return x>min && x<max;
    }
    
    public static boolean isClamped(float x) {
        return isClamped(x,0,1);
    }
    
    /**
     * Return true if x is greater than y or if equal to condition
     * is true
     * @param x float value 1
     * @param y float value 2
     * @param equalTo if x==y and this is true returns true
     * @return true if x is greater than y or if equalTo && x==y
     */
    public static boolean geq(float x, float y, boolean equalTo) {
        if (equalTo) return x>=y;
        return x>y;
    }
    
    /**
     * Return true if x is less than y or if equal to condition
     * is true
     * @param x float value 1
     * @param y float value 2
     * @param equalTo if x==y and this is true returns true
     * @return true if x is less than y or if equalTo && x==y
     */
    public static boolean leq(float x, float y, boolean equalTo) {
        if (equalTo) return x<=y;
        return x<y;
    }
    
    /**
     * <pre>
     * pattern(25,  50, 100) = 75
     * pattern(50,  50, 100) = 50
     * pattern(100, 50, 100) = 50
     * pattern(125, 50, 100) = 75
     * pattern(0,   50, 100) = 50
     * pattern(-50, 50, 100) = 50
     * </pre>
     * @return a mapping of a real number to a value in the given range
     */
    public static float pattern(float x, float min ,float max) {
        float x_minus_min = x - min;
        boolean positive = x_minus_min > 0;
        float abs_diff = abs(x_minus_min);
        float mod_range = abs_diff%(max-min);
        if (mod_range==0) {
            return min;
        }
        if (positive) {
            return mod_range + min;
        }
        return max - mod_range;
    }
    
    public static float modulo(float n, float m) {
        return ( ( n % m ) + m ) % m;
    }
    
    public static float lerp(float alpha, float a, float b) {
        return a + alpha * (b - a);
    }
    
    public static float slerp(float alpha, float a, float b) {
        return lerp(sin(alpha*PI_OVER_2),a,b);
    }
    
    public static float alpha(float value, float min, float max) {
        float n = value - min;
        float d = max - min;
        return n / d;
    }
    
    /**
     * Alpha values with 0 will remain 0
     * @param alphas list of values that add up to 1
     * @return inverted list of values that also add up to be one such that the previous
     * largest alpha value is now the smallest and vice-versa
     */
    public static float[] inverseAlphas(float[] alphas) {
        //Using doubles for larger precision
        double[] inverses = new double[alphas.length];
        double sum = 0;
        for (int i=0; i<alphas.length; i++) {
            if (alphas[i]!=0) {
                inverses[i] = 1.0 / alphas[i];
                sum += inverses[i];
            }
        }
        float[] ret = new float[alphas.length];
        for (int i=0; i<alphas.length; i++) {
            if (alphas[i]!=0) {
                ret[i] = (float) (inverses[i] / sum);
            }
        }
        return ret;
    }
    
    
    public static float dist(float x1, float y1, float x2, float y2) {
        float x = x2-x1;
        float y = y2-y1;
        return sqrt(x*x + y*y);
    }
    
    public static float slope(float x1, float y1, float x2, float y2) {
        return (y2-y1)/(x2-x1);
    }
    
    public static float length(float x, float y) {
        return dist(0,0,x,y);
    }
    
    /**
     * Returns dot product of given vectors<p>
     * Assumes that these vectors are the same size and thus
     * uses u.length for loop stopper
     * @param u
     * @param v
     * @return dot product of 
     */
    public static float dot(float[] u, float[] v) {
        float sum = 0;
        for (int i=0; i<u.length; i++)
            sum += u[i] + v[i];
        return sum;
    }
    
    public static float dot(float ux, float uy, float vx, float vy) {
        return ux*vx + uy*vy;
    }
    
    public static float dot(float ux, float uy, float uz, float vx, float vy, float vz) {
        return ux*vx + uy*vy + uz*vz;
    }
    
    public static float dot(float ux, float uy, float uz, float uw, float vx, float vy, float vz, float vw) {
        return ux*vx + uy*vy + uz*vz + uw*vw;
    }
    
    /**
     * @return dot(v,v)
     * @see #dot(float[], float[])
     */
    public static float mag2(float... v) {
        float sum = 0;
        for (float f : v)
            sum += f*f;
        return sum;
    }
    
    /** @see #dot(float...) */
    public static float len2(float... v) { return mag2(v); }
    
    public static float len(float... v) {
        return sqrt(mag2(v));
    }
    
    public static float mag(float... v) { return len(v); }
    
    public static boolean eq(float a, float b) {
        return abs(a-b) <= EPSILON;
    }
    
    public static boolean eq(float a, float b, float epsilon) {
        return abs(a-b) <= epsilon;
    }
    
    public static boolean isEqual(float a, float b, float epsilon) {
        return eq(a,b,epsilon);
    }
    
    public static boolean isEqual(float a, float b) {
        return eq(a,b);
    }
    
    public static boolean geq(float a, float b, float epsilon) {
        if (a > b) {
            return true;
        }
        return eq(a,b,epsilon);
    }
    
    public static boolean leq(float a, float b, float epsilon) {
        if (a < b) {
            return true;
        }
        return eq(a,b,epsilon);
    }
    
    public static boolean zero(float x) {
        return abs(x) <= EPSILON;
    }
    
    public static boolean zero(float x, float epsilon) {
        return abs(x) <= epsilon;
    }
    
    public static boolean isZero(float x) { return zero(x); }
    public static boolean isZero(float x, float epsilon) { return zero(x,epsilon); }
    
    public static boolean one(float x) { return eq(x,1); } 
    public static boolean one(float x, float epsilon) { return eq(x,1,epsilon); }
    
    public static boolean isOne(float x) { return one(x); }
    public static boolean isOne(float x, float epsilon) { return one(x,epsilon); }
    
    public static float rad(float x, float y) {
        if (y==0 && x==0) {
            return 0;
        } else if (y==0) {
            if (x>0) {
                return 0;
            } else {
                return PI;
            }
        } else if (x==0) {
            if (y>0) {
                return PI_OVER_2;
            } else {
                return THREE_PI_OVER_2;
            }
        }
        float ref = abs(atan(y/x));
        
        if (x>0 && y>0) {
            return ref;
        } else if (x<0 && y>0) {
            return PI - ref;
        } else if (x<0 && y<0) {
            return ref + PI;
        } else if (x>0 && y<0) {
            return TWO_PI - ref;
        }
        
        return 0;
    }
    
    public static float deg(float x, float y) {
        return toDegrees(rad(x,y));
    }

    public static float radBetween(float originX, float originY, float targetX, float targetY) {
        float o = targetY - originY;
        float a = targetX - originX;
        return rad(a,o);
    }
    
    public static float degBetween(float originX, float originY, float targetX, float targetY) {
        return toDegrees(radBetween(originX,originY,targetX,targetY));
    }
    
    public static float[][] zeros(int w, int h) {
        float[][] f = new float[h][w];
        for (int i=0; i<h; i++) {
            f[i] = new float[w];
        }
        return f;
    }
    
    public static float[][] ones(int w, int h) {
        float[][] f = new float[h][w];
        for (int i=0; i<h; i++) {
            for (int j=0; j<w; j++) {
                f[i][j] = 1;
            }
        }
        return f;
    }
    
    public static boolean toBoolean(float f) {
        return f==0 ? false : true;
    }
    
    public static int toInt(float f) {
        return (int) f;
    }
    
    public static float toFloat(boolean b) {
        return b ? 1 : 0;
    }
    
    public static float toFloat(int i) {
        return (float) i;
    }
    
    // isWithin Float
    
    public static boolean isWithin(float x, float y, float x1, float y1, float x2, float y2) {
        if (x<x1) return false;
        if (y<y1) return false;
        if (x>x2) return false;
        if (y>y2) return false;
        return true;
    }
    
    public static boolean isWithinBounds(float x, float y, float bx, float by, float bwidth, float bheight) {
        return isWithin(x,y,bx,by,bx+bwidth,by+bheight);
    }
    
    public static boolean isWithin(float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2) {
        if (x<x1) return false;
        if (y<y1) return false;
        if (x>x2) return false;
        if (y>y2) return false;
        if (z<z1) return false;
        if (z>z2) return false;
        return true;
    }
    
    public static boolean isWithinBounds(float x, float y, float z, float bx, float by, float bz,
            float bwidth, float bheight, float blength) {
        return isWithin(x,y,z,bx,by,bz,bx+bwidth,by+bheight,bz+blength);
    }
    
    // isWithin Integer
    
    public static boolean isWithin(int x, int y, int x1, int y1, int x2, int y2) {
        if (x<x1) return false;
        if (y<y1) return false;
        if (x>x2) return false;
        if (y>y2) return false;
        return true;
    }
    
    public static boolean isWithinBounds(int x, int y, int bx, int by, int bwidth, int bheight) {
        return isWithin(x,y,bx,by,bx+bwidth,by+bheight);
    }
    
    public static boolean isWithin(int x, int y, int z, int x1, int y1, int z1, int x2, int y2, int z2) {
        if (x<x1) return false;
        if (y<y1) return false;
        if (x>x2) return false;
        if (y>y2) return false;
        if (z<z1) return false;
        if (z>z2) return false;
        return true;
    }
    
    public static boolean isWithinBounds(int x, int y, int z, int bx, int by, int bz,
            int bwidth, int bheight, int blength) {
        return isWithin(x,y,z,bx,by,bz,bx+bwidth,by+bheight,bz+blength);
    }
    
    
    /* ******************************************************************************** */
    /* **************************    FLOAT-ARRAY METHODS      ************************* */
    /* ******************************************************************************** */
    
    public static void addBy(float[] dst, float[] add) {
        for (int i=0; i<dst.length; i++) {
            dst[i] += add[i];
        }
    }
    
    public static float[] sum(float[]... arrays) {
        float[] ret = new float[arrays[0].length];
        for (int i=0; i<arrays.length; i++) {
            addBy(ret,arrays[i]);
        }
        return ret;
    }
    
    public static void divBy(float[] array, float v) {
        for (int i=0; i<array.length; i++) {
            array[i] /= v;
        }
    }
    
    public static void mulBy(float[] array, float v) {
        for (int i=0; i<array.length; i++) {
            array[i] *= v;
        }
    }
    
    public static float[] mul(float v, float[] array) {
        float[] ret = Arrays.copyOf(array, array.length);
        for (int i=0; i<ret.length; i++) {
            ret[i] = ret[i]*v;
        }
        return ret;
    }
    
    public static float[] lerp(float alpha, float[] array1, float[] array2) {
        float[] ret = new float[array1.length];
        for (int i=0; i<ret.length; i++) {
            ret[i] = lerp(alpha,array1[i],array2[i]);
        }
        return ret;
    }
    
    
    /* ******************************************************************************** */
    /* *****************************    VECTOR METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static Vector2 vec2(float x, float y)                        { return new Vector2(x,y); }
    public static Vector2 vec2(Vector2 v)                               { return new Vector2(v); }
    
    public static Vector3 vec3(float x, float y, float z)               { return new Vector3(x,y,z); }
    public static Vector3 vec3(Vector3 v)                               { return new Vector3(v); }
    public static Vector3 vec3(Vector2 v, float z)                      { return new Vector3(v,z); }
    
    public static Vector4 vec4(float x, float y, float z, float w)      { return new Vector4(x,y,z,w); }
    public static Vector4 vec4(Vector4 v)                               { return new Vector4(v); }
    public static Vector4 vec4(Vector3 v, float w)                      { return new Vector4(v,w); }
    public static Vector4 vec4(Vector2 v, float z, float w)             { return new Vector4(v,z,w); }
    
    public static <T extends Vector<T>> T add(T u, T v) {
        return u.get().add(v);
    }
    public static <T extends Vector<T>> T add(T u, float v) {
        return u.get().add(v);
    }
    public static <T extends Vector<T>> T sub(T u, T v) {
        return u.get().sub(v);
    }
    public static <T extends Vector<T>> T sub(T u, float v) {
        return u.get().sub(v);
    }
    public static <T extends Vector<T>> T mul(T u, T v) {
        return u.get().multiply(v);
    }
    public static <T extends Vector<T>> T mul(T u, float v) {
        return u.get().multiply(v);
    }
    public static <T extends Vector<T>> T div(T u, T v) {
        return u.get().divide(v);
    }
    public static <T extends Vector<T>> T div(T u, float v) {
        return u.get().divide(v);
    }
    
    public static <T extends Vector<T>> T abs(T v) {
        return v.get().abs();
    }
    
    public static <T extends Vector<T>> T min(T v, float a) {
        return v.get().min(a);
    }
    public static <T extends Vector<T>> T max(T v, float a) {
        return v.get().max(a);
    }
    
    public static <T extends Vector<T>> T floor(T v) {
        return v.get().floor();
    }
    public static <T extends Vector<T>> T ceil(T v) {
        return v.get().ceil();
    }
    public static <T extends Vector<T>> T round(T v) {
        return v.get().round();
    }
    public static <T extends Vector<T>> T fract(T v) {
        return v.get().fract();
    }
    public static <T extends Vector<T>> T mod(T v, float a) {
        return v.get().mod(a);
    }
    
    public static <T extends Vector<T>> T neg(T v) {
        return v.get().neg();
    }
    
    public static float mag(Vector<?> v) {
        return v.magnitude();
    }
    
    public static <T extends Vector<T>> T norm(T v) {
        return v.get().norm();
    }
    
    public static <T extends Vector<T>> float dot(T u, T v) {
        return u.dot(v);
    }
    
    public static <T extends Vector<T>> T proj(T u, T v) {
        return u.get().proj(v);
    }
    
    
    
    public static Vector3 cross(Vector3 u, Vector3 v) {
        return Vector3.cross(u, v);
    }
    
    public static Vector3 normal(Vector3 a, Vector3 b, Vector3 c) {
        return norm(cross(sub(b,a),sub(c,a)));
    }
    
    /* ******************************************************************************** */
    /* *****************************    MATRIX METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static Matrix2 mat2(
            float m00, float m01,
            float m10, float m11) {
        return new Matrix2(
                m00,m01,
                m10,m11);
    }
    public static Matrix2 mat2(
            float m00,  /*  0   */
            /*  0   */  float m11) {
        return new Matrix2(m00,m11);
    }
    public static Matrix2 mat2(Matrix2 mat) {
        return new Matrix2(mat);
    }
    public static Matrix2 mat2(Vector2 x, Vector2 y) {
        return new Matrix2(x,y);
    }
    
    public static Matrix3 mat3(
            float m00, float m01, float m02,
            float m10, float m11, float m12,
            float m20, float m21, float m22) {
        return new Matrix3(
                m00,m01,m02,
                m10,m11,m12,
                20,m21,m22);
    }
    public static Matrix3 mat3(
            float m00, /*  0   */ /*  0   */
            /*  0   */ float m11, /*  0   */
            /*  0   */ /*  0   */ float m22) {
        return new Matrix3(m00,m11,m22);
    }
    public static Matrix3 mat3(Matrix3 mat) {
        return new Matrix3(mat);
    }
    public static Matrix3 mat3(Vector3 x, Vector3 y, Vector3 z) {
        return new Matrix3(x,y,z);
    }
    
    public static Matrix4 mat4(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        return new Matrix4(
                m00,m01,m02,m03,
                m10,m11,m12,m13,
                m20,m21,m22,m23,
                m30,m31,m32,m33);
    }
    public static Matrix4 mat4(
            float m00, /*  0   */ /*  0   */ /*  0   */
            /*  0   */ float m11, /*  0   */ /*  0   */
            /*  0   */ /*  0   */ float m22, /*  0   */
            /*  0   */ /*  0   */ /*  0   */ float m33) {
        return new Matrix4(m00,m11,m22,m33);
    }
    public static Matrix4 mat4(Matrix4 mat) {
        return new Matrix4(mat);
    }
    public static Matrix4 mat4(Vector4 x, Vector4 y, Vector4 z, Vector4 w) {
        return new Matrix4(x,y,z,w);
    }
    
    
    
    /* ******************************************************************************** */
    /* ***************************    QUATERNION METHODS      ************************* */
    /* ******************************************************************************** */
    
    public static Quaternion quat(float x, float y, float z, float w)   { return new Quaternion(x,y,z,w); }
    public static Quaternion quat(Quaternion quat)                      { return new Quaternion(quat); }
    
    public static Quaternion conjugate(Quaternion quat) {
        return quat.get().conjugate();
    }
    
    public static Quaternion conj(Quaternion quat) {
        return conjugate(quat);
    }
    
    
    
    /* ******************************************************************************** */
    /* *****************************    Extra  METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static boolean isWithinBounds(float x, float y, Vector4 bounds) {
        return isWithinBounds(x,y,bounds.x,bounds.y,bounds.z,bounds.w);
    }
    
    public static boolean isWithinBounds(Vector2 p, Vector4 bounds) {
        return isWithinBounds(p.x,p.y,bounds);
    }
    
    public static boolean isWithinBounds(float x, float y, float z, Vector3 min, Vector3 size) {
        return isWithinBounds(x,y,z,min.x,min.y,min.z,size.x,size.y,size.z);
    }
    
    public static boolean isWithinBounds(Vector3 p, Vector3 min, Vector3 size) {
        return isWithinBounds(p.x,p.y,p.z,min,size);
    }    

    
}
