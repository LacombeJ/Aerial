package jonl.ge;

import jonl.jutils.misc.ArrayUtils;

public class Texture {
    
    public int width;
    public int height;
    public Internal format;
    public Wrap wrap;
    public Filter filter;
    
    float data[];
    
    public Texture(float[] data, int width, int height, Internal format, Wrap wrap, Filter filter) {
        this.data = ArrayUtils.copy(data);
        this.width = width;
        this.height = height;
        this.format = format;
        this.wrap = wrap;
        this.filter = filter;
    }
    
    public Texture(int width, int height, Internal format, Wrap wrap, Filter filter) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.wrap = wrap;
        this.filter = filter;
    }
    
    public Texture(float[] data, int width, int height) {
        this(data,width,height,Internal.RGBA16,Wrap.CLAMP,Filter.MIPMAP);
    }
    
    public Texture(int width, int height) {
        this(width,height,Internal.RGBA16,Wrap.CLAMP,Filter.MIPMAP);
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    
    
    
    public static enum Internal {
        R16F    (jonl.jgl.Texture.Internal.R16F),
        R32F    (jonl.jgl.Texture.Internal.R32F),
        RGB16   (jonl.jgl.Texture.Internal.RGB16),
        RGB16F  (jonl.jgl.Texture.Internal.RGB16F),
        RGBA8   (jonl.jgl.Texture.Internal.RGBA8),
        RGBA16  (jonl.jgl.Texture.Internal.RGBA16),
        RGBA16F (jonl.jgl.Texture.Internal.RGBA16F);
        jonl.jgl.Texture.Internal format;
        Internal(jonl.jgl.Texture.Internal format) {
            this.format = format;
        }
    }
    
    public static enum Wrap {
        CLAMP   (jonl.jgl.Texture.Wrap.CLAMP),
        REPEAT  (jonl.jgl.Texture.Wrap.REPEAT);
        jonl.jgl.Texture.Wrap wrap;
        Wrap(jonl.jgl.Texture.Wrap wrap) {
            this.wrap = wrap;
        }
    }
    
    public static enum Filter {
        NEAREST (jonl.jgl.Texture.Filter.NEAREST),
        LINEAR  (jonl.jgl.Texture.Filter.LINEAR),
        MIPMAP  (jonl.jgl.Texture.Filter.MIPMAP);
        jonl.jgl.Texture.Filter filter;
        Filter(jonl.jgl.Texture.Filter filter) {
            this.filter = filter;
        }
    }
    
}
