package ax.box;

import java.util.ArrayList;

import ax.commons.func.Callback;
import ax.commons.func.Callback0D;
import ax.commons.func.Callback2D;
import ax.commons.func.List;
import ax.engine.core.Attachment;
import ax.engine.core.Delegate;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Service;
import ax.engine.core.Transform;

public class Physics2dModule extends Attachment {
    
    Callback<Scene> sceneCreate;
    Callback<Scene> scenePreUpdate;
    
    Callback0D preUpdate;
    Callback2D<SceneObject,Transform> update;
    
    ArrayList<Physics2dWorld> worlds = new ArrayList<>();
    
    public Physics2dModule() {
        super("physics2d-module");
        
        sceneCreate = (scene) -> {
            Physics2dWorld world = new Physics2dWorld(scene);
            scene.data().put("physics2d-world", world);
            worlds.add(world);
        };
        
        scenePreUpdate= (scene) -> {
            Physics2dWorld world = (Physics2dWorld) scene.data().get("physics2d-world");
            world.updateScene(scene);
        };
        
        //TODO only update physics-worlds whose scene is activated
        
        preUpdate = () -> {
            List.iterate(worlds, (world) -> world.step());
        };
        
        update = (sceneObject, worldParent) -> {
            List.iterate(worlds, (world) -> world.update(sceneObject, worldParent));
        };
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onSceneCreate().add(sceneCreate);
        delegate.onScenePreUpdate().add(scenePreUpdate);
        delegate.onUpdate().add(preUpdate);
        delegate.onParentWorldTransformUpdate().add(update);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onSceneCreate().remove(sceneCreate);
        delegate.onScenePreUpdate().remove(scenePreUpdate);
        delegate.onUpdate().remove(preUpdate);
        delegate.onParentWorldTransformUpdate().remove(update);
    }
    
}
