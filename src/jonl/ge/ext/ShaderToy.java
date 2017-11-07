package jonl.ge.ext;

import jonl.ge.Input;
import jonl.ge.Material;
import jonl.ge.Property;
import jonl.ge.ShaderMaterial;
import jonl.ge.Time;
import jonl.vmath.Vector2;
import jonl.vmath.Vector4;

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
        
        Time time = getTime();
        float msdiff = time.time();
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
        material.setUniform("iResolution", new Vector2(width, height));
        material.setUniform("iMouse", new Vector4(mouseX, mouseY, mouseClickX, mouseClickY));
        
    }
    
}
