package ax.engine.core;

import ax.engine.core.app.AbstractApplication;
import ax.graphics.GL;

public abstract class BaseApp extends AbstractApplication {

    private SceneManager manager = new SceneManager();
    
	protected void managerCreate(Delegate delegate, Service service, GL gl) {
	    manager.create(delegate, service, gl);
	}
	
	protected void managerLoad() {
	    manager.load();
    }
    
    protected void managerUpdate() {
        manager.update();
    }
	
	protected void managerAddScene(Scene scene) {
	    manager.addScene(scene);
	}
	
	protected void managerRemoveScene(Scene scene) {
	    manager.removeScene(scene);
	}
	
	protected void managerRendererSubsection(boolean subsection) {
	    manager.renderer().subsection(subsection);
	}
	
	protected void managerRendererOffset(int x, int y) {
	    manager.renderer().offset(x, y);
	}
	
	protected void managerRendererDimension(int width, int height) {
	    manager.renderer().dimension(width, height);
	}
	
	@Override
    public void addScene(Scene scene) {
        scene.application = this;
        manager.addScene(scene);
    }
    
    @Override
    public void removeScene(Scene scene) {
        scene.application = null;
        manager.removeScene(scene);
    }

}
