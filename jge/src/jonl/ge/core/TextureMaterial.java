package jonl.ge.core;

import java.util.ArrayList;
import java.util.List;

public class TextureMaterial extends Material {

    private Texture texture;
    
    public TextureMaterial(Texture texture) {
       this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
    
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void setUniform(String name, Object object) {
        if (name=="texture") {
            this.texture = (Texture) object;
        }
    }
    
    @Override
    List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        uniforms.add(new Uniform("texture",new TextureUniform(texture,0)));
        return uniforms;
    }

    @Override
    String shaderKey() {
        return "_texture_";
    }
    
}
