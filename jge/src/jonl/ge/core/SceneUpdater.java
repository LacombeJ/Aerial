package jonl.ge.core;

import java.util.HashMap;

import jonl.jutils.func.List;

class SceneUpdater {
	
    private SceneManager manager;
    
	private HashMap<GameObject,Transform> worldTransformMap = new HashMap<>();
	
	public SceneUpdater(SceneManager manager, Service service) {
	    
	    this.manager = manager;
	    
	    service.implementGetWorldTransform( (g) -> getWorldTransform(g) );
	    
	}
	
	void update(Scene scene) {
		scene.update();
		recurseTransform(scene);
	}
	
    Transform getWorldTransform(GameObject g) {
        return worldTransformMap.get(g);
    }
    
    /**
     * Multiplies the transforms in a scene and saves them in a hashmap
     * @param scene
     */
    private void recurseTransform(Scene scene) {
        //TODO keep premultiplied transforms for static objects and objects not moving
        worldTransformMap = new HashMap<>();
        scene.root.iterate((g) -> recurseTransform(g,new Transform()));
    }
    
    private void recurseTransform(GameObject gameObject, Transform transform) {
        //TODO dont compute matrix for static objects?
        //TODO keep premultiplied matrices for static objects and static children?
        List.iterate(manager.delegate().onParentWorldTransformUpdate(), (cb) -> cb.f((GameObject) gameObject, transform) );
        Transform mult = gameObject.transform.get().multiply(transform);
        worldTransformMap.put(gameObject,mult);
        gameObject.iterate((g) -> recurseTransform(g,mult));
    }
	
}
