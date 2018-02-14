package jonl.ge.base;

import jonl.ge.GameObject;
import jonl.ge.Scene;
import jonl.ge.Transform;

/**
 * Updates the scene
 * 
 * @author Jonathan Lacombe
 *
 */
public interface Updater {
    
    /** Loads before window is shown */
    void load();
    
    /** Create the scene */
    void create(Scene scene);
    
    /** Updates the scene */
    void update(Scene scene);
    
    /** @return the transform of an object in model space after update has been called */
    Transform getWorldTransform(GameObject g);
    
}
