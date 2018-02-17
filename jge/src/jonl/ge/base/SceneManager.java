package jonl.ge.base;

import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.jgl.GraphicsLibrary;

public class SceneManager {

	private Scene scene;
	private SceneUpdater updater;
	private SceneRenderer renderer;
	
	public SceneManager() {
		
	}
	
	public void create(Service service, GraphicsLibrary gl) {
		updater = new SceneUpdater();
		renderer = new SceneRenderer(this, service, gl);
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public void load() {
		((BaseScene)scene).create();
		renderer.load();
	}
	
	public void update() {
		((BaseTime)scene.time()).update(); //Is cast here needed? Eclipse says so
		updater.update(scene);
		renderer.render(scene);
	}
	
	public void close() {
		
	}
	
	SceneUpdater updater() {
		return updater;
	}
	
	SceneRenderer renderer() {
		return renderer;
	}
	
	
}