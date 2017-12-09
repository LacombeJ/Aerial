package jonl.ge;

import jonl.vmath.Vector2;

public abstract class AbstractWindow implements Window {
    
    public float aspect() {
        return (float)width() / height();
    }
    
    public Vector2 dimension() {
        return new Vector2(width(),height());
    }
    
    public Vector2 toUnitSpace(Vector2 screen) {
        return screen.get().divide(dimension());
    }
    
    public Vector2 toNormSpace(Vector2 screen) {
        return toUnitSpace(screen).multiply(2).sub(1);
    }
    
}
