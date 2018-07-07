package jonl.ge.mod.fx;

import jonl.ge.core.Camera;
import jonl.ge.core.SceneObject;
import jonl.ge.core.Mesh;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;
import jonl.jgl.GL;

public abstract class FXService {

    abstract void prepare(Scene scene, Service service);
    
    abstract void update(SceneObject g, Camera camera, Service service);
    
    abstract void begin(SceneObject g, Mesh mesh, GL gl, Service service);
    
    abstract void end(SceneObject g, Mesh mesh, GL gl, Service service);
    
}
