package jonl.ge.core.render;

import jonl.ge.core.Camera;
import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Texture;
import jonl.ge.core.Texture.Filter;
import jonl.ge.core.Texture.Internal;
import jonl.ge.core.Texture.Wrap;

public class CameraTarget extends Camera {
    
    FrameBuffer gBuffer = null;
    
    public CameraTarget(int width, int height) {
        gBuffer = new FrameBuffer(width,height);
    }
    
    public void addTexture(Texture t) {
    	gBuffer.attach(t);
    }
    
    public void addTexture() {
    	addTexture(new Texture(gBuffer.width(),gBuffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR));
    }
    
    public Texture getTexture(int type) {
        return gBuffer.getTexture(type);
    }
    
    public FrameBuffer buffer() {
    	return gBuffer;
    }
    
    
}
