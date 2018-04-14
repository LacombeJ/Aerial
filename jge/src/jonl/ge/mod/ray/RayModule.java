package jonl.ge.mod.ray;

import java.util.ArrayList;
import jonl.ge.core.Attachment;
import jonl.ge.core.Delegate;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.jutils.func.Callback;
import jonl.jutils.io.Console;

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
