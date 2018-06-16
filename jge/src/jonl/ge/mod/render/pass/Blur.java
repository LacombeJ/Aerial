package jonl.ge.mod.render.pass;

import jonl.ge.core.Camera;
import jonl.ge.core.FrameBuffer;
import jonl.ge.core.Material;
import jonl.ge.core.Texture;
import jonl.ge.core.TextureUniform;
import jonl.ge.core.Window;
import jonl.ge.core.Texture.Filter;
import jonl.ge.core.Texture.Internal;
import jonl.ge.core.Texture.Wrap;
import jonl.ge.core.material.GeneratedShader;
import jonl.ge.core.material.ShaderLanguage;
import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLTexU;
import jonl.ge.core.material.ShaderLanguage.SLVec2;
import jonl.ge.core.material.ShaderLanguage.SLVec4;
import jonl.ge.core.shaders.SLUtils;
import jonl.ge.mod.render.ImageEffect;
import jonl.vmath.Vector2;

public class Blur extends ImageEffect {

    FrameBuffer buffer;
    Texture blur;
    Material material;
    
    boolean horizontal;
    
    public Blur(boolean horizontal) {
        this.horizontal = horizontal;
        create();
    }
    
    public Blur() {
        this(true);
    }
    
    private void create() {
        buffer = new FrameBuffer(1024,576);
        blur = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        
        buffer.attach(blur);
        
        material = new GeneratedShader(vs(),fs());
    }
    
    public Texture blur() {
        return blur;
    }
    
    @Override
    public void input(Camera camera, Texture[] textures) {
        Window window = camera.window();
        float resolution = horizontal ? window.width() : window.height();
        float radius = 1f;
        Vector2 dir = horizontal ? new Vector2(1,0) : new Vector2(0,1);
        
        material.setUniform("texture",new TextureUniform(textures[0],0));
        material.setUniform("resolution",resolution);
        material.setUniform("radius",radius);
        material.setUniform("dir",dir);
    }

    @Override
    public Material material() {
        return material;
    }

    @Override
    public FrameBuffer buffer() {
        return buffer;
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
        
        SLTexU texture = sl.texture("texture");
        
        SLVec2 texCoord = SLUtils.flip(sl,sl.attributeIn(SLVec2.class, "vTexCoord"));
        
        SLFloat resolution = sl.slFloatu("resolution");
        SLFloat radius = sl.slFloatu("radius");
        SLVec2 dir = sl.vec2u("dir");
        
        SLVec4 sum = sl.vec4(0f);
        
        SLVec2 tc = sl.vec2(texCoord);
        
        SLFloat blur = sl.div(radius,resolution);
        
        SLFloat hstep = dir.x();
        SLFloat vstep = dir.y();
        
        float[] kernel = {
            0.0162162162f,
            0.0540540541f,
            0.1216216216f,
            0.1945945946f,
            0.2270270270f,
            0.1945945946f,
            0.1216216216f,
            0.0540540541f,
            0.0162162162f,
        };
        
        int hsize = kernel.length / 2;
        
        for (int i=-0; i<kernel.length; i++) {
            float f = i - hsize;
            float k = kernel[i];
            
            String vx = tc+".x - "+f+"*"+blur+"*"+hstep;
            String vy = tc+".y - "+f+"*"+blur+"*"+vstep;
            String v = "vec2("+vx+","+vy+")";
            String sample = "texture2D("+texture+","+v+") * "+k;
            
            sl.putStatement(sum+" += "+sample);
        }
        sl.gl_FragColor(sum);
        
        return sl;
    }

}
