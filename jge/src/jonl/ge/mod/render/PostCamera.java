package jonl.ge.mod.render;

import jonl.ge.core.Camera;
import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Material;
import jonl.ge.core.Texture;
import jonl.ge.core.Texture.Filter;
import jonl.ge.core.Texture.Internal;
import jonl.ge.core.Texture.Wrap;
import jonl.ge.core.render.FBCamera;

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
