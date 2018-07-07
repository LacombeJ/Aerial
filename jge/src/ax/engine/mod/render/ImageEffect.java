package ax.engine.mod.render;

import ax.engine.core.Camera;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Material;
import ax.engine.core.Texture;

public abstract class ImageEffect {

    public abstract void input(Camera camera, Texture[] textures);

    public abstract Material material();

    public abstract FrameBuffer buffer();
    
    public void preRender(Camera camera) {
        
    }
    
    public void postRender(Camera camera) {
        
    }
    
}
