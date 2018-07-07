package ax.engine.core.render;

import ax.engine.core.Camera;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Texture;
import ax.engine.core.Texture.Filter;
import ax.engine.core.Texture.Internal;
import ax.engine.core.Texture.Wrap;

public class CameraTarget extends Camera implements FBCamera {
    
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
    
    @Override
    public FrameBuffer buffer() {
    	return gBuffer;
    }
    
    
}
