package ax.math.vector;

import java.util.Arrays;

/**
 * For calculations using double values <p>
 * 
 * @author Jonathan Lacombe
 */
public class Mathd {

    public static final double E = Math.E;
    
    public static final double PI = Math.PI;
    
    public static final double PI_OVER_4            = PI/4d;
    public static final double PI_OVER_3            = PI/3d;
    public static final double PI_OVER_2            = PI/2d;
    public static final double THREE_PI_OVER_4      = PI*3d/4d;
    public static final double FIVE_PI_OVER_4       = PI*5d/4d;
    public static final double THREE_PI_OVER_2      = PI*3d/2d;
    public static final double SEVEN_PI_OVER_4      = PI*7d/4d;
    public static final double TWO_PI               = PI*2d;
    public static final double TWO_PI_OVER_3        = PI*2d/3d;
    
    public static final double DEGREES_TO_RADIANS   = PI/180d;
    public static final double RADIANS_TO_DEGREES   = 180d/PI;
    
    public static final double EPSILON = 0.00000001d;

    public static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
 
    
    /* ******************************************************************************** */
    /* *****************************    Simple METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static double abs(double a) {
        return Math.abs(a);
    }
    
    public static double acos(double a) {
        return Math.acos(a);
    }
    
    public static double asin(double a) {
        return Math.asin(a);
    }
    
    public static double atan(double a) {
        return Math.atan(a);
    }
    
    public static double atan2(double a, double b) {
        return Math.atan2(a, b);
    }
    
    public static double ceil(double a) {
        return Math.ceil(a);
    }
    
    public static double cos(double rad) {
        return Math.cos(rad);
    }
    
    public static double cosh(double rad) {
        return Math.cosh(rad);
    }
    
    public static double exp(double a) {
        return Math.exp(a);
    }
    
    public static double floor(double a) {
        return Math.floor(a);
    }
    
    public static double ln(double a) {
        return log(a,E);
    }
    
    public static double log(double a) {
        return Math.log(a);
    }
    
    public static double log(double a, double b) {
        return (Math.log(a) / Math.log(b));
    }
    
    public static double log10(double a) {
        return Math.log10(a);
    }
    
    public static double max(double a, double b) {
        return (a>b) ? a : b;
    }
    
    public static double max(double... a) {
        double max = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]>max) max = a[i];
        }
        return max;
    }
    
    public static double min(double a, double b) {
        return (a<b) ? a : b;
    }
    
    public static double min(double... a) {
        double min = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]<min) min = a[i];
        }
        return min;
    }
    
    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }
    
    public static double rand() {
        return Math.random();
    }
    
    public static double rand(double a) {
        return rand() * a;
    }
    
    public static double rand(double min, double max) {
        return rand(max-min) + min;
    }
    
    public static double random() { return rand(); }
    public static double random(double a) { return rand(a); }
    public static double random(double min, double max) { return rand(min,max); }
    
    public static double[] rand(int len, double min, double max) {
        double[] r = new double[len];
        for (int i=0; i<len; i++) {
            r[i] = rand(min,max);
        }
        return r;
    }
    
    public static double[] random(int len, double min, double max) { return rand(len,min,max); }
    
    public static double sin(double rad) {
        return Math.sin(rad);
    }
    
    public static double sinh(double rad) {
        return Math.sinh(rad);
    }
    
    public static double sqrt(double a) {
        return Math.sqrt(a);
    }
    
    public static double invSqrt(double a) {
        return Math.pow(a,-0.5f);
    }
    
    public static double tan(double rad) {
        return Math.tan(rad);
    }
    
    public static double tanh(double rad) {
        return Math.tanh(rad);
    }
    
    public static double toDegrees(double rad) {
        return rad * RADIANS_TO_DEGREES;
    }
    
    public static double deg(double rad) {
        return toDegrees(rad);
    }
    
    public static double toRadians(double deg) {
        return deg * DEGREES_TO_RADIANS;
    }
    
    public static double rad(double deg) {
        return toRadians(deg);
    }
    
    
    
    
    
    public static double average(double... values) {
        double sum = sum(values);
        return sum/values.length;
    }
    public static double avg(double... values) { return average(values); }
    
    public static double sum(double... values) {
        double sum = 0;
        for (double v : values) {
            sum+=v;;
        }
        return sum;
    }
    
    public static double fract(double a) {
        return a - Math.floor(a);
    }
    
    public static double mod(double x, double y) {
        return x - y * floor(x/y);
    }
    
    /**
     * Returns x or min or max if x is out of bounds
     * @param x value to clamp
     * @param min minimum value to return
     * @param max maximum value to return
     * @return clamped x
     */
    public static double clamp(double x, double min, double max) {
        return x < min ? min : (x > max ? max : x);
    }
    
