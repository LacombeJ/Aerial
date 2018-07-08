package ax.std.misc;

import ax.engine.core.Input;
import ax.engine.core.Material;
import ax.engine.core.Property;
import ax.engine.core.Time;
import ax.engine.core.material.ShaderMaterial;
import ax.math.vector.Vector2;
import ax.math.vector.Vector4;

public class ShaderToy extends Property {

    private Material material;
    private int width, height;
    
    private float mouseX, mouseY;
    private float mouseClickX, mouseClickY;
    
    public ShaderToy(ShaderMaterial material, int width, int height) {
        this.material = material;
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        
    }

    @Override
    public void update() {
        
        Time time = time();
        float msdiff = time.ms();
        float sdiff = msdiff / 1000.0f;
        
        Input input = input();
        if (input.isButtonPressed(Input.MB_LEFT)) {
            mouseClickX = input.getX();
            mouseClickY = input.getY();
        }
        if (input.isButtonDown(Input.MB_LEFT)) {
            mouseX = input.getX();
            mouseY = input.getY();
        }
        
        material.setUniform("iTime", sdiff);
        material.setUniform("iResolution", new Vector2(width, height));
        material.setUniform("iMouse", new Vector4(mouseX, mouseY, mouseClickX, mouseClickY));
        
    }
    
}
