package jonl.aui.tea.graphics;

public class TText {
    
    public final static int LEFT = 0;
    public final static int CENTER = 1;
    public final static int RIGHT = 2;
    
    String text = "text";
    
    //TFont font = new TFont("Consolas",TFont.PLAIN,24,false); //TODO make null and handle default or null font some other way
    TFont font = new TFont("Calibri",TFont.PLAIN,16,false); //TODO make null and handle default or null font some other way
    //
    
    
    int align = LEFT;
    
    public TText(String text) {
       this.text = text;
    }
    
    public TText() {
        
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public TFont getFont() {
        return font;
    }
    
    public void setFont(TFont font) {
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
        if (obj instanceof TText) {
            TText t = (TText) obj;
            return text.equals(t.text) && font.equals(t.font) && align==t.align;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return text.hashCode() + font.hashCode() + align;
    }
    
}
