package jonl.ge.mod.misc;

import jonl.ge.core.render.CameraExtension;
import jonl.ge.core.render.RenderTarget;
import jonl.jutils.io.FileUtils;
import jonl.vmath.Mathf;
import jonl.vmath.Vector3;

/**
 * https://learnopengl.com/#!Advanced-Lighting/SSAO
 * 
 * @author Jonathan Lacombe
 *
 */
public class Blur extends CameraExtension {
    
    private final int ssaoKernelSize = 8;
    private final int ssaoNoiseSize = 4;
    
    private final Vector3[] ssaoKernel;
    private final Vector3[] ssaoNoise;
    
    public Blur() {
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
    }
    
    @Override
    public void create() { }

    @Override
    public void render() {
        RenderTarget camera = getComponent(RenderTarget.class);
        setTexture(camera.getTexture(RenderTarget.RENDER_TEXTURE),"texture");
        
    }

    @Override
    public String shader() { //TODO hardcode shader
        return FileUtils.readFromFile("res/shaders/myssao.frag").toString();
    }
    
}
