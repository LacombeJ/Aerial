package jonl.jgl.lwjgl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/*
From: https://www.opengl.org/wiki/Vertex_Specification_Best_Practices
Vertex, normals, texcoords
Should you create a separate VBO for each? Would you lose performance?
If your objects are static, then merge them all into as few VBOs as possible
for best performance. See above section for more details on layout considerations.
If only some of the vertex attributes are dynamic, i.e. often changing, placing
them in separate VBO makes updates easier and faster.
 */

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import jonl.jgl.Mesh;

/**
 * Class representing a textured mesh
 * 
 * @author Jonathan Lacombe
 *
 */
class LWJGLMesh implements Mesh {

    final static int FLOAT_SIZE = 4;

    // TODO have mesh types, STATIC and DYNAMIC
    
    final int id;
    
    private int vertexID;
    private int normalID;
    private int texCoordID;
    private int indicesID;
    
    private int verticesCount;
    private int indicesCount;
    
    /** Map (Location,BufferID) */
    private final HashMap<Integer,Integer> attribMap = new HashMap<>();
    
    LWJGLMesh(float[] vertexData, float[] normalData, float[] texCoordData, int[] indices) {
        id = GL30.glGenVertexArrays();
        
        GL30.glBindVertexArray(id);
        
        vertexID        = GL15.glGenBuffers();
        normalID        = (normalData==null || normalData.length==0)        ? -1 : GL15.glGenBuffers();
        texCoordID      = (texCoordData==null || texCoordData.length==0)    ? -1 : GL15.glGenBuffers();
        indicesID       = (indices==null || indices.length==0)              ? -1 : GL15.glGenBuffers();
        
        verticesCount   = vertexData.length;
        indicesCount    = (indices==null) ? 0 : indices.length;
        
        //Vertices
        {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vertexID);
            FloatBuffer fb = MemoryUtil.memAllocFloat(vertexData.length);
            fb.put(vertexData).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,fb,GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0,3,GL11.GL_FLOAT,false,0,0);
            MemoryUtil.memFree(fb);
        }
        
        if (normalID!=-1) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,normalID);
            FloatBuffer fb = MemoryUtil.memAllocFloat(normalData.length);
            fb.put(normalData).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,fb,GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1,3,GL11.GL_FLOAT,false,0,0);
            MemoryUtil.memFree(fb);
        }
        
        if (texCoordID!=-1) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,texCoordID);
            FloatBuffer fb = MemoryUtil.memAllocFloat(texCoordData.length);
            fb.put(texCoordData).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,fb,GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(2,2,GL11.GL_FLOAT,false,0,0);
            MemoryUtil.memFree(fb);
        }
        
        if (indicesID!=-1) {
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
            IntBuffer ib = MemoryUtil.memAllocInt(indices.length);
            ib.put(indices).flip();
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,ib,GL15.GL_STATIC_DRAW);
            MemoryUtil.memFree(ib);
        }
        
        GL30.glBindVertexArray(0);
    }
    
    LWJGLMesh(float[] vertexData, int[] indices) {
        this(vertexData,null,null,indices);
    }
    
    void preRender() {
        GL30.glBindVertexArray(id);
        
        GL20.glEnableVertexAttribArray(0);
        if (normalID!=-1)   GL20.glEnableVertexAttribArray(1);
        if (texCoordID!=-1) GL20.glEnableVertexAttribArray(2);
        for (int loc : attribMap.keySet()) {
            GL20.glEnableVertexAttribArray(loc);
        }
    }
    
    void postRender() {
        GL20.glDisableVertexAttribArray(0);
        if (normalID!=-1)   GL20.glDisableVertexAttribArray(1);
        if (texCoordID!=-1) GL20.glDisableVertexAttribArray(2);
        for (int loc : attribMap.keySet()) {
            GL20.glDisableVertexAttribArray(loc);
        }
        
        GL30.glBindVertexArray(0);
    }
    
    void render() {  
        render(GL11.GL_TRIANGLES);
    }
    
    void render(int mode) {
        preRender();
        if (indicesID != -1) {
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
            GL11.glDrawElements(mode,indicesCount,GL11.GL_UNSIGNED_INT,0);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,0);
        } else {
            GL11.glDrawArrays(mode, 0, verticesCount);
        }
        postRender();
    }
    
    void renderInstance(int count) {  
        renderInstance(GL11.GL_TRIANGLES, count);
    }
    
    void renderInstance(int mode, int count) {
        preRender();
        
        if (indicesID != -1) {
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
            GL31.glDrawElementsInstanced(mode, indicesCount, GL11.GL_UNSIGNED_INT, 0, count);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,0);
        } else {
            GL31.glDrawArraysInstanced(mode, 0, verticesCount, count);
        }
        
        postRender();
    }
    
    private void setAttrib(int id, int loc, float[] data, int size, int count) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,id);
        FloatBuffer fb = MemoryUtil.memAllocFloat(data.length);
        fb.put(data).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,fb,GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(fb);
        for (int i=0; i<count; i++) {
            int stride = (count<=1) ? 0 : FLOAT_SIZE*size*count;
            GL20.glVertexAttribPointer(loc+i,size,GL11.GL_FLOAT,false,stride,FLOAT_SIZE*size*i);
        }
    }
    
    private void setDivisor(int loc, int count) {
        for (int i=0; i<count; i++) {
            GL33.glVertexAttribDivisor(loc+i,1);
        }
    }
    
    @Override
    public void setVertexAttrib(float[] vertices, int size) {
        GL30.glBindVertexArray(id);
        setAttrib(vertexID,0,vertices,size,1);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void setNormalAttrib(float[] normals, int size) {
        GL30.glBindVertexArray(id);
        if (normalID==-1) {
            normalID = GL15.glGenBuffers();
        }
        setAttrib(normalID,1,normals,size,1);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void setTexCoordAttrib(float[] texCoord, int size) {
        GL30.glBindVertexArray(id);
        if (texCoordID==-1) {
            texCoordID = GL15.glGenBuffers();
        }
        setAttrib(texCoordID,2,texCoord,size,1);
        GL30.glBindVertexArray(0);
    }

    private int putAttribMap(int loc, int count) {
        Integer id = attribMap.get(loc);
        if (id==null) {
            attribMap.put(loc,id=GL15.glGenBuffers());
        }
        for (int i=1; i<count; i++) {
            attribMap.put(loc+i, id);
        }
        return id;
    }
    
    @Override
    public void setCustomAttrib(int loc, float[] data, int size) {
        setCustomAttrib(loc,data,size,1);
    }
    
    @Override
    public void setCustomAttrib(int loc, float[] data, int size, int count) {
        GL30.glBindVertexArray(id);
        int attrib = putAttribMap(loc,count);
        setAttrib(attrib,loc,data,size,count);
        GL30.glBindVertexArray(0);
    }
    
    @Override
    public void setCustomAttribInstanced(int loc, float[] data, int size) {
        setCustomAttribInstanced(loc,data,size,1);
    }
    
    @Override
    public void setCustomAttribInstanced(int loc, float[] data, int size, int count) {
        GL30.glBindVertexArray(id);
        int attrib = putAttribMap(loc,count);
        setAttrib(attrib,loc,data,size,count);
        setDivisor(loc,count);
        GL30.glBindVertexArray(0);
    }
    
    @Override
    public void setIndices(int[] indices) {
        indicesCount = indices.length;
        GL30.glBindVertexArray(id);
        if (indicesID==-1) {
            indicesID = GL15.glGenBuffers();
        }
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesID);
        IntBuffer ib = MemoryUtil.memAllocInt(indices.length);
        ib.put(indices).flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,ib,GL15.GL_STATIC_DRAW);
        //TODO GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,0); ? Will this fix errors?
        //TODO 3/29/18 try to uncomment above ?
        GL30.glBindVertexArray(0);
        MemoryUtil.memFree(ib);
    }

    
}
