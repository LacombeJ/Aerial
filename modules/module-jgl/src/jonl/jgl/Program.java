package jonl.jgl;

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
    
    
    public void setUniformMat4(String name, float[] mat);
    
    public void setUniformMat3(String name, float[] mat);
    
    public void setUniformMat2(String name, float[] mat);
    
    
    public void setTexture(String name, Texture texture, int order);
    
}
