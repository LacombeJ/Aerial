package jonl.ge.core.render;

import jonl.ge.core.Component;
import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Service;
import jonl.ge.core.Texture;
import jonl.ge.core.Texture.Filter;
import jonl.ge.core.Texture.Internal;
import jonl.ge.core.Texture.Wrap;
import jonl.ge.core.material.ShaderMaterial;
import jonl.jutils.func.List;

public class RenderTexture extends Component {
    
    ShaderMaterial material = null;
    FrameBuffer[] buffers = null;
    boolean clearOnRender = true;
    
    public RenderTexture(int numBuffers, ShaderMaterial material, int width, int height, Internal internal, Wrap wrap, Filter filter) {
        this.material = material;
        
        buffers = new FrameBuffer[numBuffers];
        for (int i=0; i<buffers.length; i++) {
            Texture texture = new Texture(width,height,internal,wrap,filter);
            FrameBuffer buffer = new FrameBuffer(texture);
            buffers[i] = buffer;
        }
        
    }
    
    public RenderTexture(int numBuffers, ShaderMaterial material, int width, int height, Internal internal) {
        this(numBuffers,material,width,height,Internal.RGB16,Wrap.CLAMP,Filter.NEAREST);
    }
    
    public RenderTexture(int numBuffers, ShaderMaterial material, int width, int height) {
        this(numBuffers,material,width,height,Internal.RGB16);
    }
    
    public void render() {
    	Service service = gameObject().scene().service();
    	service.renderTexture(this);
    }
    
    public ShaderMaterial getMaterial() {
        return material;
    }
    
    public void setMaterial(ShaderMaterial material) {
        this.material = material;
    }
    
    public Texture getTexture(int i) {
        return buffers[i].getTexture(0);
    }
    
    public FrameBuffer[] buffers() {
    	return List.list(buffers).toArray(new FrameBuffer[buffers.length]);
    }
    
    public void setClearOnRender(boolean clearOnRender) {
        this.clearOnRender = clearOnRender;
    }
    
}
