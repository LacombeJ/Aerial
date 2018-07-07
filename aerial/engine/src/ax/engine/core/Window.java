package ax.engine.core;

import ax.math.vector.Vector2;

public interface Window {

    public int width();
    
    public int height();
    
    /** @return Vector2(width,height) */
    public Vector2 dimension();
    
    /** @return aspect ratio of window (width/height) */
    public float aspect();
    
    /** @return screen coordinates in unit space (0,1) */
    public Vector2 toUnitSpace(Vector2 screen);
    
    /** @return screen coordinates in normalized device coordinate (NDC) space (-1,1) */
    public Vector2 toNormSpace(Vector2 screen);
    
    /** Closes application window */
    public void close();
    
}
