package ax.graphics;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import ax.graphics.utils.MeshData;

/**
 * Graphics Library
 * 
 * @author Jonathan Lacombe
 */
public interface GL {

    /** Clears given buffers */
    void glClear(Mask... masks);
    
    /** Sets scissor bounds */
    void glScissor(int x, int y, int width, int height);
    
    void glScissor(int[] box);
    
    int[] glGetScissor();
    
    /** Enables target */
    void glEnable(Target target);
    
    /** Disables target */
    void glDisable(Target target);
    
    /** Sets the GL hint */
    void glHint(HintTarget target, Hint hint);
    
    /** Returns GL version */
    String glGetVersion();
    
    /** Returns GL shading version */
    String glGetGLSLVersion();
    
    /** Retruns GL shading version as an int */
    int glGetGLSLVersioni();
    
    /** Sets the blend function */
    void glBlendFunc(Factor src, Factor dst);
    
    /**
     * Sets the blending type
     * @see #glBlendFunc(Factor, Factor)
     */
    void glBlendFunc(Blend type);
    
    /** Sets the clear color (background color) */
    void glClearColor(float r, float g, float b, float a);
    
    /** @see #glClearColor(float, float, float, float) */
    void glClearColor(float[] color);
    
    /** Sets the view port for rendering */
    void glViewport(int x, int y, int width, int height);
    
    void glPointSize(float size);
    
    void glLineWidth(float width);
    
    void glPolygonMode(Face face, PMode mode);
    
    boolean glHasError();
    
    
    
    
    Texture glGenTexture();
    
    Texture glGenTexture(FloatBuffer data, int width, int height,
            Internal internal, Wrap wrap, Filter filter);
    
    Texture glGenTexture(FloatBuffer data, int width, int height);
    
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
    Texture glGenTexture(float[] data, int width, int height, Internal internal, Wrap wrap, Filter filter);
    
    /** @see #glGenTexture(float[], int, int, Internal, Wrap, Filter) */
    Texture glGenTexture(float[] data, int width, int height);
    
    Texture glGenTexture(BufferedImage image, Internal internal, Wrap wrap, Filter filter);
    
    Texture glGenTexture(BufferedImage image);
    
    Texture glGenTexture(String file, Internal internal, Wrap wrap, Filter filter);
    
    Texture glGenTexture(String file);
    
    Texture glGenTexture(int width, int height, Internal internal, Wrap wrap, Filter filter);
  
    Texture glGenTexture(int width, int height);
    
    /** Binds the given texture or 0 if null */
    void glBindTexture(Texture texture);
    
    
    /**
     * Binds texture then calls glGetTexImage; does not free texture
     * @return float array of texture
     */
    float[] glGetTexImage(Texture texture);
    
    
    
    /** @return a new framebuffer with no attached textue */
    FrameBuffer glGenFramebuffer(int width, int height);
    
    /** @return a new framebuffer with attached textures */
    FrameBuffer glGenFramebuffer(Texture... textures);
    
    
    
    /**
     * Binds the given buffer or 0 if null<p>
     * Remember to clear the color buffer and depth buffer bit after this call
     * if you plan on rendering to frame buffer.
     */
    void glBindFramebuffer(FrameBuffer fb);
    
    /**
     * Read pixels from the currently bound buffer
     * @return float array of pixels
     */
    float[] glReadPixels(int x, int y, int width, int height);
    
    /** @return a new mesh/VBO */
    Mesh glGenMesh(float[] vertexData, float[] normalData,
            float[] texCoordData, int[] indices);
    
    Mesh glGenMesh(float[] vertexData, int[] indices);
    
    /** @see #glGenMesh(float[], float[], float[], int[]) */
    Mesh glGenMesh(MeshData md);
    
    void glRender(Mesh mesh);
    
    void glRender(Mesh mesh, Mode mode);
    
    void glRenderInstance(Mesh mesh, int count);
    
    void glRenderInstance(Mesh mesh, Mode mode, int count);
    
    /** @return a new shader program with the given source code */
    Program glCreateProgram();
    
    /** @return a shader program of the given type */
    Shader glCreateShader(ShaderType type);
    
    /** @return a shader program of the given type and compiled with the given source */
    Shader glCreateShader(ShaderType type, String source);
    
    /** Uses the given program or 0 if null */
    void glUseProgram(Program program);

    
    // Enums
    
    Mask COLOR_BUFFER_BIT = Mask.COLOR_BUFFER_BIT;
    Mask DEPTH_BUFFER_BIT = Mask.DEPTH_BUFFER_BIT;
    enum Mask {
        COLOR_BUFFER_BIT,
        DEPTH_BUFFER_BIT
    }
    
    Target CULL_FACE            = Target.CULL_FACE;
    Target DEPTH_TEST           = Target.DEPTH_TEST;
    Target STENCIL_TEST         = Target.STENCIL_TEST;
    Target SCISSOR_TEST         = Target.SCISSOR_TEST;
    Target BLEND                = Target.BLEND;
    Target POINT_SMOOTH         = Target.POINT_SMOOTH;
    Target PROGRAM_POINT_SIZE   = Target.PROGRAM_POINT_SIZE;
    Target POINT_SPRITE         = Target.POINT_SPRITE;
    enum Target {
        CULL_FACE,
        DEPTH_TEST,
        STENCIL_TEST,
        SCISSOR_TEST,
        BLEND,
        POINT_SMOOTH,
        PROGRAM_POINT_SIZE,
        POINT_SPRITE
    }
    
