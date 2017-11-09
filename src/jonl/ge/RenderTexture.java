package jonl.ge;

import jonl.ge.Texture.Filter;
import jonl.ge.Texture.Internal;
import jonl.ge.Texture.Wrap;

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
        AppRenderer renderer = (AppRenderer) getGameObject().getScene().application.getRenderer();
        renderer.renderRenderTexture(this);
        translate();
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
    
    public void setClearOnRender(boolean clearOnRender) {
        this.clearOnRender = clearOnRender;
    }
    
    private void translate() {
        // [ 0, 1, 2 ] -> [ 1, 2, 0 ]
        FrameBuffer first = buffers[0];
        for (int i=0; i<buffers.length-1; i++) {
            buffers[i] = buffers[i + 1];
        }
        buffers[buffers.length-1] = first;
    }
    
    
}
