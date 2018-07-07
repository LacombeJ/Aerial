package ax.commons.misc;

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
    
    /** @return object casted if it is of instance or null if not */
    @SuppressWarnings("unchecked")
	public static <T> T cast(Object o, Class<T> c) {
    	if (c.isInstance(o)) {
    		return (T) o;
    	}
    	return null;
    }
    
}
