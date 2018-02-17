package jonl.ge.core;

public final class Font {

    public final static int PLAIN = jonl.jgl.Font.PLAIN;
    public final static int BOLD = jonl.jgl.Font.BOLD;
    public final static int ITALIC = jonl.jgl.Font.ITALIC;
    
    final String font;
    final int type;
    final int size;
    final boolean antialias;
    
    public Font(String font, int type, int size, boolean antialias) {
        this.font = font;
        this.type = type;
        this.size = size;
        this.antialias = antialias;
    }
    
    public String font() {
    	return font;
    }
    
    public int type() {
    	return type;
    }
    
    public int size() {
    	return size;
    }
    
    public boolean antialias() {
    	return antialias;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Font) {
            Font f = (Font) obj;
            return font.equals(f.font) && type==f.type
                    && size==f.size && antialias==f.antialias;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return font.hashCode() + type + size + (antialias ? 1 : 0);
    }
    
}
