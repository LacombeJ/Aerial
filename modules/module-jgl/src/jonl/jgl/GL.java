package jonl.jgl;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;
import jonl.jgl.utils.MeshData;

/**
 * Graphics Library
 * 
 * @author Jonathan Lacombe
 */
public interface GL {

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
    
    /** Sets the GL hint */
    public void glHint(HintTarget target, Hint hint);
    
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

    
    // Enums
    
    public final static Mask COLOR_BUFFER_BIT = Mask.COLOR_BUFFER_BIT;
    public final static Mask DEPTH_BUFFER_BIT = Mask.DEPTH_BUFFER_BIT;
    public static enum Mask {
        COLOR_BUFFER_BIT,
        DEPTH_BUFFER_BIT
    }
    
    public final static Target CULL_FACE            = Target.CULL_FACE;
    public final static Target DEPTH_TEST           = Target.DEPTH_TEST;
    public final static Target STENCIL_TEST         = Target.STENCIL_TEST;
    public final static Target SCISSOR_TEST         = Target.SCISSOR_TEST;
    public final static Target BLEND                = Target.BLEND;
    public final static Target POINT_SMOOTH         = Target.POINT_SMOOTH;
    public final static Target PROGRAM_POINT_SIZE   = Target.PROGRAM_POINT_SIZE;
    public final static Target POINT_SPRITE         = Target.POINT_SPRITE;
    public static enum Target {
        CULL_FACE,
        DEPTH_TEST,
        STENCIL_TEST,
        SCISSOR_TEST,
        BLEND,
        POINT_SMOOTH,
        PROGRAM_POINT_SIZE,
        POINT_SPRITE
    }
    
    public final static HintTarget POINT_SMOOTH_HINT    = HintTarget.POINT_SMOOTH_HINT;
    public final static HintTarget LINE_SMOOTH_HINT     = HintTarget.LINE_SMOOTH_HINT;
    public final static HintTarget POLYGON_SMOOTH_HINT  = HintTarget.POLYGON_SMOOTH_HINT;
    public static enum HintTarget {
        POINT_SMOOTH_HINT,
        LINE_SMOOTH_HINT,
        POLYGON_SMOOTH_HINT
    }
    
    public final static Hint FASTEST    = Hint.FASTEST;
    public final static Hint NICEST     = Hint.NICEST;
    public final static Hint DONT_CARE  = Hint.DONT_CARE;
    public static enum Hint {
        FASTEST,
        NICEST,
        DONT_CARE
    }
    
    public final static Blend NORMAL    = Blend.NORMAL;
    public final static Blend MULTIPLY  = Blend.MULTIPLY;
    public final static Blend ADDITIVE  = Blend.ADDITIVE;
    public final static Blend SCREEN    = Blend.SCREEN;
    public final static Blend SUBTRACT  = Blend.SUBTRACT;
    public final static Blend DARKEN    = Blend.DARKEN;
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
        DARKEN
    }
    
    public final static Factor ZERO                 = Factor.ZERO;
    public final static Factor ONE                  = Factor.ONE;
    public final static Factor SRC_COLOR            = Factor.SRC_COLOR;
    public final static Factor ONE_MINUS_SRC_COLOR  = Factor.ONE_MINUS_SRC_COLOR;
    public final static Factor SRC_ALPHA            = Factor.SRC_ALPHA;
    public final static Factor ONE_MINUS_SRC        = Factor.ONE_MINUS_SRC;
    public final static Factor DST_ALPHA            = Factor.DST_ALPHA;
    public final static Factor ONE_MINUS_DST_ALPHA  = Factor.ONE_MINUS_DST_ALPHA;
    public final static Factor ONE_MINUS_SRC_ALPHA  = Factor.ONE_MINUS_SRC_ALPHA;
    public final static Factor ONE_MINUS_DST_COLOR  = Factor.ONE_MINUS_DST_COLOR;
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
        ONE_MINUS_DST_COLOR
    }
    
    public final static Face FRONT              = Face.FRONT;
    public final static Face BACK               = Face.BACK;
    public final static Face FRONT_AND_BACK     = Face.FRONT_AND_BACK;
    public static enum Face {
        FRONT,
        BACK,
        FRONT_AND_BACK
    }
    
    public final static Mode POINTS     = Mode.POINTS;
    public final static Mode LINES      = Mode.LINES;
    public final static Mode LINE_STRIP = Mode.LINE_STRIP;
    public final static Mode TRIANGLES  = Mode.TRIANGLES;
    public static enum Mode {
        POINTS,
        LINES,
        LINE_STRIP,
        TRIANGLES
    }
    
    public final static PMode POINT = PMode.POINT;
    public final static PMode LINE  = PMode.LINE;
    public final static PMode FILL  = PMode.FILL;
    public static enum PMode {
        POINT,
        LINE,
        FILL
    }
    
    public final static ShaderType VERTEX_SHADER            = ShaderType.VERTEX_SHADER;
    public final static ShaderType FRAGMENT_SHADER          = ShaderType.FRAGMENT_SHADER;
    public final static ShaderType GEOMETRY_SHADER          = ShaderType.GEOMETRY_SHADER;
    public final static ShaderType TESS_CONTROL_SHADER      = ShaderType.TESS_CONTROL_SHADER;
    public final static ShaderType TESS_EVALUATION_SHADER   = ShaderType.TESS_EVALUATION_SHADER;
    public static enum ShaderType {
        VERTEX_SHADER,
        FRAGMENT_SHADER,
        GEOMETRY_SHADER,
        TESS_CONTROL_SHADER,
        TESS_EVALUATION_SHADER
    }

    
    
}
