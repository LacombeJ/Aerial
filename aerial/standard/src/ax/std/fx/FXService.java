package ax.std.fx;

import ax.engine.core.Camera;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Service;
import ax.graphics.GL;

public abstract class FXService {

    abstract void prepare(Scene scene, Service service);
    
    abstract void update(SceneObject g, Camera camera, Service service);
    
    abstract void begin(SceneObject g, Mesh mesh, GL gl, Service service);
    
    abstract void end(SceneObject g, Mesh mesh, GL gl, Service service);
    
}
