package jonl.ge;

import jonl.jutils.misc.ArrayUtils;

public class Text extends Component {
    
    public static final int VA_TOP    = 0;
    public static final int VA_MIDDLE = 1;
    public static final int VA_BOTTOM = 2;
    
    public static final int HA_LEFT   = 0;
    public static final int HA_CENTER = 1;
    public static final int HA_RIGHT  = 2;
    
    String text = "text";
    
    Font font = AppUtil.fontConsolas();
    
    float[] color = { 1, 1, 1, 1 };
    
    int valign = VA_MIDDLE;
    int halign = HA_CENTER;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Font getFont() {
        return font;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public int getHAlign() {
        return halign;
    }
    
    public void setHAlign(int halign) {
        this.halign = halign;
    }
    
    public int getVAlign() {
        return valign;
    }
    
    public void setVAlign(int valign) {
        this.valign = valign;
    }
    
    public float[] getColor() {
        return ArrayUtils.copy(color);
    }
    
    public void setColor(float[] color) {
        this.color = ArrayUtils.copy(color);
    }
    
}
