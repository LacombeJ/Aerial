package jonl.jgl;

import java.awt.GraphicsEnvironment;

public interface Font {

    public static final int PLAIN = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    
    public static final String[] AVAILABLE_FONTS = 
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
    
    public Texture getTexture();
    
    public float[] getIndices(char c);
    
    public float getWidth(char c);
    
    public float getWidth(String str);
    
    public float getHeight();
    
    Glyph getGlyph(char c);
    
    static class Glyph {
        final char c;
        final float x;
        final float width;
        Glyph(char c, float x, float width){
            this.c = c;
            this.x = x;
            this.width = width;
        }
    }
    
    public static String[] getAvailableFonts() {
        String[] fonts = new String[AVAILABLE_FONTS.length];
        for (int i=0; i<fonts.length; i++) {
            fonts[i] = AVAILABLE_FONTS[i];
        }
        return fonts;
    }
    
    public static String getFirstFont() {
        return AVAILABLE_FONTS[0];
    }
    
}
