package jonl.aui.tea;

import jonl.aui.Font;

public class TFont implements Font {

    jonl.jgl.Font font;
    String name;
    int size;
    
    TFont(String font, int size) {
        name = font;
        this.size = size;
    }
    
    @Override
    public float getWidth(char c) {
        return font.getWidth(c);
    }

    @Override
    public float getWidth(String str) {
        return font.getWidth(str);
    }

    @Override
    public float getHeight() {
        return font.getHeight();
    }

    
    
}
