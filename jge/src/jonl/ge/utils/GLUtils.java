package jonl.ge.utils;

import java.nio.FloatBuffer;

import jonl.ge.core.Mesh;
import jonl.ge.core.Texture;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;
import jonl.jgl.Shader;
import jonl.jgl.GraphicsLibrary.ShaderType;
import jonl.jutils.io.FileUtils;
import jonl.jutils.misc.BufferPool;
import jonl.vmath.Matrix2;
import jonl.vmath.Matrix3;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class GLUtils {

    public static Program createProgramFromSource(GraphicsLibrary gl, String vertSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(ShaderType.VERTEX_SHADER,vertSource);
        Shader fragShader = gl.glCreateShader(ShaderType.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    public static Program createProgramFromSource(GraphicsLibrary gl, String vertSource, String geomSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(ShaderType.VERTEX_SHADER,vertSource);
        Shader geomShader = gl.glCreateShader(ShaderType.GEOMETRY_SHADER,geomSource);
        Shader fragShader = gl.glCreateShader(ShaderType.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(geomShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    public static Program createProgram(GraphicsLibrary gl, String vertFile, String fragFile) {
        return createProgramFromSource(gl,FileUtils.readFromFile(vertFile).toString(),FileUtils.readFromFile(fragFile).toString());
    }
    
    public static void setTexture(Program program, String name, jonl.jgl.Texture texture, int order) {
        program.setTexture(name, texture, order);
    }
    public static void setUniformi(Program program, String name, boolean b) {
        program.setUniformi(name, b ? 1 : 0);
    }
    public static void setUniformi(Program program, String name, int i) {
        program.setUniformi(name, i);
    }
    public static void setUniform(Program program, String name, float f) {
        program.setUniform(name, f);
    }
    public static void setUniform(Program program, String name, Vector2 v) {
        program.setUniform(name,v.x,v.y);
    }
    public static void setUniform(Program program, String name, Vector3 v) {
        program.setUniform(name,v.x,v.y,v.z);
    }
    public static void setUniform(Program program, String name, Vector4 v) {
        program.setUniform(name,v.x,v.y,v.z,v.w);
    }
    public static void setUniform(Program program, String name, float[] v) {
        switch (v.length) {
        case 1: program.setUniform(name,v[0]); break;
        case 2: program.setUniform(name,v[0],v[1]); break;
        case 3: program.setUniform(name,v[0],v[1],v[2]); break;
        case 4: program.setUniform(name,v[0],v[1],v[3]); break;
        default: throw new IndexOutOfBoundsException(); //TODO throw error
        }
    }
    public static void setUniform(Program program, String name, Matrix2 m) {
        FloatBuffer fb = BufferPool.borrowFloatBuffer(4,true);
        program.setUniformMat2(name,m.toFloatBuffer(fb));
        BufferPool.returnFloatBuffer(fb);
    }
    public static void setUniform(Program program, String name, Matrix3 m) {
        FloatBuffer fb = BufferPool.borrowFloatBuffer(9,true);
        program.setUniformMat3(name,m.toFloatBuffer(fb));
        BufferPool.returnFloatBuffer(fb);
    }
    public static void setUniform(Program program, String name, Matrix4 m) {
        FloatBuffer fb = BufferPool.borrowFloatBuffer(16,true);
        program.setUniformMat4(name,m.toFloatBuffer(fb));
        BufferPool.returnFloatBuffer(fb);
    }
    
    public static jonl.jgl.Texture.Internal map(Texture.Internal internal) {
        switch (internal) {
        case R16F       : return jonl.jgl.Texture.Internal.R16F;
        case R32F       : return jonl.jgl.Texture.Internal.R32F;
        case RGB16      : return jonl.jgl.Texture.Internal.RGB16;
        case RGB16F     : return jonl.jgl.Texture.Internal.RGB16F;
        case RGBA8      : return jonl.jgl.Texture.Internal.RGBA8;
        case RGBA16     : return jonl.jgl.Texture.Internal.RGBA16;
        case RGBA16F    : return jonl.jgl.Texture.Internal.RGBA16F;
        default         : return null;
        }
    }
    
    public static jonl.jgl.Texture.Filter map(Texture.Filter filter) {
        switch (filter) {
        case NEAREST    : return jonl.jgl.Texture.Filter.NEAREST;
        case LINEAR     : return jonl.jgl.Texture.Filter.LINEAR;
        case MIPMAP     : return jonl.jgl.Texture.Filter.MIPMAP;
        default         : return null;
        }
    }
    
    public static jonl.jgl.Texture.Wrap map(Texture.Wrap wrap) {
        switch (wrap) {
        case CLAMP  : return jonl.jgl.Texture.Wrap.CLAMP;
        case REPEAT : return jonl.jgl.Texture.Wrap.REPEAT;
        default     : return null;
        }
    }
    
    public static jonl.jgl.GraphicsLibrary.Mode map(Mesh.Mode mode) {
        switch (mode) {
        case TRIANGLES  : return jonl.jgl.GraphicsLibrary.Mode.TRIANGLES;
        case LINES      : return jonl.jgl.GraphicsLibrary.Mode.LINES;
        case LINE_STRIP : return jonl.jgl.GraphicsLibrary.Mode.LINE_STRIP;
        case POINTS     : return jonl.jgl.GraphicsLibrary.Mode.POINTS;
        default         : return null;
        }
    }
    
}
