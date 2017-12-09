package jonl.jgl;

import java.nio.FloatBuffer;

/**
 * @author Jonathan Lacombe
 */
public interface Program {
    
    public void attach(Shader shader);
    
    public void link();

    public void setAttribute(String name, float v0);
    public void setAttribute(String name, float v0, float v1);
    public void setAttribute(String name, float v0, float v1, float v2);
    public void setAttribute(String name, float v0, float v1, float v2, float v3);
    
    public void setUniform(String name, float v0);
    public void setUniform(String name, float v0, float v1);
    public void setUniform(String name, float v0, float v1, float v2);
    public void setUniform(String name, float v0, float v1, float v2, float v3);
    
    public void setUniformi(String name, int v0);
    
    
    /**
     * @param name uniform name label
     * @param fb buffer with at least 16 elements
     */
    public void setUniformMat4(String name, FloatBuffer fb);
    
    public void setUniformMat3(String name, FloatBuffer fb);
    
    public void setUniformMat2(String name, FloatBuffer fb);
    
    /** @see #setUniformMat4(String, FloatBuffer) */
    public void setUniformMat4(String name, float[] mat);
    
    
    public void setTexture(String name, Texture texture, int order);
    
}
