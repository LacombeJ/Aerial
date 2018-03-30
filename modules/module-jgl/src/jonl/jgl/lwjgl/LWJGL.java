package jonl.jgl.lwjgl;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import jonl.jgl.AbstractGraphicsLibrary;
import jonl.jgl.Font;
import jonl.jgl.FrameBuffer;
import jonl.jgl.Mesh;
import jonl.jgl.Program;
import jonl.jgl.Shader;
import jonl.jgl.Texture;
import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;

class LWJGL extends AbstractGraphicsLibrary {

    @Override
    public void glClear(Mask... masks) {
        int mask = 0;
        for (Mask m : masks)
            mask = mask | getMask(m);
        GL11.glClear(mask);
    }
    
    @Override
    public void glClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r,g,b,a);
    }
    
    @Override
    public void glScissor(int x, int y, int width, int height) {
        GL11.glScissor(x,y,width,height);
    }
    
    @Override
    public void glScissor(int[] box) {
        GL11.glScissor(box[0],box[1],box[2],box[3]);
    }
    
    @Override
    public int[] glGetScissor() {
        int[] box = new int[4];
        GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX,box);
        return box;
    }
    
    @Override
    public void glEnable(Target target) {
        GL11.glEnable(getTarget(target));
    }
    
    @Override
    public void glDisable(Target target) {
        GL11.glDisable(getTarget(target));
    }
    
    @Override
    public void glHint(HintTarget target, Hint hint) {
        GL11.glHint(getHintTarget(target), getHint(hint));
    }
    
    @Override
    public String glGetVersion() {
        return GL11.glGetString(GL11.GL_VERSION);
    }
    
    @Override
    public String glGetGLSLVersion() {
        return GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
    }
    
    @Override
    public int glGetGLSLVersioni() {
        return Integer.parseInt(glGetGLSLVersion().replace(".","").split(" ")[0]);
    }
    
    @Override
    public void glBlendFunc(Factor src, Factor dst) {
        GL11.glBlendFunc(getFactor(src),getFactor(dst));
    }
    
    @Override
    public void glBlendFunc(Blend type) {
        GL11.glEnable(GL11.GL_BLEND);
        switch (type) {
        case NORMAL:
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            break;
        case MULTIPLY:
            GL11.glBlendFunc(GL11.GL_ZERO,GL11.GL_SRC_COLOR);
            break;
        case ADDITIVE:
            GL11.glBlendFunc(GL11.GL_ONE,GL11.GL_ONE);
            break;
        case SCREEN:
            GL11.glBlendFunc(GL11.GL_ONE,GL11.GL_ONE_MINUS_SRC_ALPHA);
            break;
        case SUBTRACT:
            GL11.glBlendFunc(GL11.GL_ZERO,GL11.GL_ONE_MINUS_SRC_COLOR);
            break;
        case DARKEN:
            GL11.glBlendFunc(GL11.GL_ONE,GL11.GL_SRC_COLOR);
            break;
        default:
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            break;
        }
    }
    
    @Override
    public void glViewport(int x, int y, int width, int height) {
        GL11.glViewport(x,y,width,height);
    }
    
    @Override
    public void glPointSize(float size) {
        GL11.glPointSize(size);
    }
    
    @Override
    public void glLineWidth(float width) {
        GL11.glLineWidth(width);
    }
    
    @Override
    public void glPolygonMode(Face face, PMode mode) {
        GL11.glPolygonMode(getFace(face), getPMode(mode));
    }
    
    @Override
    public boolean glHasError() {
        return GL11.glGetError()!=GL11.GL_NO_ERROR;
    }
    
    @Override
    public Texture glGenTexture() {
        return new LWJGLTexture();
    }
    
    @Override
    public Texture glGenTexture(FloatBuffer data, int width, int height,
            Internal internal, Wrap wrap, Filter filter) {
        return new LWJGLTexture(data,width,height,internal,wrap,filter);
    }
    
    @Override
    public void glBindTexture(Texture texture) {
        if (texture==null)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
        else
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,((LWJGLTexture)texture).getID());
    }
    
    @Override
    public float[] glGetTexImage(Texture texture) {
        float[] pixels = new float[texture.getWidth()*texture.getHeight()*4];
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,((LWJGLTexture)texture).getID());
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,GL11.GL_FLOAT,pixels);
        return pixels;
    }
    
    @Override
    public Font glGenFont(String font, int type, int size, boolean antialias) {
        return new LWJGLFont(this,font,type,size,antialias);
    }
    
    @Override
    public FrameBuffer glGenFramebuffer(int width, int height) {
        return new LWJGLFrameBuffer(width,height);
    }
    
    @Override
    public FrameBuffer glGenFramebuffer(Texture... textures) {
        return new LWJGLFrameBuffer(textures);
    }
    
    @Override
    public void glBindFramebuffer(FrameBuffer fb) {
        if (fb==null)
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,0);
        else
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,((LWJGLFrameBuffer)fb).getID());
    }
    
    @Override
    public float[] glReadPixels(int x, int y, int width, int height) {
        float[] pixels = new float[width*height*4];
        GL11.glReadPixels(x,y,width,height,GL11.GL_RGBA,GL11.GL_FLOAT,pixels);
        return pixels;
    }

    @Override
    public Mesh glGenMesh(float[] vertexData, float[] normalData, float[] texCoordData, int[] indices) {
        return new LWJGLMesh(vertexData,normalData,texCoordData,indices);
    }
    
    @Override
    public Mesh glGenMesh(float[] vertexData, int[] indices) {
        return new LWJGLMesh(vertexData,indices);
    }
    
    @Override
    public void glRender(Mesh mesh) {
        ((LWJGLMesh)mesh).render();
    }
    
    @Override
    public void glRender(Mesh mesh, Mode mode) {
        ((LWJGLMesh)mesh).render(getMode(mode));
    }
    
    @Override
    public void glRenderInstance(Mesh mesh, int count) {
        ((LWJGLMesh)mesh).renderInstance(count);
    }
    
    @Override
    public void glRenderInstance(Mesh mesh, Mode mode, int count) {
        ((LWJGLMesh)mesh).renderInstance(getMode(mode), count);
    }

    @Override
    public Program glCreateProgram() {
        return new LWJGLProgram();
    }
    
    @Override
    public Shader glCreateShader(ShaderType type) {
        return new LWJGLShader(type);
    }
    
    @Override
    public Shader glCreateShader(ShaderType type, String source) {
        return new LWJGLShader(type,source);
    }
    
    @Override
    public void glUseProgram(Program program) {
        if (program==null)
            GL20.glUseProgram(0);
        else
            GL20.glUseProgram(((LWJGLProgram)program).getID());
    }
    
    static int getMask(Mask m) {
        switch(m) {
        case COLOR_BUFFER_BIT:  return GL11.GL_COLOR_BUFFER_BIT;
        case DEPTH_BUFFER_BIT:  return GL11.GL_DEPTH_BUFFER_BIT;
        default:                throw new IllegalStateException("Unknown mask");
        }
    }
    
    static Mask getMask(int m) {
        switch(m) {
        case GL11.GL_COLOR_BUFFER_BIT:  return Mask.COLOR_BUFFER_BIT;
        case GL11.GL_DEPTH_BUFFER_BIT:  return Mask.DEPTH_BUFFER_BIT;
        default:                        throw new IllegalStateException("Unknown mask");
        }
    }
    
    static int getTarget(Target t) {
        switch(t) {
        case CULL_FACE:             return GL11.GL_CULL_FACE;
        case DEPTH_TEST:            return GL11.GL_DEPTH_TEST;
        case STENCIL_TEST:          return GL11.GL_STENCIL_TEST;
        case SCISSOR_TEST:          return GL11.GL_SCISSOR_TEST;
        case BLEND:                 return GL11.GL_BLEND;
        case POINT_SMOOTH:          return GL11.GL_POINT_SMOOTH;
        case PROGRAM_POINT_SIZE:    return GL32.GL_PROGRAM_POINT_SIZE;
        default:                    throw new IllegalStateException("Unknown target");
        }
    }
    
    static int getHintTarget(HintTarget ht) {
        switch(ht) {
        case POINT_SMOOTH_HINT:     return GL11.GL_POINT_SMOOTH_HINT;
        case LINE_SMOOTH_HINT:      return GL11.GL_LINE_SMOOTH_HINT;
        case POLYGON_SMOOTH_HINT:   return GL11.GL_POLYGON_SMOOTH_HINT;
        default:                    throw new IllegalStateException("Unknown hint target");
        }
    }
    
    static int getHint(Hint h) {
        switch(h) {
        case FASTEST:   return GL11.GL_FASTEST;
        case NICEST:    return GL11.GL_NICEST;
        case DONT_CARE: return GL11.GL_DONT_CARE;
        default:        throw new IllegalStateException("Unknown hint");
        }
    }
    
    static int getFactor(Factor f) {
        switch(f) {
        case ZERO:                  return GL11.GL_ZERO;
        case ONE:                   return GL11.GL_ONE;
        case SRC_COLOR:             return GL11.GL_SRC_COLOR;
        case ONE_MINUS_SRC_COLOR:   return GL11.GL_ONE_MINUS_SRC_COLOR;
        case DST_ALPHA:             return GL11.GL_DST_ALPHA;
        case ONE_MINUS_DST_ALPHA:   return GL11.GL_ONE_MINUS_DST_ALPHA;
        case ONE_MINUS_SRC_ALPHA:   return GL11.GL_ONE_MINUS_SRC_ALPHA;
        case ONE_MINUS_DST_COLOR:   return GL11.GL_ONE_MINUS_DST_COLOR;
        default:                    throw new IllegalStateException("Unknown factor");
        }
    }
    
    static int getFace(Face m) {
        switch(m) {
        case FRONT:                 return GL11.GL_FRONT;
        case BACK:                  return GL11.GL_BACK;
        case FRONT_AND_BACK:        return GL11.GL_FRONT_AND_BACK;
        default:                    throw new IllegalStateException("Unknown face");
        }
    }
    
    static int getMode(Mode m) {
        switch(m) {
        case POINTS:                return GL11.GL_POINTS;
        case LINES:                 return GL11.GL_LINES;
        case LINE_STRIP:            return GL11.GL_LINE_STRIP;
        case TRIANGLES:             return GL11.GL_TRIANGLES;
        default:                    throw new IllegalStateException("Unknown mode");
        }
    }
    
    static int getPMode(PMode m) {
        switch(m) {
        case POINT:                 return GL11.GL_POINT;
        case LINE:                  return GL11.GL_LINE;
        case FILL:                  return GL11.GL_FILL;
        default:                    throw new IllegalStateException("Unknown polygon mode");
        }
    }
    
    static int getShaderType(ShaderType t) {
        switch(t) {
        case VERTEX_SHADER:             return GL20.GL_VERTEX_SHADER;
        case FRAGMENT_SHADER:           return GL20.GL_FRAGMENT_SHADER;
        case GEOMETRY_SHADER:           return GL32.GL_GEOMETRY_SHADER;
        case TESS_CONTROL_SHADER:       return GL40.GL_TESS_CONTROL_SHADER;
        case TESS_EVALUATION_SHADER:    return GL40.GL_TESS_EVALUATION_SHADER;
        default:                        throw new IllegalStateException("Unknown shader type");
        }
    }
    
    static ShaderType getShaderType(int t) {
        switch(t) {
        case GL20.GL_VERTEX_SHADER:             return ShaderType.VERTEX_SHADER;
        case GL20.GL_FRAGMENT_SHADER:           return ShaderType.FRAGMENT_SHADER;
        case GL32.GL_GEOMETRY_SHADER:           return ShaderType.GEOMETRY_SHADER;
        case GL40.GL_TESS_CONTROL_SHADER:       return ShaderType.TESS_CONTROL_SHADER;
        case GL40.GL_TESS_EVALUATION_SHADER:    return ShaderType.TESS_EVALUATION_SHADER;
        default:                                throw new IllegalStateException("Unknown shader type");
        }
    }

    


}
