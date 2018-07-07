package jonl.ge.mod.ray;

import jonl.ge.core.Camera;
import jonl.ge.core.Transform;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class ScreenRayTracer extends RayTracer {

    ScreenRayTracer(Ray ray) {
        super(ray);
    }
    
    /**
     * 
     * @param camera
     * @param x screen x in unit space (0,1)
     * @param y screen y in unit space (0,1)
     */
    public ScreenRayTracer(Camera camera, float x, float y) {
        this(rayFromCamera(camera,x,y));
    }
    
    /**
     * 
     * @param camera
     * @param xy screen coordinate in unit space (0,1)
     */
    public ScreenRayTracer(Camera camera, Vector2 xy) {
        this(rayFromCamera(camera,xy.x,xy.y));
    }
    
    private static Ray rayFromCamera(Camera camera, float x, float y) {
        Transform transform = camera.computeWorldTransform();
        
        Matrix4 view = Camera.computeViewMatrix(transform);
        Matrix4 proj = camera.getProjection();
        Matrix4 MVP = proj.multiply(view);
        
        Vector3 orig = transform.translation.get();

        Vector3 pos = Matrix4.fromScreenSpace(MVP,new Vector4(x,y,1f,1));
        Vector3 dir = pos.sub(orig).norm();
        
        return new Ray(orig,dir);
    } 
    
}
