package jonl.jgl;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;
import jonl.jgl.utils.MeshData;
import jonl.jutils.misc.BufferPool;
import jonl.jutils.misc.ImageUtils;

/**
 * Abstract RenderManager Class
 * 
 * @author Jonathan Lacombe
 *
 */
public abstract class AbstractGraphicsLibrary implements GraphicsLibrary {
    
    @Override
    public void glClearColor(float[] color) {
        glClearColor(color[0],color[1],color[2],color[3]);
    }
    
    @Override
    public void glBlendFunc(Blend type) {
        glEnable(Target.BLEND);
        switch (type) {
        case MULTIPLY:
            glBlendFunc(Factor.ZERO,Factor.SRC_COLOR);
            break;
        case ADDITIVE:
            glBlendFunc(Factor.ONE,Factor.ONE);
            break;
        case SCREEN:
            glBlendFunc(Factor.ONE,Factor.ONE_MINUS_SRC_ALPHA);
            break;
        case SUBTRACT:
            glBlendFunc(Factor.ZERO,Factor.ONE_MINUS_SRC_COLOR);
            break;
        case DARKEN:
            glBlendFunc(Factor.ONE,Factor.SRC_COLOR);
            break;
        default:
            glBlendFunc(Factor.SRC_ALPHA,Factor.ONE_MINUS_SRC_ALPHA);
            break;
        }
    }
    
    @Override
    public Texture glGenTexture(FloatBuffer data, int width, int height) {
        return glGenTexture(data,width,height,Internal.RGBA16,Wrap.CLAMP,Filter.MIPMAP);
    }
    
    @Override
    public Texture glGenTexture(float[] data, int width, int height, Internal internal, Wrap wrap, Filter filter) {
        return glGenTexture(BufferPool.getFloatBuffer(data,true),width,height,internal,wrap,filter);
    }
    
    @Override
    public Texture glGenTexture(float[] data, int width, int height) {
        return glGenTexture(BufferPool.getFloatBuffer(data,true),width,height);
    }
    
    @Override
    public Texture glGenTexture(BufferedImage image, Internal internal, Wrap wrap, Filter filter) {
        return glGenTexture(ImageUtils.getBufferData(image),image.getWidth(),image.getHeight(),internal,wrap,filter);
    }
    
    @Override
    public Texture glGenTexture(BufferedImage image) {
        return glGenTexture(ImageUtils.getBufferData(image),image.getWidth(),image.getHeight());
    }
    
    @Override
    public Texture glGenTexture(String file, Internal internal, Wrap wrap, Filter filter) {
        return glGenTexture(ImageUtils.loadBufferedImage(file),internal,wrap,filter);
    }
    
    @Override
    public Texture glGenTexture(String file) {
        return glGenTexture(ImageUtils.loadBufferedImage(file));
    }
    
    @Override
    public Texture glGenTexture(int width, int height, Internal internal, Wrap wrap, Filter filter) {
        return glGenTexture(ImageUtils.getBufferData(new float[4],width,height),width,height,internal,wrap,filter);
    }
    
    @Override
    public Texture glGenTexture(int width, int height) {
        return glGenTexture(ImageUtils.getBufferData(new float[4],width,height),width,height);
    }
    
    @Override
    public Mesh glGenMesh(MeshData meshData) {
        return glGenMesh(meshData.vertexData,meshData.normalData,meshData.texCoordData,meshData.indices);
    }
    
}
