package ax.engine.mod.physics;

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

public class PhysicsModule extends Attachment {
    
    Callback<Scene> sceneCreate;
    
    Callback0D preUpdate;
    Callback2D<SceneObject,Transform> update;
    
    ArrayList<PhysicsWorld> worlds = new ArrayList<>();
    
    public PhysicsModule() {
        super("physics-module");
        
        sceneCreate = (scene) -> {
            PhysicsWorld world = new PhysicsWorld(scene);
            scene.data().put("physics-world", world);
            worlds.add(world);
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
        delegate.onUpdate().add(preUpdate);
        delegate.onParentWorldTransformUpdate().add(update);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onSceneCreate().remove(sceneCreate);
        delegate.onUpdate().remove(preUpdate);
        delegate.onParentWorldTransformUpdate().remove(update);
    }
    
}
