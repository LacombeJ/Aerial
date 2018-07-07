package ax.graphics;

/**
 * @author Jonathan Lacombe
 */
public interface FrameBuffer {
    
    public int getWidth();
    public int getHeight();
    
    public void attach(Texture texture);
    
    public void link();
    
    public Texture getTexture(int i);
    public int getTextureCount();
    
    /** Copies the default framebuffer (screen buffer) */
    public void copyDefault();
    
    /**
     * Deletes the frame buffer but returns the associated textures
     * @return the textures associated with this frame buffer
     */
    public Texture[] delete();
    
    /**
     * Deletes the frame buffer and associated textures
     */
    public void deleteAll();
    
}
