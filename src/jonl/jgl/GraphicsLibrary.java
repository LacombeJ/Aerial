package jonl.jgl;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;
import jonl.jgl.utils.MeshData;

/**
 * @author Jonathan Lacombe
 */
public interface GraphicsLibrary {

    /** Clears given buffers */
    public void glClear(Mask... masks);
    
    /** Sets scissor bounds */
    public void glScissor(int x, int y, int width, int height);
    
    public void glScissor(int[] box);
    
    public int[] glGetScissor();
    
    /** Enables target */
    public void glEnable(Target target);
    
    /** Disables target */
    public void glDisable(Target target);
    
    /** Returns GL version */
    public String glGetVersion();
    
    /** Returns GL shading version */
    public String glGetGLSLVersion();
    
    /** Retruns GL shading version as an int */
    public int glGetGLSLVersioni();
    
    /** Sets the blend function */
    public void glBlendFunc(Factor src, Factor dst);
    
    /**
     * Sets the blending type
     * @see #glBlendFunc(int, int)
     */
    public void glBlendFunc(Blend type);
    
    /** Sets the clear color (background color) */
    public void glClearColor(float r, float g, float b, float a);
    
    /** @see #setClearColor(float, float, float, float) */
    public void glClearColor(float[] color);
    
    /** Sets the view port for rendering */
    public void glViewport(int x, int y, int width, int height);
    
    public void glPointSize(float size);
    
    public void glLineWidth(float width);
    
    public void glPolygonMode(Face face, PMode mode);
    
    public boolean glHasError();
    
    
    
    
    public Texture glGenTexture();
    
    public Texture glGenTexture(FloatBuffer data, int width, int height,
            Internal internal, Wrap wrap, Filter filter);
    
    public Texture glGenTexture(FloatBuffer data, int width, int height);
    
    /**
     * 
     * @param data array of texture data which should be of size [width*height*dim]
     * where, for example, dim(RGBA)=4
     * @param width
     * @param height
     * @param internal
     * @param wrap
     * @param filter
     * @return
     */
    public Texture glGenTexture(float[] data, int width, int height, Internal internal, Wrap wrap, Filter filter);
    
    /** @see #glGenTexture(float[], int, int, Internal, Wrap, Filter) */
    public Texture glGenTexture(float[] data, int width, int height);
    
    public Texture glGenTexture(BufferedImage image, Internal internal, Wrap wrap, Filter filter);
    
    public Texture glGenTexture(BufferedImage image);
    
    public Texture glGenTexture(String file, Internal internal, Wrap wrap, Filter filter);
    
    public Texture glGenTexture(String file);
    
    public Texture glGenTexture(int width, int height, Internal internal, Wrap wrap, Filter filter);
  
    public Texture glGenTexture(int width, int height);
    
    /** Binds the given texture or 0 if null */
    public void glBindTexture(Texture texture);
    
    
    /**
     * Binds texture then calls glGetTexImage; does not free texture
     * @return float array of texture
     */
    public float[] glGetTexImage(Texture texture);
    
    
    
    public Font glGenFont(String font, int type, int size, boolean antialias);
    
    
    /** @return a new framebuffer with no attached textue */
    public FrameBuffer glGenFramebuffer(int width, int height);
    
    /** @return a new framebuffer with attached textures */
    public FrameBuffer glGenFramebuffer(Texture... textures);
    
    
    
    /**
     * Binds the given buffer or 0 if null<p>
     * Remember to clear the color buffer and depth buffer bit after this call
     * if you plan on rendering to frame buffer.
     */
    public void glBindFramebuffer(FrameBuffer fb);
    
    /**
     * Read pixels from the currently bound buffer
     * @return float array of pixels
     */
    public float[] glReadPixels(int x, int y, int width, int height);
    
    /** @return a new mesh/VBO */
    public Mesh glGenMesh(float[] vertexData, float[] normalData,
            float[] texCoordData, int[] indices);
    
    public Mesh glGenMesh(float[] vertexData, int[] indices);
    
    /** @see #createMesh(float[], float[], float[], int[]) */
    public Mesh glGenMesh(MeshData md);
    
    public void glRender(Mesh mesh);
    
    public void glRender(Mesh mesh, Mode mode);
    
    public void glRenderInstance(Mesh mesh, int count);
    
    public void glRenderInstance(Mesh mesh, Mode mode, int count);
    
    /** @return a new shader program with the given source code */
    public Program glCreateProgram();
    
    /** @return a shader program of the given type */
    public Shader glCreateShader(ShaderType type);
    
    /** @return a shader program of the given type and compiled with the given source */
    public Shader glCreateShader(ShaderType type, String source);
    
    /** Uses the given program or 0 if null */
    public void glUseProgram(Program program);

    public static enum Mask {
        COLOR_BUFFER_BIT,
        DEPTH_BUFFER_BIT;
    }
    
    public static enum Target {
        CULL_FACE,
        DEPTH_TEST,
        STENCIL_TEST,
        SCISSOR_TEST,
        BLEND;
    }
    
    public static enum Blend {
        /** src=SRC_ALPHA dst=ONE_MINUS_SRC_ALPHA   */
        NORMAL,
        
        /** src=ZERO, dst=SRC_COLOR */
        MULTIPLY,
        
        /** src=ONE, dst=ONE */
        ADDITIVE,
        
        /** src=ONE, dst=ONE_MINUS_SRC_ALPHAr */
        SCREEN,
        
        /** src=ZERO, dst=ONE_MINUS_SRC_COLOR */
        SUBTRACT,
        
        /** src=ONE, dst=SRC_COLOR */
        DARKEN;
    }
    
    public static enum Factor {
        ZERO,
        ONE,
        SRC_COLOR,
        ONE_MINUS_SRC_COLOR,
        SRC_ALPHA,
        ONE_MINUS_SRC,ALPHA,
        DST_ALPHA,
        ONE_MINUS_DST_ALPHA,
        ONE_MINUS_SRC_ALPHA,
        ONE_MINUS_DST_COLOR;
    }
    
    public static enum Face {
        FRONT,
        BACK,
        FRONT_AND_BACK
    }
    
    public static enum Mode {
        POINTS,
        LINES,
        LINE_STRIP,
        TRIANGLES
    }
    
    public static enum PMode {
        POINT,
        LINE,
        FILL
    }
    
    public static enum ShaderType {
        VERTEX_SHADER,
        FRAGMENT_SHADER,
        GEOMETRY_SHADER,
        TESS_CONTROL_SHADER,
        TESS_EVALUATION_SHADER
    }

    
    
}
