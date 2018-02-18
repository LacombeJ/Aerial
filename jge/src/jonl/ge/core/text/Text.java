package jonl.ge.core.text;

public class Text {
    
    public final static int LEFT = 0;
    public final static int CENTER = 1;
    public final static int RIGHT = 2;
    
    String text = "text";
    
    Font font = new Font("Consolas",Font.PLAIN,24,false); //TODO make null and handle default or null font some other way
    
    int align = LEFT;
    
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
    
    public int getAlign() {
        return align;
    }
    
    public void setAlign(int align) {
        this.align = align;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Text) {
            Text t = (Text) obj;
            return text.equals(t.text) && font.equals(t.font) && align==t.align;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return text.hashCode() + font.hashCode() + align;
    }
    
}
