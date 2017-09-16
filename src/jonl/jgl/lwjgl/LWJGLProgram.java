package jonl.jgl.lwjgl;

/*
 * Some notes about GLSL (don't know where else to put this):
 * 
 * For the following example (found through experimentation):
 *   Vertex shader:
 *     gl_Position = MVP * vec4(vertex,1); //glsl built-in
 *     outPosition = MVP * vec4(vertex,1); //personal
 *   Fragment shader:
 *     vec4 u = gl_FragCoord;
 *     vec4 v = outPosition;
 *   Vector uT and vT are equivalent for vertex inside frustrum:
 *     vec4 uT = vec4( u.x/width, u.y/height, u.z, u.w );
 *     vec4 vT = (v/v.w + 1)/2.0;
 *     vT = vec4(vT.x,vT.y,vT.z,1-vT.z);
 *   where width and height are the dimension of screen(or buffer).
 *   Vector uT and vT also gives values clamped from 0 to 1
 *   
 * So we can define the methods (not tested):
 *   Vector4 gl_FragCoord(Matrix4 MVP, Vector3 vertex, width, height) {
 *      Vector4 v = MVP * Vector4(vertex,1);
 *      v = ( v / v.w + Vector4(1,1,1,1) ) / 2.0;
 *      v.x *= width;
 *      v.y *= height;
 *      v.w = 1 - v.z;
 *      return v;
 *   }
 *   Vector4 toScreenSpace(Matrix4 MVP, Vector3 vertex) {
 *      Vector4 v = MVP * Vector4(vertex,1);
 *      v = ( v / v.w + Vector4(1,1,1,1) ) / 2.0;
 *      v.w = 1 - v.z;
 *      return v;
 *   }
 * 
 */

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import jonl.jgl.AbstractProgram;
import jonl.jgl.Shader;
import jonl.jgl.Texture;
import jonl.jutils.misc.StringUtils;


/**
 * 
 * 
 * @author Jonathan Lacombe
 *
 */
class LWJGLProgram extends AbstractProgram {
    
    //TODO
    //Remove setting attributes and uniforms to GraphicsLibrary

    private final int id;
    
    LWJGLProgram() {
        id = GL20.glCreateProgram();
    }
    
    int getID() {
        return id;
    }
    
    @Override
    public void setAttribute(String name, float v0) {
        int loc = GL20.glGetAttribLocation(id,name);
        GL20.glVertexAttrib1f(loc,v0);
    }
    
    @Override
    public void setAttribute(String name, float v0, float v1) {
        int loc = GL20.glGetAttribLocation(id,name);
        GL20.glVertexAttrib2f(loc,v0,v1);
    }
    
    @Override
    public void setAttribute(String name, float v0, float v1, float v2) {
        int loc = GL20.glGetAttribLocation(id,name);
        GL20.glVertexAttrib3f(loc,v0,v1,v2);
    }
    
    @Override
    public void setAttribute(String name, float v0, float v1, float v2, float v3) {
        int loc = GL20.glGetAttribLocation(id,name);
        GL20.glVertexAttrib4f(loc,v0,v1,v2,v3);
    }
    
    
    
    @Override
    public void setUniform(String name, float v0) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniform1f(loc,v0);
    }
    
    @Override
    public void setUniform(String name, float v0, float v1) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniform2f(loc,v0,v1);
    }
    
    @Override
    public void setUniform(String name, float v0, float v1, float v2) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniform3f(loc,v0,v1,v2);
    }
    
    @Override
    public void setUniform(String name, float v0, float v1, float v2, float v3) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniform4f(loc,v0,v1,v2,v3);
    }
    
    
    @Override
    public void setUniformi(String name, int v0) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniform1i(loc,v0);
    }
    
    
    
    @Override
    public void setUniformMat4(String name, FloatBuffer fb) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniformMatrix4fv(loc,true,fb);
    }
    
    @Override
    public void setUniformMat3(String name, FloatBuffer fb) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniformMatrix3fv(loc,true,fb);
    }
    
    @Override
    public void setUniformMat2(String name, FloatBuffer fb) {
        int loc = GL20.glGetUniformLocation(id,name);
        GL20.glUniformMatrix2fv(loc,true,fb);
    }
    
    @Override
    public void setTexture(String name, Texture texture, int order) {
        GL20.glUniform1i( GL20.glGetUniformLocation(id,name) , order );
        GL13.glActiveTexture( GL13.GL_TEXTURE0 + order );
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((LWJGLTexture)texture).getID());
    }

    @Override
    public void attach(Shader shader) {
        GL20.glAttachShader(id,((LWJGLShader)shader).getID());
    }

    @Override
    public void link() {
        GL20.glLinkProgram(id);
        
        if (GL20.glGetProgrami(id,GL20.GL_LINK_STATUS)==GL11.GL_FALSE) {
            System.err.println("Program "+id+" failed to link");
            
            String log = GL20.glGetProgramInfoLog(id);
            System.err.println(log);
        }
    }
    
}
