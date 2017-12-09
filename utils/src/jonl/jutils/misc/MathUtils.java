package jonl.jutils.misc;

public class MathUtils {

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
    
}
