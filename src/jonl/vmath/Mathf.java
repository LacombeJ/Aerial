package jonl.vmath;

import java.util.Arrays;

/**
 * The Mathf class for single-precision math <p>
 * Extension to java math class<p>
 * Some methods from libGDX
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
    
    /**
     * A constant epsilon for comparing floats
     */
    public static final float EPSILON = 0.000001f;
    
    
    /* ******************************************************************************** */
    /* *****************************    Simple METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static int abs(int a) {
        return Math.abs(a);
    }
    
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
     * Returns the greatest-common-divisor of a and b using the euclidean algorithm.
     * @param a integer a
     * @param b integer b
     * @return the greatest-common-divisor of a and b
     */
    public static int gcd(int a, int b) {
        a = (int) Math.abs(a);
        b = (int) Math.abs(b);
        if (b>a)
            return gcd(b,a);
        else {
            if (b==0)
                return a;
            else {
                int quotient = a/b;
                int remainder = a-(quotient*b);
                return (remainder!=0) ? gcd(b,remainder) : b;
            }
        }
    }
    
    /**
     * Returns the greatest-common-divisor of a and b using the euclidean algorithm.
     * @param a long a
     * @param b long b
     * @return the greatest-common-divisor of a and b
     */
    public static long gcd(long a, long b) {
        a = (long) Math.abs(a);
        b = (long) Math.abs(b);
        if (b>a)
            return gcd(b,a);
        else {
            if (b==0)
                return a;
            else {
                long quotient = a/b;
                long remainder = a-(quotient*b);
                return (remainder!=0) ? gcd(b,remainder) : b;
            }
        }
    }
    
    /**
     * Checks whether a value is even
     * @param a value
     * @return true if this value is even
     */
    public static boolean isEven(int a) {
        return (a%2==0) ? true : false;
    }
    public static boolean even(int a) { return isEven(a); }
    public static boolean isOdd(int a) { return !isEven(a); }
    public static boolean odd(int a) { return isOdd(a); }
    
    /**
     * Returns the least-common-divisor using the formula (a*b) = gcd(a,b)*lcd(a,b) where
     * gcd is found using the euclidean algorithm.
     * @param a integer a
     * @param b integer b
     * @return the least-common-divisor of a and b
     */
    public static int lcd(int a, int b) {
        int gcd = gcd(a,b);
        return a*b / gcd;
    }
    
    /**
     * Returns the least-common-divisor using the formula (a*b) = gcd(a,b)*lcd(a,b) where
     * gcd is found using the euclidean algorithm.
     * @param a long a
     * @param b long b
     * @return the least-common-divisor of a and b
     */
    public static long lcd(long a, long b) {
        long gcd = gcd(a,b);
        return a*b / gcd;
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
    
    public static int max(int a, int b) {
        return (a>b) ? a : b;
    }
    
    public static int max(int... a) {
        int max = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]>max) max = a[i];
        }
        return max;
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
    
    public static int min(int a, int b) {
        return (a<b) ? a : b;
    }
    
    public static int min(int... a) {
        int min = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]<min) min = a[i];
        }
        return min;
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
        return (rand(max-min) + min);
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
    
    public static int randInt(int a) {
        return (int) Math.round(Math.random()*a);
    }
    
    public static int round(float a) {
        return Math.round(a);
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
    
    public static float lerp(float alpha, float a, float b) {
        return a + alpha * (b - a);
    }
    
    public static float alpha(float value, float min, float max) {
        float n = value - min;
        float d = max - min;
        return n / d;
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
                return PI/2f;
            } else {
                return 3*PI/2f;
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
            return 2*PI - ref;
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
    
    public static int ceilMultiple(int i, int mult) {
        if (i%mult==0) return i;
        int j = i/mult;
        return (j+1)*mult;
    }
    
    public static int floorMultiple(int i, int mult) {
        if (i%mult==0) return i;
        int j = i/mult;
        return j*mult;
    }
    
    public static boolean isPowerOfTwo(int i) {
        return (i & (i-1) ) == 0;
    }
    
    public static int nextPowerOfTwo(int i) {
        if (i==0) return 2;
        int max = 0b01000000000000000000000000000000;
        while ((max & i)!=max) {
            max = max >> 1;
        }
        return max << 1;
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
    
    public static boolean toBoolean(int i) {
        return i==0 ? false : true;
    }
    
    public static int toInt(boolean b) {
        return b ? 1 : 0;
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
    /* ****************************    Int-ARRAY METHODS      ************************* */
    /* ******************************************************************************** */
    
    public static void addBy(int[] dst, int[] add) {
        for (int i=0; i<dst.length; i++) {
            dst[i] += add[i];
        }
    }
    
    public static int[] sum(int[]... arrays) {
        int[] ret = new int[arrays[0].length];
        for (int i=0; i<arrays.length; i++) {
            addBy(ret,arrays[i]);
        }
        return ret;
    }
    
    public static void divBy(int[] array, int v) {
        for (int i=0; i<array.length; i++) {
            array[i] /= v;
        }
    }
    
    public static void mulBy(int[] array, int v) {
        for (int i=0; i<array.length; i++) {
            array[i] *= v;
        }
    }
    
    public static int[] mul(int v, int[] array) {
        int[] ret = Arrays.copyOf(array, array.length);
        for (int i=0; i<ret.length; i++) {
            ret[i] = ret[i]*v;
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
    
    public static <T extends Vector<T>> T sub(T u, T v) {
        return u.get().sub(v);
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
