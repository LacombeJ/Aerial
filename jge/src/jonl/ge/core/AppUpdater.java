package jonl.ge.core;

import java.util.HashMap;

import jonl.ge.base.Updater;
import jonl.jutils.func.List;

class AppUpdater implements Updater {

	private AbstractApp app;
    private HashMap<GameObject,Transform> multiplied = new HashMap<>();
    
    AppUpdater(AbstractApp app) {
        this.app = app;
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
    	ModuleUpdate.preUpdate(app.getModule(ModuleUpdate.class));
        scene.update();
        ModuleUpdate.postUpdate(app.getModule(ModuleUpdate.class));
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
        scene.root.iterate((g) -> recurseTransform(g,new Transform()));
    }
    
    private void recurseTransform(GameObject gameObject, Transform transform) {
        //TODO dont compute matrix for static objects?
        //TODO keep premultiplied matrices for static objects and static children?
    	ModuleUpdate.update(app.getModule(ModuleUpdate.class), gameObject, transform.get());
        Transform mult = gameObject.transform.get().multiply(transform);
        multiplied.put(gameObject,mult);
        gameObject.iterate((g) -> recurseTransform(g,mult));
        gameObject.children();
    }
    

}
