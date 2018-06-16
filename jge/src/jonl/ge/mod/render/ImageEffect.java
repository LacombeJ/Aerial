package jonl.ge.mod.render;

import jonl.ge.core.Camera;
import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Material;
import jonl.ge.core.Texture;

public abstract class ImageEffect {

    public abstract void input(Camera camera, Texture[] textures);

    public abstract Material material();

    public abstract FrameBuffer buffer();
    
    public void preRender(Camera camera) {
        
    }
    
    public void postRender(Camera camera) {
        
    }
    
}
