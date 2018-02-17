package jonl.ge.base;

import jonl.ge.core.Delegate;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.jgl.GraphicsLibrary;
import jonl.jutils.func.List;

public class SceneManager {

	private Scene scene;
	
	private SceneUpdater updater;
	private SceneRenderer renderer;
	
	private Delegate delegate;
	
	public SceneManager() {
		
	}
	
	public void create(Delegate delegate, Service service, GraphicsLibrary gl) {
		updater = new SceneUpdater(this, service);
		renderer = new SceneRenderer(this, service, gl);
		
		this.delegate = delegate;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public void load() {
	    List.iterate(delegate.onLoad(), (cb) -> cb.f() );
		((BaseScene)scene).create();
		List.iterate(delegate.onSceneCreate(), (cb) -> cb.f(scene) );
		renderer.load();
	}
	
	public void update() {
	    List.iterate(delegate.onUpdate(), (cb) -> cb.f() );
		((BaseTime)scene.time()).update(); //Is cast here needed? Eclipse says so
		updater.update(scene);
		List.iterate(delegate.onSceneUpdate(), (cb) -> cb.f(scene) );
		renderer.render(scene);
	}
	
	public void close() {
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