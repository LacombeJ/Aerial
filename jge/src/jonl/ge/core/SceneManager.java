package jonl.ge.core;

import java.util.ArrayList;

import jonl.jgl.GL;
import jonl.jutils.func.List;

class SceneManager {

    private ArrayList<Scene> scenes = new ArrayList<>();
	
	private SceneUpdater updater;
	private SceneRenderer renderer;
	
	private Delegate delegate;
	
	SceneManager() {
		
	}
	
	void create(Delegate delegate, Service service, GL gl) {
		updater = new SceneUpdater(this, service);
		renderer = new SceneRenderer(this, service, gl);
		
		this.delegate = delegate;
	}
	
	Scene getScene(int index) {
		return scenes.get(index);
	}
	
	void addScene(Scene scene) {
		scenes.add(scene);
	}
	
	void removeScene(Scene scene) {
	    scenes.remove(scene);
	}
	
	void load() {
	    List.iterate(delegate.onLoad(), (cb) -> cb.f() );
	    for (Scene scene : scenes) {
	        List.iterate(delegate.onScenePreCreate(), (cb) -> cb.f(scene) );
	        scene.create();
	        List.iterate(delegate.onSceneCreate(), (cb) -> cb.f(scene) );
	    }
		
		renderer.load();
	}
	
	void update() {
	    List.iterate(delegate.onUpdate(), (cb) -> cb.f() );
	    for (Scene scene : scenes) {
	        scene.time().update();
	        updater.update(scene);
	        List.iterate(delegate.onSceneUpdate(), (cb) -> cb.f(scene) );
	        renderer.render(scene);
	    }
	}
	
	void close() {
		List.iterate(delegate.onClose(), (cb) -> cb.f() );
	}
	
	SceneUpdater updater() {
		return updater;
	}
	
	SceneRenderer renderer() {
		return renderer;
	}
	
	Delegate delegate() {
		return delegate;
	}
	
	
}