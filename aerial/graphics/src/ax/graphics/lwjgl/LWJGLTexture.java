package ax.graphics.lwjgl;

import java.nio.FloatBuffer;

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
    
    private Filter filter;
    
    LWJGLTexture() {
        id = GL11.glGenTextures();
    }
    
    LWJGLTexture(FloatBuffer data, int width, int height,
            Internal internal, Wrap wrap, Filter filter) {
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
    
    @Override
    public void image(float[] data, int width, int height, Internal internal) {
        bind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,getInternal(internal),width,height,0,GL11.GL_RGBA,GL11.GL_FLOAT,data);
        this.width = width;
        this.height = height;
        if (filter==Filter.MIPMAP) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        free();
    }

    @Override
    public void image(FloatBuffer data, int width, int height, Internal internal) {
        bind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,getInternal(internal),width,height,0,GL11.GL_RGBA,GL11.GL_FLOAT,data);
        this.width = width;
        this.height = height;
        if (filter==Filter.MIPMAP) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        free();
    }

    @Override
    public void parameters(Wrap wrap, Filter filter) {
        int glWrap = getWrap(wrap);
        int glFilter = getFilter(filter);
        bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S,glWrap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T,glWrap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,glFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,glFilter);
        if (filter==Filter.MIPMAP) {
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

   
    private int getInternal(Internal internal) {
        int glInternal = 0;
        switch (internal) {
        case R16F:              glInternal = GL30.GL_R16F;              break;
        case R32F:              glInternal = GL30.GL_R32F;              break;
        case RGB16:             glInternal = GL11.GL_RGB16;             break;
        case RGB16F:            glInternal = GL30.GL_RGB16F;            break;
        case RGBA8:             glInternal = GL11.GL_RGBA8;             break;
        case RGBA16:            glInternal = GL11.GL_RGBA16;            break;
        case RGBA16F:           glInternal = GL30.GL_RGBA16F;           break;
        case DEPTH_COMPONENT:   glInternal = GL11.GL_DEPTH_COMPONENT;   break;
        }
        return glInternal;
    }
    
    private int getWrap(Wrap wrap) {
        int glWrap = 0;
        switch (wrap) {
        case CLAMP:     glWrap = GL12.GL_CLAMP_TO_EDGE; break;
        case REPEAT:    glWrap = GL11.GL_REPEAT;        break;
        }
        return glWrap;
    }
    
    private int getFilter(Filter filter) {
        int glFilter = 0;
        switch (filter) {
        case NEAREST:   glFilter = GL11.GL_NEAREST;                 break;
        case LINEAR:    glFilter = GL11.GL_LINEAR;                  break;
        case MIPMAP:    glFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;    break;
        }
        return glFilter;
    }
    
}
