package jonl.ge.core;

import jonl.jgl.GL;
import jonl.jutils.func.List;

class SceneManager {

	private Scene scene;
	
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
	
	Scene getScene() {
		return scene;
	}
	
	void setScene(Scene scene) {
		this.scene = scene;
	}
	
	void load() {
	    List.iterate(delegate.onLoad(), (cb) -> cb.f() );
		scene.create();
		List.iterate(delegate.onSceneCreate(), (cb) -> cb.f(scene) );
		renderer.load();
	}
	
	void update() {
	    List.iterate(delegate.onUpdate(), (cb) -> cb.f() );
		scene.time().update();
		updater.update(scene);
		List.iterate(delegate.onSceneUpdate(), (cb) -> cb.f(scene) );
		renderer.render(scene);
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