package ax.std.render.pass;

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
import ax.engine.core.material.ShaderLanguage.SLFloat;
import ax.engine.core.material.ShaderLanguage.SLInt;
import ax.engine.core.material.ShaderLanguage.SLMat3;
import ax.engine.core.material.ShaderLanguage.SLTexU;
import ax.engine.core.material.ShaderLanguage.SLVec2;
import ax.engine.core.material.ShaderLanguage.SLVec3;
import ax.engine.core.material.ShaderLanguage.SLVec4;
import ax.engine.core.shaders.SLUtils;
import ax.engine.utils.GLUtils;
import ax.graphics.Program;
import ax.math.vector.Mathf;
import ax.math.vector.Vector3;
import ax.std.render.ImageEffect;

public class SSAO extends ImageEffect {

    FrameBuffer buffer;
    Texture ssao;
    Material material;
    
    private final int ssaoKernelSize = 8;
    private final int ssaoNoiseSize = 4;
    
    private final Vector3[] ssaoKernel;
    private final Vector3[] ssaoNoise;
    
    private final Texture noiseTexture;
    
    public SSAO() {
        ssaoKernel = new Vector3[ssaoKernelSize*ssaoKernelSize];
        //TODO try to multiply i * (1/64f) ? performance vs accuracy >
        for (int i=0; i<ssaoKernel.length; i++) {
            Vector3 sample = new Vector3(
                Mathf.rand() * 2f - 1f,
                Mathf.rand() * 2f - 1f,
                Mathf.rand()
            );
            sample.normalize();
            sample.scale(Mathf.rand());
            float scale = i / (float)ssaoKernel.length;
            scale = Mathf.lerp(scale*scale,0.1f,1f);
            sample.scale(scale);
            ssaoKernel[i] = sample;
        }
        ssaoNoise = new Vector3[ssaoNoiseSize*ssaoNoiseSize];
        for (int i=0; i<ssaoNoise.length; i++) {
            Vector3 noise = new Vector3(
                Mathf.rand() * 2f - 1f,
                Mathf.rand() * 2f - 1f,
                0
            );
            ssaoNoise[i] = noise;
        }
        noiseTexture = new Texture(Vector3.pack(ssaoNoise),
                ssaoNoiseSize,ssaoNoiseSize,Internal.RGB16,Wrap.REPEAT,Filter.NEAREST);
        
        create();
    }
    
    private void create() {
        buffer = new FrameBuffer(1024,576);
        ssao = new Texture(buffer.width(),buffer.height(),Internal.RGB16F,Wrap.CLAMP,Filter.LINEAR);
        
        buffer.attach(ssao);
        
        material = new GeneratedShader(vs(),fs());
    }
    
    public Texture ssao() {
        return ssao;
    }
    
    @Override
    public void input(Camera camera, Texture[] textures) {
        material.setUniform("gPosition",new TextureUniform(textures[0],0));
        material.setUniform("gNormal",new TextureUniform(textures[1],1));
        material.setUniform("texNoise",new TextureUniform(noiseTexture,2));
        material.setUniform("gStencil",new TextureUniform(textures[2],3));
        
        //TODO remove service getOrCreate method and modify shader material to handle arrays
        Program program = camera.service().getOrCreateProgram(material);
        
        camera.service().getGL().glUseProgram(program);
        
        GLUtils.setUniform(program,"samples",ssaoKernel);
        GLUtils.setUniform(program,"P",camera.getProjection());
        GLUtils.setUniform(program,"V",camera.computeViewMatrix());
        
        camera.service().getGL().glUseProgram(null);
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
        return SLUtils.basicVert();
    }
    
    private ShaderLanguage fs() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        int kernelSize = 64;
        float radius = 0.5f;
        float bias = 0.025f;
        
        SLTexU gPosition = sl.texture("gPosition");
        SLTexU gNormal = sl.texture("gNormal");
        SLTexU texNoise = sl.texture("texNoise");
        SLTexU gStencil = sl.texture("gStencil");
        
        SLVec2 texCoord = SLUtils.flip(sl,sl.attributeIn(SLVec2.class, "vTexCoord"));
        
        sl.uniform("vec3 samples[64]");
        sl.uniform("mat4 V");
        sl.uniform("mat4 P");
        
        SLVec2 noiseScale = sl.vec2c(1024f/4, 576/4);
        
        SLVec4 sampleFragPos = sl.sample(gPosition, texCoord);
        SLVec3 fragPos = sl.vec3("(V * "+sampleFragPos+").xyz");
        
        SLVec3 sampleNormal = sl.normalize(sl.sample(gNormal, texCoord).xyz());
        SLVec3 normal = sl.vec3("inverse(transpose(mat3(V))) * "+sampleNormal);
        
        SLVec3 randomVec = sl.sample(texNoise, sl.mul(texCoord,noiseScale)).xyz();
        
        SLVec3 tangent = sl.normalize(sl.sub(randomVec,sl.mul(normal,sl.dot(randomVec,normal))));
        SLVec3 bitangent = sl.normalize(sl.cross(normal, tangent));
        SLMat3 TBN = sl.mat3(tangent, bitangent, normal);
        
        SLFloat occlusion = sl.slFloat(0f);
        
        SLInt i = sl.slLoop(0,kernelSize,1); {
            
            SLVec3 sample = sl.vec3(TBN+" * samples["+i+"]");
            sample = sl.add(fragPos,sl.mul(sample,radius));
            
            SLVec4 offset = sl.vec4(sample, 1.0f);
            sl.putStatement(offset+"      = P * "+offset);
            sl.putStatement(offset+".xyz /= "+offset+".w");
            sl.putStatement(offset+".xyz  = "+offset+".xyz * 0.5 + 0.5");
            
            SLVec4 sampleDepthVec = sl.sample(gPosition,offset.xy());
            SLFloat sampleDepth = sl.slFloat("(V * "+sampleDepthVec+").z");
            
            // Using stencil buffer to ensure edges look nice
            SLVec4 sampleStencilVec = sl.sample(gStencil,offset.xy());
            sl.slIf(sl.equals(sampleStencilVec.x(),0f));
            sl.set(sampleDepth,sl.slFloat(1));
            sl.slEndIf();
            
            sl.putStatement(occlusion+" += ("+sampleDepth+" >= "+sample+".z + "+bias+" ? 1.0 : 0.0)");
            
        }
        sl.slEndLoop();
        
        sl.putStatement(occlusion+" = 1 - ("+occlusion+" / 16)");
        
        sl.gl_FragColor(sl.vec4(occlusion,occlusion,occlusion,1));
        
        return sl;
    }

}
