package jonl.aui.sui;

import jonl.aui.Font;

public class SFont implements Font {

    jonl.jgl.Font font;
    String name;
    int size;
    
    SFont(String font, int size) {
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
