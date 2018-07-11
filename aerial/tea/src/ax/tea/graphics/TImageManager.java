package ax.tea.graphics;

import ax.commons.structs.Pool;
import ax.graphics.GL;
import ax.graphics.Texture;

public class TImageManager {

    GL gl;
    
    Pool<TImage,Texture> textMap;
    
    public TImageManager(GL gl) {
        
        this.gl = gl;
        
        textMap = new Pool<>((i) -> createTexture(i), (t) -> destroyTexture(t));
        
    }
    
    public Texture getOrCreateTexture(TImage image) {
        return textMap.get(image);
    }
    
    private Texture createTexture(TImage image) {
        return gl.glGenTexture(image.image(), GL.RGBA16, GL.CLAMP, GL.NEAREST);
    }
    
    private void destroyTexture(Texture texture) {
        texture.delete();
    }
    
}
