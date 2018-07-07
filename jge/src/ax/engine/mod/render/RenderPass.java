package ax.engine.mod.render;

import java.util.ArrayList;

import ax.commons.func.Function0D;
import ax.commons.func.Tuple2;
import ax.engine.core.Material;
import ax.engine.core.Texture;
import ax.engine.core.TextureUniform;
import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLTexU;

public class RenderPass {

    static class Pass extends Tuple2<ImageEffect,Function0D<Texture[]>> {
        public Pass(ImageEffect x, Function0D<Texture[]> y) {
            super(x, y);
        }
    }
    
    ArrayList<Pass> passes = new ArrayList<>();
    Function0D<Texture> finish = null;
    Material material;
    
    public RenderPass() {
        material = build();
    }
    
    public <T extends ImageEffect> T attach(T effect, Function0D<Texture[]> textures) {
        Pass pass = new Pass(effect,textures);
        passes.add(pass);
        return effect;
    }
    
    public void finish(Function0D<Texture> texture) {
        finish = texture;
    }
    
    void prepareFinish() {
        material.setUniform("texture",new TextureUniform(finish.f(),0));
    }
    
    private Material build() {
        return new GeneratedShader(vs(),fs());
    }
    
    private ShaderLanguage vs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        sl.layoutIn(1,"vec3 normal");
        sl.layoutIn(2,"vec2 texCoord");
        
        sl.uniform("mat4 MVP");
        
        sl.attributeOut("vec2 vTexCoord");
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.putStatement("vTexCoord = texCoord");
        
        return sl;
    }
    
    private ShaderLanguage fs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.attributeIn("vec2 vTexCoord");
        
        SLTexU texture = sl.texture("texture");

        // Flipped for texture rendering
        sl.putStatement("gl_FragColor = texture2D("+texture+",vec2(vTexCoord.x, 1 - vTexCoord.y))");
        
        return sl;
    }

}
