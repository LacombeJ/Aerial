package jonl.ge.core;

import jonl.jgl.utils.FontUtils;
import jonl.jutils.misc.ArrayUtils;

public class Text extends Component {
    
    public static final int VA_TOP    = FontUtils.VA_TOP;
    public static final int VA_MIDDLE = FontUtils.VA_MIDDLE;
    public static final int VA_BOTTOM = FontUtils.VA_BOTTOM;
    
    public static final int HA_LEFT   = FontUtils.HA_LEFT;
    public static final int HA_CENTER = FontUtils.HA_CENTER;
    public static final int HA_RIGHT  = FontUtils.HA_RIGHT;
    
    String text = "text";
    
    Font font = AppUtil.FONT_CONSOLAS;
    
    float[] color = { 1, 1, 1, 1 };
    
    int valign = VA_MIDDLE;
    int halign = HA_CENTER;
    
    public Text(String text) {
       this.text = text;
    }
    
    public Text() {
        
    }

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
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Text) {
            Text t = (Text) obj;
            return text.equals(t.text) && font.equals(t.font)
                    && valign==t.valign && halign==t.halign;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return text.hashCode() + font.hashCode() + valign + halign;
    }
    
    static Text copy(Text t) {
        Text text = new Text();
        text.text = t.text;
        text.font = t.font;
        text.halign = t.halign;
        text.valign = t.valign;
        return text;
    }
    
}
