package jonl.ge.mod.fx;

import jonl.ge.core.Camera;
import jonl.ge.core.GameObject;
import jonl.ge.core.Mesh;
import jonl.ge.core.Service;
import jonl.jgl.GraphicsLibrary;

public abstract class FXService {

    abstract void update(GameObject g, Camera camera, Service service);
    
    abstract void begin(GameObject g, Mesh mesh, GraphicsLibrary gl, Service service);
    
    abstract void end(GameObject g, Mesh mesh, GraphicsLibrary gl, Service service);
    
}
