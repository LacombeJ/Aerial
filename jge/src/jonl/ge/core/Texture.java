package jonl.ge.core;

import jonl.jutils.misc.ArrayUtils;

public class Texture {
    
    float data[];
    
    public final int width;
    public final int height;
    Internal format;
    Wrap wrap;
    Filter filter;
    
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

    public int width() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    
    
    
    public static enum Internal {
        R16F,
        R32F,
        RGB16,
        RGB16F,
        RGBA8,
        RGBA16,
        RGBA16F;
    }
    
    public static enum Wrap {
        CLAMP,
        REPEAT;
    }
    
    public static enum Filter {
        NEAREST,
        LINEAR,
        MIPMAP;
    }
    
}
