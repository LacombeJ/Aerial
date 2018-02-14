package jonl.ge.core;

import jonl.ge.core.Texture.Filter;
import jonl.ge.core.Texture.Internal;
import jonl.ge.core.Texture.Wrap;

public class RenderTarget extends Camera {
    
    //TODO add support for gBuffer frameBuffers
    
    public static final int POSITION_TEXTURE    = 0;
    public static final int NORMALS_TEXTURE     = 1;
    public static final int DIFFUSE_TEXTURE     = 2;
    public static final int SPECULAR_TEXTURE    = 3;
    public static final int MATERIAL_TEXTURE    = 4;
    public static final int RENDER_TEXTURE      = 5;
    
    FrameBuffer gBuffer = null;
    FrameBuffer buffer = null;
    
    public RenderTarget(int width, int height) {
        buffer = new FrameBuffer(width,height);
        Texture texture = new Texture(width,height,Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        buffer.attach(texture);
        
        gBuffer = new FrameBuffer(width,height);
    }
    
    public Texture getTexture(int type) {
        if (type==RENDER_TEXTURE) return buffer.getTexture(0);
        return gBuffer.getTexture(type);
    }
    
    public Texture getRenderTexture() {
        return buffer.getTexture(0);
    }
    
    
}
