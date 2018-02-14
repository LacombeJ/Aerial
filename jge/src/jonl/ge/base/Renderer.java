package jonl.ge.base;

import jonl.ge.core.Scene;

/**
 * Handles rendering a scene and should not modify the scene
 * 
 * @author Jonathan Lacombe
 *
 */
public interface Renderer {
    
    /** Loads before window is shown */
    void load();

    /** Renders the scene without modifying any data in the scene */
    void render(Scene scene);
    
}
