package ax.std.render;

import ax.engine.core.Camera;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Material;
import ax.engine.core.Texture;
import ax.engine.core.Texture.Filter;
import ax.engine.core.Texture.Internal;
import ax.engine.core.Texture.Wrap;
import ax.engine.core.render.FBCamera;

public class PostCamera extends Camera implements FBCamera {

    FrameBuffer buffer = null;
    Texture texture = null;
    Material material;
    
    public PostCamera(int width, int height, Material material) {
        resize(width,height);
        this.material = material;
    }
    
    void resize(int width, int height) {
        //TODO delete previous buffer if not null
        buffer = new FrameBuffer(width,height);
        texture = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        buffer.attach(texture);
    }
    
    public Texture texture() {
        return texture;
    }
    
    @Override
    public FrameBuffer buffer() {
        return buffer;
    }

}
