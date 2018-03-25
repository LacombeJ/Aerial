package jonl.vmath;

/**
 * For calculations using long values <p>
 * 
 * @author Jonathan Lacombe
 */
public class Mathl {

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
    
    public static long max(long a, long b) {
        return (a>b) ? a : b;
    }
    
    public static long max(long... a) {
        long max = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]>max) max = a[i];
        }
        return max;
    }

    public static long min(long a, long b) {
        return (a<b) ? a : b;
    }
    
    public static long min(long... a) {
        long min = a[0];
        for (int i=1; i<a.length; i++) {
            if (a[i]<min) min = a[i];
        }
        return min;
    }
    
    public static long clamp(long x, long min, long max) {
        return x < min ? min : (x > max ? max : x);
    }
    
}
