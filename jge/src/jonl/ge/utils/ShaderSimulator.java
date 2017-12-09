package jonl.ge.utils;

import jonl.jutils.func.Callback0D;
import jonl.jutils.image.Frame;
import jonl.jutils.image.Image;

public class ShaderSimulator {

    private PseudoShader shader;
    private Image image;
    private Callback0D uniformCallback;
    
    public ShaderSimulator(PseudoShader shader, int width, int height) {
        
        this.shader = shader;
        image = new Image(width,height);
        
    }
    
    public void simulate() {
        Frame.imshow("image", image);
        
        while (true) {
            
            if (uniformCallback != null) {
                uniformCallback.f();
            }
            
            for (int i=0; i<image.width(); i++) {
                for (int j=0; j<image.height(); j++) {
                    float x = i / (float)image.width();
                    float y = (image.height()-j) / (float)image.height(); // y is flipped to match gl coord
                    shader.gl_FragCoord.x = x * 2 - 1;
                    shader.gl_FragCoord.y = y * 2 - 1;
                    shader.gl_FragCoord.z = 0;;
                    shader.gl_FragCoord.w = 1;
                    shader.main();
                    int r = (int) (shader.gl_FragColor.x*255);
                    int g = (int) (shader.gl_FragColor.y*255);
                    int b = (int) (shader.gl_FragColor.z*255);
                    int a = (int) (shader.gl_FragColor.w*255);
                    image.setRGBA(i, j, r, g, b, a);
                }
            }
            
            Frame.imshow("image", image);
            
        }
    }
    
    public void setUniformCallback(Callback0D callback) {
        uniformCallback = callback;
    }
    
}
