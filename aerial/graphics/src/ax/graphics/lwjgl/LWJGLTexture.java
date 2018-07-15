package ax.graphics.lwjgl;

import java.nio.FloatBuffer;

import ax.graphics.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import ax.graphics.Texture;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
class LWJGLTexture implements Texture {
    
    private final int id;
    
    private int width;
    private int height;
    
    private GL.Filter filter;
    
    LWJGLTexture() {
        id = GL11.glGenTextures();
    }
    
    LWJGLTexture(FloatBuffer data, int width, int height,
            GL.Internal internal, GL.Wrap wrap, GL.Filter filter) {
        this();
        parameters(wrap,filter);
        image(data,width,height,internal);
    }
    
    int getID() {
        return id;
    }
    
    void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,id);
    }
    
    void free() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }

    private void setSizeAndMipmap(int width, int height) {
        this.width = width;
        this.height = height;
        if (filter==GL.Filter.MIPMAP) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
    }

    private int getFormatFromInternalFormat(GL.Internal internal) {
        if (internal==GL.DEPTH_COMPONENT) {
            return GL11.GL_DEPTH_COMPONENT;
        }
        return GL11.GL_RGBA;
    }

    @Override
    public void image(float[] data, int width, int height, GL.Internal internal) {
        bind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,LWJGL.getInternal(internal),
                width,height,0,getFormatFromInternalFormat(internal),GL11.GL_FLOAT,data);
        setSizeAndMipmap(width, height);
        free();
    }

    @Override
    public void image(FloatBuffer data, int width, int height, GL.Internal internal) {
        bind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,LWJGL.getInternal(internal),
                width,height,0,getFormatFromInternalFormat(internal),GL11.GL_FLOAT,data);
        setSizeAndMipmap(width, height);
        free();
    }

    @Override
    public void parameters(GL.Wrap wrap, GL.Filter filter) {
        int glWrap = LWJGL.getWrap(wrap);
        int glFilter = LWJGL.getFilter(filter);
        bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S,glWrap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T,glWrap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,glFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,glFilter);
        if (filter==GL.MIPMAP) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        this.filter = filter;
        free();
    }
    
    @Override
    public void subImage(float[] data, int x, int y, int width, int height) {
        bind();
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D,0,x,y,width,height,GL11.GL_RGBA,GL11.GL_FLOAT,data);
        free();
    }
    
    @Override
    public void subImage(FloatBuffer data, int x, int y, int width, int height) {
        bind();
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D,0,x,y,width,height,GL11.GL_RGBA,GL11.GL_FLOAT,data);
        free();
    }
    
    @Override
    public void delete() {
        GL11.glDeleteTextures(id);
    }
    
    @Override
    public int hashCode() {
        return width*height + width*id + height;
    }
    
}
