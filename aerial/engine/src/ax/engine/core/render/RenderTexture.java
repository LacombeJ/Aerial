package ax.engine.core.render;

import ax.commons.func.List;
import ax.engine.core.Component;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Material;
import ax.engine.core.Service;
import ax.engine.core.Texture;
import ax.graphics.GL;

public class RenderTexture extends Component {
    
    Material material = null;
    FrameBuffer[] buffers = null;
    boolean clearOnRender = true;
    
    public RenderTexture(int numBuffers, Material material, int width, int height, GL.Internal internal, GL.Wrap wrap, GL.Filter filter) {
        this.material = material;
        
        buffers = new FrameBuffer[numBuffers];
        for (int i=0; i<buffers.length; i++) {
            Texture texture = new Texture(width,height,internal,wrap,filter);
            FrameBuffer buffer = new FrameBuffer(texture);
            buffers[i] = buffer;
        }
        
    }
    
    public RenderTexture(int numBuffers, Material material, int width, int height, GL.Internal internal) {
        this(numBuffers,material,width,height,GL.RGB16,GL.CLAMP,GL.NEAREST);
    }
    
    public RenderTexture(int numBuffers, Material material, int width, int height) {
        this(numBuffers,material,width,height,GL.RGB16);
    }
    
    public void render() {
    	Service service = sceneObject().scene().service();
    	service.renderTexture(this);
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public void setMaterial(Material material) {
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
