package ax.engine.mod.ray;

import ax.engine.core.Camera;
import ax.engine.core.Transform;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

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
