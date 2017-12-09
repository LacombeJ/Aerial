package jonl.jgl.lwjgl;

import jonl.jgl.AbstractFont;
import jonl.jgl.Texture;
import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;

class LWJGLFont extends AbstractFont {

    LWJGLTexture texture;
    
    LWJGLFont(LWJGL gl, String font, int type, int size, boolean antialias) {
        setBufferedImage(font,type,size,antialias);
        texture = (LWJGLTexture) gl.glGenTexture(getAndDestroyBufferedImage(),Internal.RGBA16,Wrap.CLAMP,Filter.LINEAR);
        
    }
    
    @Override
    public Texture getTexture() {
        return texture;
    }

}
