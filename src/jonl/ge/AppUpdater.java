package jonl.ge;

import java.util.HashMap;

class AppUpdater implements Updater {

    private HashMap<GameObject,Transform> multiplied = new HashMap<>();
    
    AppUpdater() {
        
    }
    
    @Override
    public void load() {
        
    }
    
    @Override
    public void create(Scene scene) {
        scene.create();
    }
    
    @Override
    public void update(Scene scene) {
        scene.update();
        recurseTransform(scene);
    }

    @Override
    public Transform getWorldTransform(GameObject g) {
        return multiplied.get(g);
    }
    
    /**
     * Multiplies the transforms in a scene and saves them in a hashmap
     * @param scene
     */
    private void recurseTransform(Scene scene) {
        //TODO keep premultiplied transforms for static objects and objects not moving
        multiplied = new HashMap<>();
        for (GameObject g : scene.gameObjects) {
            recurseTransform(g,new Transform());
        }
    }
    
    private void recurseTransform(GameObject gameObject, Transform transform) {
        //TODO dont compute matrix for static objects?
        //TODO keep premultiplied matrices for static objects and static children?
        Transform mult = gameObject.transform.get().multiply(transform);
        multiplied.put(gameObject,mult);
        for (GameObject g : gameObject.children) {
            recurseTransform(g,mult);
        }
    }
    

}
