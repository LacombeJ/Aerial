package jonl.aui.tea.graphics;

import jonl.jutils.misc.AwtFont;

public class TFont {

    public final static int PLAIN = jonl.jgl.Font.PLAIN;
    public final static int BOLD = jonl.jgl.Font.BOLD;
    public final static int ITALIC = jonl.jgl.Font.ITALIC;
    
    final String font;
    final int type;
    final int size;
    final boolean antialias;
    
    final AwtFont awtFont;
    
    public TFont(String font, int type, int size, boolean antialias) {
        this.font = font;
        this.type = type;
        this.size = size;
        this.antialias = antialias;
        awtFont = new AwtFont(font, toAwtFontType(type), size, antialias);
    }
    
    public AwtFont awt() {
        return awtFont;
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
    
    public int getHeight() {
        return awtFont.getHeight();
    }
    
    public int getWidth(String string) {
        return awtFont.getWidth(string);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TFont) {
            TFont f = (TFont) obj;
            return font.equals(f.font) && type==f.type
                    && size==f.size && antialias==f.antialias;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return font.hashCode() + type + size + (antialias ? 1 : 0);
    }
    
    public static int toAwtFontType(int type) {
        switch (type) {
        case TFont.PLAIN  : return java.awt.Font.PLAIN;
        case TFont.BOLD   : return java.awt.Font.BOLD;
        case TFont.ITALIC : return java.awt.Font.ITALIC;
        default: return java.awt.Font.PLAIN;
        }
    }
    
}
