package ax.engine.core;

import ax.commons.misc.ArrayUtils;
import ax.graphics.GL;

public class Texture {
    
    float data[];
    
    public final int width;
    public final int height;
    GL.Internal format;
    GL.Wrap wrap;
    GL.Filter filter;
    
    public Texture(float[] data, int width, int height, GL.Internal format, GL.Wrap wrap, GL.Filter filter) {
        this.data = ArrayUtils.copy(data);
        this.width = width;
        this.height = height;
        this.format = format;
        this.wrap = wrap;
        this.filter = filter;
    }
    
    public Texture(int width, int height, GL.Internal format, GL.Wrap wrap, GL.Filter filter) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.wrap = wrap;
        this.filter = filter;
    }
    
    public Texture(float[] data, int width, int height) {
        this(data,width,height,GL.RGBA16,GL.CLAMP,GL.MIPMAP);
    }
    
    public Texture(int width, int height) {
        this(width,height,GL.RGBA16,GL.CLAMP,GL.MIPMAP);
    }

    public int width() {
        return width;
    }
    public int height() {
        return height;
    }
    
}
