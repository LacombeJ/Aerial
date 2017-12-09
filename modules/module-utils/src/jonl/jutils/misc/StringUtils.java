package jonl.jutils.misc;

public class StringUtils {

	public static String format(String targ, String string, Object... args) {
		String ret = string.replace(targ, "%s");
		return String.format(ret, args);
	}
	
	public static String format(String string, Object... args) {
		return format("%",string,args);
	}
	public static String f(String string, Object... args) {
		return format(string,args);
	}
	
    public static String padFront(String str, char pad, int n) {
        for (int i=0; i<n; i++) {
            str = pad+str;
        }
        return str;
    }
    
    public static String padBack(String str, char pad, int n) {
        for (int i=0; i<n; i++) {
            str = str+pad;
        }
        return str;
    }
    
    public static String padFrontMatch(String str, char pad, int length) {
        int n = length-str.length();
        for (int i=0; i<n; i++) {
            str = pad+str;
        }
        return str;
    }
    
    public static String padBackMatch(String str, char pad, int length) {
        int n = length-str.length();
        for (int i=0; i<n; i++) {
            str = str+pad;
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
    
    /**
     * <pre>
     * fit("abcdefg",0) = ""
     * fit("abcdefg",1) = "."
     * fit("abcdefg",2) = ".."
     * fit("abcdefg",3) = "a.."
     * fit("abcdefg",4) = "a..."
     * fit("abcdefg",5) = "ab..."
     * fit("abcdefg",6) = "abc..."
     * fit("abcdefg",7) = "abcdefg"
     * fit("abcdefg",8) = "abcdefg "
     * fit("abcdefg",9) = "abcdefg  "
     * </pre>
     * @return a string that has the length given by n
     */
    public static String fit(String str, int n) {
        if (n==str.length()) {
            return str;
        }
        if (n<str.length()) {
            if (n<=0) return "";
            if (n==1) return ".";
            if (n==2) return "..";
            if (n==3) return str.charAt(0)+".."; //TODO check case where str is empty? ""
            String front = str.substring(0, n-3);
            return padBackMatch(front,'.',n);
        }
        if (n>str.length()) {
            return padBackMatch(str,' ',n);
        }
        return str;
    }

    public static String spaces(int level, String string) {
        return padFront(string,' ',level);
    }
    
    public static String spaces(String string, int level) {
        return padBack(string,' ',level);
    }
    
}
