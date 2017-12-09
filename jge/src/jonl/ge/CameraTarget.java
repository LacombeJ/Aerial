package jonl.ge;

import jonl.ge.Texture.Filter;
import jonl.ge.Texture.Internal;
import jonl.ge.Texture.Wrap;

public class CameraTarget extends Camera {
    
    FrameBuffer gBuffer = null;
    
    public CameraTarget(int width, int height) {
        gBuffer = new FrameBuffer(width,height);
    }
    
    public void addTexture(Texture t) {
    	gBuffer.attach(t);
    }
    
    public void addTexture() {
    	addTexture(new Texture(gBuffer.width,gBuffer.height,Internal.RGB16,Wrap.CLAMP,Filter.LINEAR));
    }
    
    public Texture getTexture(int type) {
        return gBuffer.getTexture(type);
    }
    
    
}
