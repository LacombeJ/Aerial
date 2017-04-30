package jonl.jutils.misc;

public class StringUtils {

    public static String padFront(String str, char pad, int length) {
        int n = length-str.length();
        for (int i=0; i<n; i++) {
            str = pad+str;
        }
        return str;
    }
    
    public static String padBack(String str, char pad, int length) {
        int n = length-str.length();
        for (int i=0; i<n; i++) {
            str+=pad;
        }
        return str;
    }
    
    public static String repeat(String str, int n) {
        StringBuilder sb = new StringBuilder(str.length()*n);
        for (int i=0; i<n; i++) {
           sb.append(str);
        }
        return sb.toString();
    }
    
}
