package jonl.jgl.utils;

import jonl.jgl.Font;
import jonl.jutils.func.Tuple4f;

public class FontUtils {

    public static final int VA_TOP    = 0;
    public static final int VA_MIDDLE = 1;
    public static final int VA_BOTTOM = 2;
    
    public static final int HA_LEFT   = 0;
    public static final int HA_CENTER = 1;
    public static final int HA_RIGHT  = 2;
    
    public Tuple4f[] getCharPositions(Font font, String string, int halign, int valign) {
        
        String[] str = string.split("\n");
        
        int len = 0;
        for (String s : str) {
            len += s.length();
        }
        Tuple4f[] cpos = new Tuple4f[len];
        
        
        float totalHeight = font.getHeight()*str.length;
        
        float sdy = 0;
        
        if (valign==VA_BOTTOM) {
            sdy = totalHeight - font.getHeight();
        } else if (valign==VA_MIDDLE) {
            sdy = (totalHeight)/2 - font.getHeight();
        } else {
            sdy -= font.getHeight();
        }
        
        int k = 0;
        for (int i=0; i<str.length; i++) {
            String line = str[i];
            float sdx = 0;
            if (halign==HA_CENTER) {
                sdx = -font.getWidth(line)/2;
            } else if (halign==HA_RIGHT) {
                sdx = -font.getWidth(line);
            }
            for (int j=0; j<line.length(); j++) {
                char c = line.charAt(j);
                
                //Parameters x and y must be given as ints or else
                //characters would show unwanted artifacts;
                //unknown reason found in AppRenderer in GE
                int x = (int) sdx;
                int y = (int) sdy;
                float width = font.getWidth(c);
                float height = font.getHeight();
                
                cpos[k++] = new Tuple4f(x,y,width,height);
                
                sdx += font.getWidth(c);
            }
            sdy -= font.getHeight();
        }
        
        return cpos;
    }
    
}
