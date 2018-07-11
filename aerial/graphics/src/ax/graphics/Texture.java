package ax.graphics;

import java.nio.FloatBuffer;

/**
 * @author Jonathan Lacombe
 */
public interface Texture {
    
    public int getWidth();
    public int getHeight();
    
    /** glTexImage2D - generates the texture image and sets width and height */
    public void image(float[] data, int width, int height, Internal internal);
    
    /** glTexImage2D - generates the texture image and sets width and height*/
    public void image(FloatBuffer data, int width, int height, Internal internal);
    
    /** glTexParametersi - sets the texture parameters */
    public void parameters(Wrap wrap, Filter filter);
    
    /** glTexSubImage2D */
    public void subImage(float[] data, int x, int y, int width, int height);
    /** glTexSubImage2D */
    public void subImage(FloatBuffer data, int x, int y, int width, int height);
    
    public void delete();
    
    public static enum Internal {
        R16F,
        R32F,
        
        RGB16,
        RGB16F,
        
        RGBA8,
        RGBA16,
        RGBA16F,

        DEPTH_COMPONENT
    }
    
    public static enum Wrap {
        CLAMP,
        REPEAT
    }
    
    public static enum Filter {
        NEAREST,
        LINEAR,
        MIPMAP
    }
    
}
