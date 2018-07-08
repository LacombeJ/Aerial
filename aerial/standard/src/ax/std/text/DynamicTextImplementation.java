package ax.std.text;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import ax.commons.misc.AwtFont;
import ax.graphics.GL;
import ax.graphics.Texture;
import ax.graphics.Texture.Filter;
import ax.graphics.Texture.Internal;
import ax.graphics.Texture.Wrap;

/**
 * This implementation of text rendering will keep a map of text to textures
 * created with a frame buffer
 * 
 * @author Jonathan L
 *
 */
class DynamicTextImplementation implements TextImplementation {

    GL gl;
    
    HashMap<Font, AwtFont> fontMap;
    HashMap<Text, Texture> textMap;
    
    public DynamicTextImplementation() {
        fontMap = new HashMap<>();
        textMap = new HashMap<>();
    }
    
    @Override
    public Texture get(Text text) {
        return getOrCreateTexture(text);
    }

    @Override
    public void load(GL gl) {
        this.gl = gl;
    }
    
    AwtFont getOrCreateFont(Font font) {
        AwtFont textFont = fontMap.get(font);
        if (textFont == null) {
            textFont = new AwtFont(
                    font.font(),
                    TextUtils.toAwtFontType(font.type()),
                    font.size(),
                    font.antialias());
            fontMap.put(font, textFont);
        }
        return textFont;
    }
    
    Texture getOrCreateTexture(Text text) {
        Texture texture = textMap.get(text);
        if (texture == null) {
            AwtFont font = getOrCreateFont(text.getFont());
            texture = genTexture(font, text);
            textMap.put(text, texture);
        }
        return texture;
    }
    
    Texture genTexture(AwtFont font, Text text) {
        Filter filter = (text.getFont().antialias()) ? Filter.LINEAR : Filter.NEAREST;
        BufferedImage image = font.genBufferedImage(text.getText(), TextUtils.toAwtAlign(text.getAlign()));
        return gl.glGenTexture(image, Internal.RGBA16, Wrap.CLAMP, filter);
    }

}
