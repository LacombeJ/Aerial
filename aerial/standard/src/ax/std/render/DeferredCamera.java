package ax.std.render;

import ax.engine.core.Camera;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Material;
import ax.engine.core.Texture;
import ax.engine.core.Texture.Filter;
import ax.engine.core.Texture.Internal;
import ax.engine.core.Texture.Wrap;
import ax.engine.core.render.FBCamera;

public class DeferredCamera extends Camera implements FBCamera {

    FrameBuffer buffer = null;
    Texture position = null;
    Texture normal = null;
    Texture texCoord = null;
    Texture stencil = null;
    Texture diffuse = null;
    Texture specular = null;
    Material material;
    
    RenderPass renderPass = null;
    
    public DeferredCamera(int width, int height, Material material) {
        resize(width,height);
        this.material = material;
    }
    
    void resize(int width, int height) {
        //TODO delete previous buffer if not null
        buffer = new FrameBuffer(width,height);
        position = new Texture(buffer.width(),buffer.height(),Internal.RGB16F,Wrap.CLAMP,Filter.LINEAR);
        normal = new Texture(buffer.width(),buffer.height(),Internal.RGB16F,Wrap.CLAMP,Filter.LINEAR);
        texCoord = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        stencil = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        diffuse = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        specular = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        
        buffer.attach(position);
        buffer.attach(normal);
        buffer.attach(texCoord);
        buffer.attach(stencil);
        buffer.attach(diffuse);
        buffer.attach(specular);
    }
    
    public Texture position() {
        return position;
    }
    
    public Texture normal() {
        return normal;
    }
    
    public Texture texCoord() {
        return texCoord;
    }
    
    public Texture stencil() {
        return stencil;
    }
    
    @Override
    public FrameBuffer buffer() {
        return buffer;
    }
    
    public void setRenderPass(RenderPass rp) {
        renderPass = rp;
    }

}