    /**
     * Returns isClamp with geq and leq parameters true
     * @return isClamp with geq and leq parameters true
     * @see #isClamp(double, double, double, boolean, boolean)
     */
    public static boolean isClamped(double x, double min, double max) {
        return x>min && x<max;
    }
    
    public static boolean isClamped(double x) {
        return isClamped(x,0,1);
    }
    
    /**
     * Return true if x is greater than y or if equal to condition
     * is true
     * @param x double value 1
     * @param y double value 2
     * @param equalTo if x==y and this is true returns true
     * @return true if x is greater than y or if equalTo && x==y
     */
    public static boolean geq(double x, double y, boolean equalTo) {
        if (equalTo) return x>=y;
        return x>y;
    }
    
    /**
     * Return true if x is less than y or if equal to condition
     * is true
     * @param x double value 1
     * @param y double value 2
     * @param equalTo if x==y and this is true returns true
     * @return true if x is less than y or if equalTo && x==y
     */
    public static boolean leq(double x, double y, boolean equalTo) {
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
    public static double pattern(double x, double min ,double max) {
        double x_minus_min = x - min;
        boolean positive = x_minus_min > 0;
        double abs_diff = abs(x_minus_min);
        double mod_range = abs_diff%(max-min);
        if (mod_range==0) {
            return min;
        }
        if (positive) {
            return mod_range + min;
        }
        return max - mod_range;
    }
    
    public static double lerp(double alpha, double a, double b) {
        return a + alpha * (b - a);
    }
    
    public static double slerp(double alpha, double a, double b) {
        return lerp(sin(alpha*PI_OVER_2),a,b);
    }
    
    public static double alpha(double value, double min, double max) {
        double n = value - min;
        double d = max - min;
        return n / d;
    }
    
    public static double[] inverseAlphas(double[] alphas) {
        //Using doubles for larger precision
        double[] inverses = new double[alphas.length];
        double sum = 0;
        for (int i=0; i<alphas.length; i++) {
            if (alphas[i]!=0) {
                inverses[i] = 1.0 / alphas[i];
                sum += inverses[i];
            }
        }
        double[] ret = new double[alphas.length];
        for (int i=0; i<alphas.length; i++) {
            if (alphas[i]!=0) {
                ret[i] = (inverses[i] / sum);
            }
        }
        return ret;
    }
    
    
    public static double dist(double x1, double y1, double x2, double y2) {
        double x = x2-x1;
        double y = y2-y1;
        return sqrt(x*x + y*y);
    }
    
    public static double slope(double x1, double y1, double x2, double y2) {
        return (y2-y1)/(x2-x1);
    }
    
    public static double length(double x, double y) {
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
    public static double dot(double[] u, double[] v) {
        double sum = 0;
        for (int i=0; i<u.length; i++)
            sum += u[i] + v[i];
        return sum;
    }
    
    public static double dot(double ux, double uy, double vx, double vy) {
        return ux*vx + uy*vy;
    }
    
    public static double dot(double ux, double uy, double uz, double vx, double vy, double vz) {
        return ux*vx + uy*vy + uz*vz;
    }
    
    public static double dot(double ux, double uy, double uz, double uw, double vx, double vy, double vz, double vw) {
        return ux*vx + uy*vy + uz*vz + uw*vw;
    }
    
    /**
     * @return dot(v,v)
     * @see #dot(double[], double[])
     */
    public static double mag2(double... v) {
        double sum = 0;
        for (double f : v)
            sum += f*f;
        return sum;
    }
    
    /** @see #dot(double...) */
    public static double len2(double... v) { return mag2(v); }
    
    public static double len(double... v) {
        return sqrt(mag2(v));
    }
    
    public static double mag(double... v) { return len(v); }
    
    public static boolean eq(double a, double b) {
        return abs(a-b) <= EPSILON;
    }
    
    public static boolean eq(double a, double b, double epsilon) {
        return abs(a-b) <= epsilon;
    }
    
    public static boolean isEqual(double a, double b, double epsilon) {
        return eq(a,b,epsilon);
    }
    
    public static boolean isEqual(double a, double b) {
        return eq(a,b);
    }
    
    public static boolean geq(double a, double b, double epsilon) {
        if (a > b) {
            return true;
        }
        return eq(a,b,epsilon);
    }
    
    public static boolean leq(double a, double b, double epsilon) {
        if (a < b) {
            return true;
        }
        return eq(a,b,epsilon);
    }
    
    public static boolean zero(double x) {
        return abs(x) <= EPSILON;
    }
    
    public static boolean zero(double x, double epsilon) {
        return abs(x) <= epsilon;
    }
    
    public static boolean isZero(double x) { return zero(x); }
    public static boolean isZero(double x, double epsilon) { return zero(x,epsilon); }
    
    public static boolean one(double x) { return eq(x,1); } 
    public static boolean one(double x, double epsilon) { return eq(x,1,epsilon); }
    
    public static boolean isOne(double x) { return one(x); }
    public static boolean isOne(double x, double epsilon) { return one(x,epsilon); }
    
    public static double rad(double x, double y) {
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
        double ref = abs(atan(y/x));
        
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
    
    public static double deg(double x, double y) {
        return toDegrees(rad(x,y));
    }

    public static double radBetween(double originX, double originY, double targetX, double targetY) {
        double o = targetY - originY;
        double a = targetX - originX;
        return rad(a,o);
    }
    
    public static double degBetween(double originX, double originY, double targetX, double targetY) {
        return toDegrees(radBetween(originX,originY,targetX,targetY));
    }
    
    public static double[][] zeros(int w, int h) {
        double[][] f = new double[h][w];
        for (int i=0; i<h; i++) {
            f[i] = new double[w];
        }
        return f;
    }
    
    public static double[][] ones(int w, int h) {
        double[][] f = new double[h][w];
        for (int i=0; i<h; i++) {
            for (int j=0; j<w; j++) {
                f[i][j] = 1;
            }
        }
        return f;
    }
    
    public static boolean toBoolean(double f) {
        return f==0 ? false : true;
    }
    
    public static int toInt(double f) {
        return (int) f;
    }
    
    public static double todouble(boolean b) {
        return b ? 1 : 0;
    }
    
    public static double todouble(int i) {
        return i;
    }
    
    // isWithin double
    
    public static boolean isWithin(double x, double y, double x1, double y1, double x2, double y2) {
        if (x<x1) return false;
        if (y<y1) return false;
        if (x>x2) return false;
        if (y>y2) return false;
        return true;
    }
    
    public static boolean isWithinBounds(double x, double y, double bx, double by, double bwidth, double bheight) {
        return isWithin(x,y,bx,by,bx+bwidth,by+bheight);
    }
    
    public static boolean isWithin(double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2) {
        if (x<x1) return false;
        if (y<y1) return false;
        if (x>x2) return false;
        if (y>y2) return false;
        if (z<z1) return false;
        if (z>z2) return false;
        return true;
    }
    
    public static boolean isWithinBounds(double x, double y, double z, double bx, double by, double bz,
            double bwidth, double bheight, double blength) {
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
    /* **************************    double-ARRAY METHODS      ************************* */
    /* ******************************************************************************** */
    
    public static void addBy(double[] dst, double[] add) {
        for (int i=0; i<dst.length; i++) {
            dst[i] += add[i];
        }
    }
    
    public static double[] sum(double[]... arrays) {
        double[] ret = new double[arrays[0].length];
        for (int i=0; i<arrays.length; i++) {
            addBy(ret,arrays[i]);
        }
        return ret;
    }
    
    public static void divBy(double[] array, double v) {
        for (int i=0; i<array.length; i++) {
            array[i] /= v;
        }
    }
    
    public static void mulBy(double[] array, double v) {
        for (int i=0; i<array.length; i++) {
            array[i] *= v;
        }
    }
    
    public static double[] mul(double v, double[] array) {
        double[] ret = Arrays.copyOf(array, array.length);
        for (int i=0; i<ret.length; i++) {
            ret[i] = ret[i]*v;
        }
        return ret;
    }
    
    public static double[] lerp(double alpha, double[] array1, double[] array2) {
        double[] ret = new double[array1.length];
        for (int i=0; i<ret.length; i++) {
            ret[i] = lerp(alpha,array1[i],array2[i]);
        }
        return ret;
    }
    
}
