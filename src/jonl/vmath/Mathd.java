package jonl.vmath;

/**
 * The Mathd class for double-precision math <p>
 * Extension to java math class<p>
 * 
 * @author Jonathan Lacombe
 */
public final class Mathd {

    /**
     * The constant <i>e</i>
     * <p>
     * Known as Euler's number / Napier's Constant
     */
    public static final double E = (double) Math.E;
    
    /**
     * The constant &#960 (pi)<pre>
     */
    public static final double PI = (double) Math.PI;
    
    public static final double PI_OVER_4            = PI/4.0;
    public static final double PI_OVER_3            = PI/3.0;
    public static final double PI_OVER_2            = PI/2.0;
    public static final double THREE_PI_OVER_4      = PI*3.0/4.0;
    public static final double FIVE_PI_OVER_4       = PI*5.0/4.0;
    public static final double THREE_PI_OVER_2      = PI*3.0/2.0;
    public static final double SEVEN_PI_OVER_4      = PI*7.0/4.0;
    public static final double TWO_PI               = PI*2.0;
    public static final double TWO_PI_OVER_3        = PI*2.0/3.0;
    
    public static final double DEGREES_TO_RADIANS   = PI/180.0;
    public static final double RADIANS_TO_DEGREES   = 180.0/PI;
    
    /**
     * A constant epsilon for comparing doubles
     */
    public static final double EPSILON = 0.0000001;
    
    
    /* ******************************************************************************** */
    /* *****************************    Simple METHODS      *************************** */
    /* ******************************************************************************** */
    
    public static int abs(int a) {
        return Math.abs(a);
    }
    
    public static double abs(double a) {
        return Math.abs(a);
    }
    
    public static double acos(double a) {
        return (double) Math.acos(a);
    }
    
    public static double asin(double a) {
        return (double) Math.asin(a);
    }
    
    public static double atan(double a) {
        return (double) Math.atan(a);
    }
    
    public static double atan2(double a, double b) {
        return (double) Math.atan2(a, b);
    }
    
    public static double ceil(double a) {
        return (double) Math.ceil(a);
    }
    
    public static double cos(double rad) {
        return (double) Math.cos(rad);
    }
    
    public static double cosh(double rad) {
        return (double) Math.cosh(rad);
    }
    
    public static double exp(double a) {
        return (double) Math.exp(a);
    }
    
    public static double floor(double a) {
        return (double) Math.floor(a);
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
    public static double ln(double a) {
        return log(a,E);
    }
    
    public static double log(double a) {
        return (double) Math.log(a);
    }
    
    /**
     * Returns log<sub>b</sub>a
     * @param a value
     * @param b base
     * @return log<sub>b</sub>a
     */
    public static double log(double a, double b) {
        return (double) (Math.log(a) / Math.log(b));
    }
    
    public static double log10(double a) {
        return (double) Math.log10(a);
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
        return (double) Math.pow(a, b);
    }
    
    public static double rand() {
        return (double) Math.random();
    }
    
    /**
     * Returns a double greater than or equal to 0.0 and less than a
     * @param a upper bound
     * @return 0.0 &#8804 x < a
     */
    public static double rand(double a) {
        return rand() * a;
    }
    
    public static double rand(double min, double max) {
        return (rand(max-min) + min);
    }
    
    public static double random() { return rand(); }
    public static double random(double a) { return rand(a); }
    public static double random(double min, double max) { return rand(min,max); }
    
    public static int randInt(int a) {
        return (int) Math.round(Math.random()*a);
    }
    
    public static long round(double a) {
        return Math.round(a);
    }
    
    public static double sin(double rad) {
        return (double) Math.sin(rad);
    }
    
    public static double sinh(double rad) {
        return (double) Math.sinh(rad);
    }
    
    public static double sqrt(double a) {
        return (double) Math.sqrt(a);
    }
    
    public static double invSqrt(double a) {
        return (double) Math.pow(a,-0.5f);
    }
    
    public static double tan(double rad) {
        return (double) Math.tan(rad);
    }
    
    public static double tanh(double rad) {
        return (double) Math.tanh(rad);
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
    
    public static double pattern(double x, double min ,double max) {
        return x%(max-min) + min;
    }
    
    public static double lerp(double alpha, double a, double b) {
        return a + alpha * (b - a);
    }
    
    public static double alpha(double value, double min, double max) {
        double n = value - min;
        double d = max - min;
        return n / d;
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
                return PI/2.0;
            } else {
                return 3*PI/2.0;
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
            return 2*PI - ref;
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
    
    public static boolean toBoolean(int i) {
        return i==0 ? false : true;
    }
    
    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }
    
    public static int toInt(double f) {
        return (int) f;
    }
    
    public static double toDouble(boolean b) {
        return b ? 1 : 0;
    }
    
    public static double toDouble(int i) {
        return (double) i;
    }
    
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
    
}
