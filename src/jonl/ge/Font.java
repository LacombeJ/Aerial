package jonl.ge;

public class Font {

    public final static int PLAIN = jonl.jgl.Font.PLAIN;
    public final static int BOLD = jonl.jgl.Font.BOLD;
    public final static int ITALIC = jonl.jgl.Font.ITALIC;
    
    String font;
    int type;
    int size;
    boolean antialias;
    
    public Font(String font, int type, int size, boolean antialias) {
        this.font = font;
        this.type = type;
        this.size = size;
        this.antialias = antialias;
    }
    
}
