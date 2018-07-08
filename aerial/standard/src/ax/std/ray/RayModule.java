package ax.std.ray;

import java.util.ArrayList;

import ax.commons.func.Callback;
import ax.engine.core.Attachment;
import ax.engine.core.Delegate;
import ax.engine.core.Scene;
import ax.engine.core.Service;

public class RayModule extends Attachment {
    
    Callback<Scene> scenePreCreate;
    
    ArrayList<RayComponents> worlds = new ArrayList<>();
    
    public RayModule() {
        super("ray-module");
        
        scenePreCreate = (scene) -> {
            RayComponents rayComponents = new RayComponents(scene);
            scene.data().put("ray-components", rayComponents);
            worlds.add(rayComponents);
        };
        
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onScenePreCreate().add(scenePreCreate);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onScenePreCreate().remove(scenePreCreate);
    }
    
}
