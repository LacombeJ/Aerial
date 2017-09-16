package jonl.ge;

import jonl.ge.Texture.Filter;
import jonl.ge.Texture.Internal;
import jonl.ge.Texture.Wrap;

public class RenderTexture extends Component {
    
    ShaderMaterial material = null;
    FrameBuffer buffer = null;
    
    public RenderTexture(ShaderMaterial material, int width, int height, Internal internal) {
        this.material = material;
        
        Texture texture = new Texture(width,height,internal,Wrap.CLAMP,Filter.LINEAR);
        buffer = new FrameBuffer(texture);
    }
    
    public RenderTexture(ShaderMaterial material, int width, int height) {
        this(material,width,height,Internal.RGB16);
    }
    
    public void render() {
        AppRenderer renderer = (AppRenderer) getGameObject().getScene().application.getRenderer();
        renderer.renderRenderTexture(this);
    }
    
    public ShaderMaterial getMaterial() {
        return material;
    }
    
    public void setMaterial(ShaderMaterial material) {
        this.material = material;
    }
    
    public Texture getTexture() {
        return buffer.getTexture(0);
    }
    
    
}
