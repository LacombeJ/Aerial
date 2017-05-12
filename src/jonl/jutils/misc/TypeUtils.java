package jonl.jutils.misc;

import java.util.Comparator;

import jonl.jutils.func.Function2D;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class TypeUtils {
    
    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }
    
    public static float toFloat(boolean b) {
        return b ? 1 : 0;
    }
    
    public static int toBool(int i) {
        return (i==0) ? 0 : 1;
    }
    

    public static byte[] intToBytes(int val) {
        return new byte[] {
            (byte) ((val >> 24) & 255),
            (byte) ((val >> 16) & 255),
            (byte) ((val >> 8)  & 255),
            (byte) ((val)       & 255)
        };
    }
    
    public static int bytesToInt(byte a, byte r, byte g, byte b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    public static int compare(float x, float y) {
    	return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
    
    public static int compare(int x, int y) {
    	return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
    
}
