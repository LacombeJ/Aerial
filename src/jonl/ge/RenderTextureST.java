package jonl.ge;

import jonl.jutils.time.Clock;
import jonl.vmath.Vector2;
import jonl.vmath.Vector4;

/**
 * RenderTexture with ShaderToy uniforms sent
 * 
 * @author Jonathan Lacombe
 *
 */
public class RenderTextureST extends RenderTexture {
    
    private float mouseX, mouseY;
    private float mouseClickX, mouseClickY;
    
    public RenderTextureST(ShaderMaterial material, int width, int height) {
        super(material, width, height);
    }

    @Override
    void updateComponent() {
        super.updateComponent();
        
        Clock clock = getClock();
        float msdiff = clock.lap() / 1000000.0f;
        float sdiff = msdiff / 1000.0f;
        
        Input input = getInput();
        if (input.isButtonPressed(Input.MB_LEFT)) {
            mouseClickX = input.getX();
            mouseClickY = input.getY();
        }
        if (input.isButtonDown(Input.MB_LEFT)) {
            mouseX = input.getX();
            mouseY = input.getY();
        }
        
        material.setUniform("iTime", sdiff);
        material.setUniform("iResolution", new Vector2(buffer.width, buffer.height));
        material.setUniform("iMouse", new Vector4(mouseX, mouseY, mouseClickX, mouseClickY));
        
    }
    
    
}
