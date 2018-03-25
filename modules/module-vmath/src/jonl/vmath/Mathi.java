package jonl.vmath;

import java.util.Arrays;

public class Mathi {

    public static int abs(int a) {
        return Math.abs(a);
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
    

    public static int randInt(int a) {
        return (int) Math.round(Math.random()*a);
    }
    
    public static int randInt(int min, int max) {
        return randInt(max-min) + min;
    }

    public static int clamp(int x, int min, int max) {
        return x < min ? min : (x > max ? max : x);
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

    public static boolean toBoolean(int i) {
        return i==0 ? false : true;
    }

    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }

    public static int toInt(float f) {
        return (int) f;
    }
    
    public static float toFloat(int i) {
        return (float) i;
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
    
    
    
    
}
