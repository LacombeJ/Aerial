package ax.std.misc;

import ax.commons.io.FileUtils;
import ax.engine.core.Texture;
import ax.engine.core.Transform;
import ax.engine.core.render.CameraExtension;
import ax.engine.core.render.RenderTarget;
import ax.graphics.GL;
import ax.math.vector.Mathf;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector3;

/**
 * https://learnopengl.com/#!Advanced-Lighting/SSAO
 * 
 * @author Jonathan Lacombe
 *
 */
public class MYSSAO extends CameraExtension {
    
    private final int ssaoKernelSize = 8;
    private final int ssaoNoiseSize = 4;
    
    private final Vector3[] ssaoKernel;
    private final Vector3[] ssaoNoise;
    
    private final Texture noiseTexture;
    
    public MYSSAO() {
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
                ssaoNoiseSize,ssaoNoiseSize,GL.RGB16,GL.REPEAT,GL.NEAREST);
    }
    
    @Override
    public void create() { }

    @Override
    public void render() {
        RenderTarget camera = getComponent(RenderTarget.class);
        setTexture(camera.getTexture(RenderTarget.POSITION_TEXTURE),"gPosition");
        setTexture(camera.getTexture(RenderTarget.NORMALS_TEXTURE),"gNormal");
        setTexture(noiseTexture,"texNoise");
        setVec3Array(ssaoKernel,"samples");
        setMat4(camera.getProjection(),"projection");
        Transform t = camera.computeWorldTransform();
        Matrix4 view = Matrix4.identity()
                .rotate(t.rotation)
                .translate(t.translation.get().neg());
        setMat4(view,"view");
    }

    @Override
    public String shader() { //TODO hardcode shader
        return FileUtils.readFromFile("res/shaders/myssao.frag").toString();
    }
    
}
