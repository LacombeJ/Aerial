package ax.examples.engine.grass;

import ax.engine.core.Material;
import ax.engine.core.SceneObject;
import ax.engine.core.Texture;
import ax.engine.core.Time;
import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLFloat;
import ax.engine.core.material.ShaderLanguage.SLVec2;
import ax.engine.core.render.RenderTexture;
import ax.engine.core.shaders.SLImports.PerlinNoise;

public class MovingGrassTexture {

    SceneObject sceneObject;
    Texture texture;
    float frequency = 0.005f;
    
    MovingGrassTexture() {
        create();
    }
    
    void create() {
        
        
        sceneObject = new SceneObject();
        
        Material material = shader();
        
        RenderTexture rt = new RenderTexture(1, material, 1024, 1024);
        
        sceneObject.addComponent(rt);
        sceneObject.addUpdate(()->{
            rt.render();
            
            Time time = rt.time();
            float msdiff = time.ms();
            float sdiff = msdiff / 1000.0f;
            material.setUniform("time",sdiff);
            material.setUniform("frequency",frequency);
        });
        
        texture = rt.getTexture(0);
        
    }
    
    Material shader() {
        return new GeneratedShader(vs(),fs());
    }
    
    ShaderLanguage vs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");

        sl.uniform("mat4 MVP");
        
        sl.putStatement("gl_Position = MVP * vertex");
        return sl;
    }
    
    
    
    ShaderLanguage fs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        SLFloat time = sl.slFloatu("time");
        
        PerlinNoise perlin = sl.include(new PerlinNoise());
        
        SLVec2 p = sl.gl_FragCoord.xy();
        
        SLFloat frequency = sl.slFloatu("frequency",this.frequency);
        
        sl.mul(p.x(),frequency);
        sl.add(sl.mul(p.x(),frequency),time);
        SLFloat x = sl.add(sl.mul(p.x(),frequency), time);
        SLFloat y = sl.add(sl.mul(p.y(),frequency), time);
        SLFloat z = time;
        
        SLFloat noise = sl.call(perlin.noise,x,y,z);
        noise = sl.slFloat("("+noise+" + 1.0) / 2.0");
        
        sl.gl_FragColor(sl.vec4(noise,noise,noise,1f));
        
        return sl;
    }
    
}
