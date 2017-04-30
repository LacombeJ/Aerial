package jonl.jgl.lwjgl;

import java.util.HashMap;


/*
From: https://www.opengl.org/wiki/Vertex_Specification_Best_Practices
Vertex, normals, texcoords
Should you create a separate VBO for each? Would you lose performance?
If your objects are static, then merge them all into as few VBOs as possible
for best performance. See above section for more details on layout considerations.
If only some of the vertex attributes are dynamic, i.e. often changing, placing
them in separate VBO makes updates easier and faster.
 */

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import jonl.jgl.Mesh;
import jonl.jutils.misc.BufferPool;

/**
 * Class representing a textured mesh
 * 
 * @author Jonathan Lacombe
 *
 */
class LWJGLMesh implements Mesh {

    final int id;
    
    private int vertexID;
    private int normalID;
    private int texCoordID;
    private int indicesID;
    
    private int indicesCount;
    
    /** Map (Location,BufferID) */
    private final HashMap<Integer,Integer> attribMap = new HashMap<>();
    
    LWJGLMesh(float[] vertexData, float[] normalData, float[] texCoordData, int[] indices) {
        id = GL30.glGenVertexArrays();
        
        GL30.glBindVertexArray(id);
        
        vertexID        = GL15.glGenBuffers();
        normalID        = (normalData!=null)    ? GL15.glGenBuffers() : -1;
        texCoordID      = (texCoordData!=null)  ? GL15.glGenBuffers() : -1;
        indicesID       = GL15.glGenBuffers();
        indicesCount    = indices.length;
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vertexID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,BufferPool.getFloatBuffer(vertexData,true),GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0,3,GL11.GL_FLOAT,false,0,0);
        
        if (normalID!=-1) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,normalID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,BufferPool.getFloatBuffer(normalData,true),GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1,3,GL11.GL_FLOAT,false,0,0);
        }
        
        if (texCoordID!=-1) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,texCoordID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,BufferPool.getFloatBuffer(texCoordData,true),GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(2,2,GL11.GL_FLOAT,false,0,0);
        }
        
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,BufferPool.getIntBuffer(indices,true),GL15.GL_STATIC_DRAW);
        
        GL30.glBindVertexArray(0);
    }
    
    LWJGLMesh(float[] vertexData, int[] indices) {
        this(vertexData,null,null,indices);
    }
    
    void render() {  
        render(GL11.GL_TRIANGLES);
    }
    
    void render(int mode) {
        GL30.glBindVertexArray(id);
        
        GL20.glEnableVertexAttribArray(0);
        if (normalID!=-1)   GL20.glEnableVertexAttribArray(1);
        if (texCoordID!=-1) GL20.glEnableVertexAttribArray(2);
        for (int loc : attribMap.keySet()) {
            GL20.glEnableVertexAttribArray(loc);
        }
        
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
        GL11.glDrawElements(mode,indicesCount,GL11.GL_UNSIGNED_INT,0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,0);
        
        GL20.glDisableVertexAttribArray(0);
        if (normalID!=-1)   GL20.glDisableVertexAttribArray(1);
        if (texCoordID!=-1) GL20.glDisableVertexAttribArray(2);
        for (int loc : attribMap.keySet()) {
            GL20.glDisableVertexAttribArray(loc);
        }
        
        GL30.glBindVertexArray(0);
    }
    
    private void setAttrib(int id, int loc, float[] data, int size) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,BufferPool.getFloatBuffer(data,true),GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(loc,size,GL11.GL_FLOAT,false,0,0);
    }

    @Override
    public void setVertexAttrib(float[] vertices, int size) {
        GL30.glBindVertexArray(id);
        setAttrib(vertexID,0,vertices,size);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void setNormalAttrib(float[] normals, int size) {
        GL30.glBindVertexArray(id);
        if (normalID==-1) {
            normalID = GL15.glGenBuffers();
        }
        setAttrib(normalID,1,normals,size);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void setTexCoordAttrib(float[] texCoord, int size) {
        GL30.glBindVertexArray(id);
        if (texCoordID==-1) {
            texCoordID = GL15.glGenBuffers();
        }
        setAttrib(texCoordID,2,texCoord,size);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void setCustomAttrib(int loc, float[] data, int size) {
        GL30.glBindVertexArray(id);
        Integer id = attribMap.get(loc);
        if (id==null) {
            attribMap.put(loc,id=GL15.glGenBuffers());
        }
        setAttrib(id,loc,data,size);
        GL30.glBindVertexArray(0);
    }
    
    @Override
    public void setIndices(int[] indices) {
        indicesCount = indices.length;
        GL30.glBindVertexArray(id);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,BufferPool.getIntBuffer(indices,true),GL15.GL_STATIC_DRAW);
        GL30.glBindVertexArray(0);
    }

    
}
