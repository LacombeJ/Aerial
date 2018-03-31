package jonl.ge.core.app;

import jonl.ge.core.Window;
import jonl.vmath.Vector2;

public class ApplicationWindow implements Window {

    private AbstractApplication app;
    
    public ApplicationWindow(AbstractApplication app) {
        this.app = app;
    }
    
    @Override
    public int width() {
        return app.getWidth();
    }
    
    @Override
    public int height() {
        return app.getHeight();
    }
    
    public Vector2 dimension() {
        return new Vector2(width(),height());
    }
    
    public float aspect() {
        return (float)width() / height();
    }
    
    public Vector2 toUnitSpace(Vector2 screen) {
        return screen.get().divide(dimension());
    }
    
    public Vector2 toNormSpace(Vector2 screen) {
        return toUnitSpace(screen).multiply(2).sub(1);
    }
    
    @Override
    public void close() {
        app.close();
    }
    
}
