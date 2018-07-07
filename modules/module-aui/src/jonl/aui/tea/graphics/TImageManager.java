package jonl.aui.tea.graphics;

import jonl.jgl.GL;
import jonl.jgl.Texture;
import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;
import jonl.jutils.structs.Pool;

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
        return gl.glGenTexture(image.image(), Internal.RGBA16, Wrap.CLAMP, Filter.NEAREST);
    }
    
    private void destroyTexture(Texture texture) {
        texture.delete();
    }
    
}
