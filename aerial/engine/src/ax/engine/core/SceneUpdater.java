package ax.engine.core;

import java.util.HashMap;

import ax.commons.func.List;

class SceneUpdater {
	
    private SceneManager manager;
    
	private HashMap<SceneObject,Transform> worldTransformMap = new HashMap<>();
	
	public SceneUpdater(SceneManager manager, Service service) {
	    
	    this.manager = manager;
	    
	    service.implementGetWorldTransform( (o) -> getWorldTransform(o) );
	    
	}
	
	void update(Scene scene) {
		scene.update();
		recurseTransform(scene);
	}
	
    Transform getWorldTransform(SceneObject o) {
        return worldTransformMap.get(o);
    }
    
    /**
     * Multiplies the transforms in a scene and saves them in a hashmap
     * @param scene
     */
    private void recurseTransform(Scene scene) {
        //TODO keep premultiplied transforms for static objects and objects not moving
        worldTransformMap = new HashMap<>();
        scene.root.iterate((o) -> recurseTransform(o,new Transform()));
    }
    
    private void recurseTransform(SceneObject sceneObject, Transform transform) {
        //TODO dont compute matrix for static objects?
        //TODO keep premultiplied matrices for static objects and static children?
        List.iterate(manager.delegate().onParentWorldTransformUpdate(), (cb) -> cb.f((SceneObject) sceneObject, transform) );
        Transform mult = sceneObject.transform.get().multiply(transform);
        worldTransformMap.put(sceneObject,mult);
        sceneObject.iterate((g) -> recurseTransform(g,mult));
    }
	
}
