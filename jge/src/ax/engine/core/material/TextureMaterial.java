package ax.engine.core.material;

import java.util.ArrayList;
import java.util.List;

import ax.engine.core.Material;
import ax.engine.core.Texture;
import ax.engine.core.TextureUniform;
import ax.engine.utils.PresetData;

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
    protected List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        uniforms.add(new Uniform("texture",new TextureUniform(texture,0)));
        return uniforms;
    }

    @Override
    protected String shaderKey() {
        return "_texture_";
    }

	@Override
	protected String vertexShader(int version) {
		return PresetData.basicVSSource(version);
	}

	@Override
	protected String geometryShader(int version) {
		return null;
	}

	@Override
	protected String fragmentShader(int version) {
		return PresetData.basicFSSource(version);
	}
    
}
