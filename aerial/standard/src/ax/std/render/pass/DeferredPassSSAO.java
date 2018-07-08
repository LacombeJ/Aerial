package ax.std.render.pass;

import java.util.ArrayList;

import ax.commons.func.Function;
import ax.engine.core.Camera;
import ax.engine.core.FrameBuffer;
import ax.engine.core.Material;
import ax.engine.core.Texture;
import ax.engine.core.TextureUniform;
import ax.engine.core.Texture.Filter;
import ax.engine.core.Texture.Internal;
import ax.engine.core.Texture.Wrap;
import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLInt;
import ax.engine.core.material.ShaderLanguage.SLTexU;
import ax.engine.core.material.ShaderLanguage.SLVec2;
import ax.engine.core.material.ShaderLanguage.SLVec3;
import ax.math.vector.Vector3;
import ax.std.render.ImageEffect;
import ax.std.render.Light;
import ax.std.render.StandardMaterialUtil;
import ax.std.render.StandardMaterialUtil.SLLight;

public class DeferredPassSSAO extends ImageEffect {

    FrameBuffer buffer;
    Texture texture;
    Material material;
    
    public DeferredPassSSAO() {
        create();
    }
    
    private void create() {
        buffer = new FrameBuffer(1024,576);
        texture = new Texture(buffer.width(),buffer.height(),Internal.RGB16,Wrap.CLAMP,Filter.LINEAR);
        
        buffer.attach(texture);
        
        material = new GeneratedShader(vs(),fs());
    }
    
    public Texture texture() {
        return texture;
    }
    
    @Override
    public void input(Camera camera, Texture[] textures) {
        ArrayList<Light> lights = camera.scene().findComponents(Light.class);
        
        material.setUniform("gPosition",new TextureUniform(textures[0],0));
        material.setUniform("gNormal",new TextureUniform(textures[1],1));
        material.setUniform("gTexCoord",new TextureUniform(textures[2],2));
        material.setUniform("gStencil",new TextureUniform(textures[3],3));
        material.setUniform("ssao",new TextureUniform(textures[4],4));
        
        Vector3 eye = camera.service().getWorldTransform(camera.sceneObject()).translation;
        
        material.setUniform("eye",eye);
        
        int numLights = 0;
        for (int i=0; i<lights.size(); i++) {
            Light light = lights.get(i);
            Vector3 p = camera.service().getWorldTransform(light.sceneObject()).translation;
            
            material.setUniform("light["+i+"].position",p.get());
            
            material.setUniform("light["+i+"].type",light.getType());
            material.setUniform("light["+i+"].color",light.getColor());
            material.setUniform("light["+i+"].ambient",light.getAmbient());
            material.setUniform("light["+i+"].falloff",light.getFalloff());
            material.setUniform("light["+i+"].radius",light.getRadius());
            material.setUniform("light["+i+"].direction",light.getDirection());
            
            numLights++;
        }
        material.setUniform("numLights",numLights);
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
        
        SLTexU gPosition = sl.texture("gPosition");
        SLTexU gNormal = sl.texture("gNormal");
        sl.texture("gTexCoord");
        SLTexU gStencil = sl.texture("gStencil");
        SLTexU ssao = sl.texture("ssao");
        
        SLVec2 texCoordOrig = sl.attributeIn(SLVec2.class, "vTexCoord");
        SLVec2 viewTexCoord = sl.vec2(texCoordOrig.x(), sl.sub(1,texCoordOrig.y()));
        
        SLVec3 vPosition = sl.sample(gPosition, viewTexCoord).xyz();
        SLVec3 vNormal = sl.sample(gNormal, viewTexCoord).xyz();
        SLVec3 vStencil = sl.sample(gStencil, viewTexCoord).xyz();
        SLVec3 ssaoSample = sl.sample(ssao, viewTexCoord).xyz();
        
        vNormal = sl.normalize(vNormal);
        
        SLInt numLights = sl.slIntu("numLights");
        SLVec3 eye = sl.vec3u("eye");
        
        SLVec3 fDiffuse = sl.vec3(0.5f);
        SLVec3 fNormal = vNormal;
        SLVec3 fSpecular = sl.vec3(0.5f);
        
        sl.slIf(sl.equals(vStencil.x(),0f));
        sl.discard();
        sl.slEndIf();
        
        Function<SLLight,SLVec3> ambientf = (light) -> {
            return sl.mul(light.ambient,ssaoSample);
        };
        
        StandardMaterialUtil.fragment(sl,vPosition,eye,numLights,fDiffuse,fNormal,fSpecular,ambientf);
        
        return sl;
    }
    
}