    HintTarget POINT_SMOOTH_HINT    = HintTarget.POINT_SMOOTH_HINT;
    HintTarget LINE_SMOOTH_HINT     = HintTarget.LINE_SMOOTH_HINT;
    HintTarget POLYGON_SMOOTH_HINT  = HintTarget.POLYGON_SMOOTH_HINT;
    enum HintTarget {
        POINT_SMOOTH_HINT,
        LINE_SMOOTH_HINT,
        POLYGON_SMOOTH_HINT
    }
    
    Hint FASTEST    = Hint.FASTEST;
    Hint NICEST     = Hint.NICEST;
    Hint DONT_CARE  = Hint.DONT_CARE;
    enum Hint {
        FASTEST,
        NICEST,
        DONT_CARE
    }
    
    Blend NORMAL    = Blend.NORMAL;
    Blend MULTIPLY  = Blend.MULTIPLY;
    Blend ADDITIVE  = Blend.ADDITIVE;
    Blend SCREEN    = Blend.SCREEN;
    Blend SUBTRACT  = Blend.SUBTRACT;
    Blend DARKEN    = Blend.DARKEN;
    enum Blend {
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
    
    Factor ZERO                 = Factor.ZERO;
    Factor ONE                  = Factor.ONE;
    Factor SRC_COLOR            = Factor.SRC_COLOR;
    Factor ONE_MINUS_SRC_COLOR  = Factor.ONE_MINUS_SRC_COLOR;
    Factor SRC_ALPHA            = Factor.SRC_ALPHA;
    Factor ONE_MINUS_SRC        = Factor.ONE_MINUS_SRC;
    Factor DST_ALPHA            = Factor.DST_ALPHA;
    Factor ONE_MINUS_DST_ALPHA  = Factor.ONE_MINUS_DST_ALPHA;
    Factor ONE_MINUS_SRC_ALPHA  = Factor.ONE_MINUS_SRC_ALPHA;
    Factor ONE_MINUS_DST_COLOR  = Factor.ONE_MINUS_DST_COLOR;
    enum Factor {
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
    
    Face FRONT              = Face.FRONT;
    Face BACK               = Face.BACK;
    Face FRONT_AND_BACK     = Face.FRONT_AND_BACK;
    enum Face {
        FRONT,
        BACK,
        FRONT_AND_BACK
    }
    
    Mode POINTS     = Mode.POINTS;
    Mode LINES      = Mode.LINES;
    Mode LINE_STRIP = Mode.LINE_STRIP;
    Mode TRIANGLES  = Mode.TRIANGLES;
    enum Mode {
        POINTS,
        LINES,
        LINE_STRIP,
        TRIANGLES
    }
    
    PMode POINT = PMode.POINT;
    PMode LINE  = PMode.LINE;
    PMode FILL  = PMode.FILL;
    enum PMode {
        POINT,
        LINE,
        FILL
    }
    
    ShaderType VERTEX_SHADER            = ShaderType.VERTEX_SHADER;
    ShaderType FRAGMENT_SHADER          = ShaderType.FRAGMENT_SHADER;
    ShaderType GEOMETRY_SHADER          = ShaderType.GEOMETRY_SHADER;
    ShaderType TESS_CONTROL_SHADER      = ShaderType.TESS_CONTROL_SHADER;
    ShaderType TESS_EVALUATION_SHADER   = ShaderType.TESS_EVALUATION_SHADER;
    enum ShaderType {
        VERTEX_SHADER,
        FRAGMENT_SHADER,
        GEOMETRY_SHADER,
        TESS_CONTROL_SHADER,
        TESS_EVALUATION_SHADER
    }


    Internal R16F               = Internal.R16F;
    Internal R32F               = Internal.R32F;
    Internal RGB16              = Internal.RGB16;
    Internal RGB16F             = Internal.RGB16F;
    Internal RGBA8              = Internal.RGBA8;
    Internal RGBA16             = Internal.RGBA16;
    Internal RGBA16F            = Internal.RGBA16F;
    Internal DEPTH_COMPONENT    = Internal.DEPTH_COMPONENT;
    enum Internal {
        R16F,
        R32F,
        RGB16,
        RGB16F,
        RGBA8,
        RGBA16,
        RGBA16F,
        DEPTH_COMPONENT
    }

    Wrap CLAMP  = Wrap.CLAMP;
    Wrap REPEAT = Wrap.REPEAT;
    enum Wrap {
        CLAMP,
        REPEAT
    }

    Filter NEAREST  = Filter.NEAREST;
    Filter LINEAR   = Filter.LINEAR;
    Filter MIPMAP   = Filter.MIPMAP;
    enum Filter {
        NEAREST,
        LINEAR,
        MIPMAP
    }
    
}
