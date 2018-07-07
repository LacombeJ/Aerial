package jonl.jgl.lwjgl;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import jonl.jgl.FrameBuffer;
import jonl.jgl.Texture;

class LWJGLFrameBuffer implements FrameBuffer {
    
    private final ArrayList<LWJGLTexture> textures = new ArrayList<>();
    
    private final int id;
    private final int rBufferID;
    private final int width;
    private final int height;
    
    LWJGLFrameBuffer(int width, int height) {
        id = GL30.glGenFramebuffers();
        rBufferID = GL30.glGenRenderbuffers();
        this.width = width;
        this.height = height;
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,id);
        
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER,rBufferID);
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER,GL30.GL_DEPTH_COMPONENT32F,width,height);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER,GL30.GL_DEPTH_ATTACHMENT,GL30.GL_RENDERBUFFER,rBufferID);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER,0);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
    }
    
    LWJGLFrameBuffer(Texture... textures) {
        this(textures[0].getWidth(),textures[0].getHeight());
        for (Texture t : textures) {
            attach(t);
        }
        link();
    }
    
    int getID() {
        return id;
    }
    
    @Override
    public void attach(Texture texture) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,id);
        
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + textures.size(),
                GL11.GL_TEXTURE_2D,((LWJGLTexture)texture).getID(),0);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        
        textures.add((LWJGLTexture)texture);
    }
    
    @Override
    public void link() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,id);
        
        int[] attachments = new int[textures.size()];
        for (int i=0; i<attachments.length; i++) {
            attachments[i] = GL30.GL_COLOR_ATTACHMENT0 + i;
        }
        GL20.glDrawBuffers(attachments);
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
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
    public Texture getTexture(int i) {
        return textures.get(i);
    }
    
    @Override
    public int getTextureCount() {
        return textures.size();
    }
    
    @Override
    public void copyDefault() {
        blit(0);
    }
    
    void blit(int fbID) {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER,fbID);  
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,id);  
        GL30.glBlitFramebuffer(0,0,width,height,0,0,
                width,height,GL11.GL_COLOR_BUFFER_BIT,GL11.GL_NEAREST); 
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER,0);       
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER,0);
    }
    
    @Override
    public Texture[] delete() {
        GL30.glDeleteRenderbuffers(rBufferID);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        GL30.glDeleteFramebuffers(id);
        return textures.toArray(new Texture[0]);
    }

    @Override
    public void deleteAll() {
        Texture[] textures = delete();
        for (Texture texture : textures) {
            texture.delete();
        }
    }

}
