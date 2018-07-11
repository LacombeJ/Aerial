package ax.std.render;

import ax.engine.core.Camera;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Material;
import ax.engine.core.Texture;
import ax.engine.core.render.FBCamera;
import ax.graphics.GL;

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
        texture = new Texture(buffer.width(),buffer.height(),GL.RGB16,GL.CLAMP,GL.LINEAR);
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
