package ax.engine.utils;

import ax.commons.io.FileUtils;
import ax.engine.core.Mesh;
import ax.engine.core.Texture;
import ax.graphics.GL;
import ax.graphics.Program;
import ax.graphics.Shader;
import ax.math.vector.Matrix2;
import ax.math.vector.Matrix3;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class GLUtils {

    public static Program createProgramFromSource(GL gl, String vertSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(GL.VERTEX_SHADER,vertSource);
        Shader fragShader = gl.glCreateShader(GL.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    public static Program createProgramFromSource(GL gl, String vertSource, String geomSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(GL.VERTEX_SHADER,vertSource);
        Shader geomShader = gl.glCreateShader(GL.GEOMETRY_SHADER,geomSource);
        Shader fragShader = gl.glCreateShader(GL.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(geomShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    public static Program createProgram(GL gl, String vertFile, String fragFile) {
        return createProgramFromSource(gl,FileUtils.readFromFile(vertFile).toString(),FileUtils.readFromFile(fragFile).toString());
    }
    
    public static void setTexture(Program program, String name, ax.graphics.Texture texture, int order) {
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
        program.setUniformMat2(name,m.toArray());
    }
    public static void setUniform(Program program, String name, Matrix3 m) {
        program.setUniformMat3(name,m.toArray());
    }
    public static void setUniform(Program program, String name, Matrix4 m) {
        program.setUniformMat4(name,m.toArray());
    }
    
    public static void setUniform(Program program, String name, Vector2[] v) {
        for (int i=0; i<v.length; i++) {
            setUniform(program,name+"["+i+"]",v[i]);
        }
    }
    public static void setUniform(Program program, String name, Vector3[] v) {
        for (int i=0; i<v.length; i++) {
            setUniform(program,name+"["+i+"]",v[i]);
        }
    }
    public static void setUniform(Program program, String name, Vector4[] v) {
        for (int i=0; i<v.length; i++) {
            setUniform(program,name+"["+i+"]",v[i]);
        }
    }
    
    public static ax.graphics.Texture.Internal map(Texture.Internal internal) {
        switch (internal) {
        case R16F               : return ax.graphics.Texture.Internal.R16F;
        case R32F               : return ax.graphics.Texture.Internal.R32F;
        case RGB16              : return ax.graphics.Texture.Internal.RGB16;
        case RGB16F             : return ax.graphics.Texture.Internal.RGB16F;
        case RGBA8              : return ax.graphics.Texture.Internal.RGBA8;
        case RGBA16             : return ax.graphics.Texture.Internal.RGBA16;
        case RGBA16F            : return ax.graphics.Texture.Internal.RGBA16F;
        case DEPTH_COMPONENT    : return ax.graphics.Texture.Internal.DEPTH_COMPONENT;
        default                 : return null;
        }
    }
    
    public static ax.graphics.Texture.Filter map(Texture.Filter filter) {
        switch (filter) {
        case NEAREST    : return ax.graphics.Texture.Filter.NEAREST;
        case LINEAR     : return ax.graphics.Texture.Filter.LINEAR;
        case MIPMAP     : return ax.graphics.Texture.Filter.MIPMAP;
        default         : return null;
        }
    }
    
    public static ax.graphics.Texture.Wrap map(Texture.Wrap wrap) {
        switch (wrap) {
        case CLAMP  : return ax.graphics.Texture.Wrap.CLAMP;
        case REPEAT : return ax.graphics.Texture.Wrap.REPEAT;
        default     : return null;
        }
    }
    
    public static ax.graphics.GL.Mode map(Mesh.Mode mode) {
        switch (mode) {
        case TRIANGLES  : return ax.graphics.GL.TRIANGLES;
        case LINES      : return ax.graphics.GL.LINES;
        case LINE_STRIP : return ax.graphics.GL.LINE_STRIP;
        case POINTS     : return ax.graphics.GL.POINTS;
        default         : return null;
        }
    }
    
}
