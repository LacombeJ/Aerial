package jonl.ge.mod.physics;

import java.util.ArrayList;
import jonl.ge.core.Attachment;
import jonl.ge.core.Delegate;
import jonl.ge.core.GameObject;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.ge.core.Transform;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.jutils.func.Callback2D;
import jonl.jutils.func.List;

public class PhysicsModule extends Attachment {
    
    Callback<Scene> sceneCreate;
    
    Callback0D preUpdate;
    Callback2D<GameObject,Transform> update;
    
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
        
        update = (gameObject, worldParent) -> {
            List.iterate(worlds, (world) -> world.update(gameObject, worldParent));
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
