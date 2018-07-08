package ax.graphics;

/**
 * @author Jonathan Lacombe
 */
public interface Mesh {
    
    public void setVertexAttrib(float[] vertices, int size);
    
    public void setNormalAttrib(float[] normals, int size);
    
    public void setTexCoordAttrib(float[] texCoord, int size);
    
    public void setCustomAttrib(int loc, float[] data, int size);
    public void setCustomAttrib(int loc, float[] data, int size, int count);
    
    public void setCustomAttribInstanced(int loc, float[] data, int size);
    public void setCustomAttribInstanced(int loc, float[] data, int size, int count);
    
    public void setIndices(int[] indices);
    
}
