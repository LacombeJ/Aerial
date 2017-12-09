package jonl.ge;

import java.util.ArrayList;
import java.util.List;

import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

public class Camera extends Component {
    
	int order = 0; //order in which to render cameras
	
    float height = 10f; //used in orthographic projection
    float fov = 90; //used in perspective projection
    float aspect = 16/9f; //width / height
    float near = 0.1f;
    float far = 100f;
    
    boolean orthographic = false;
    Matrix4 projection  = Matrix4.perspective(fov,aspect,near,far);
    
    float viewLeft = 0;
    float viewBottom = 0;
    float viewRight = 1;
    float viewTop = 1;
    
    float[] clearColor = { 0, 0, 0, 1 };
    boolean scissor = true;
    boolean scaleProjection = false;
    
    
    public enum Target {
        ALL,
        EXCEPT,
        ONLY,
    }
    private Target type = Target.ALL;
    private List<GameObject> targets = new ArrayList<>();
    
    
    
    /**
     * Camera Component
     * <p>
     * Default view is projection with 90 fov, 16/9.0 aspect ratio,
     * 0.1 near, and 100 far.
     */
    public Camera() {
        
    }
    
    public float height() { return height; }
    
    public float fov() { return fov; }
    
    public float aspect() { return aspect; }
    
    public float near() { return near; }
    
    public float far() { return far; }
    
    public void setOrder(int order) {
    	this.order = order;
    }
    
    /**
     * 
     * @param fov
     * @param aspect width / height
     * @param near
     * @param far
     * @return
     */
    public Camera setPerspective(float fov, float aspect, float near, float far) {
        orthographic = false;
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        projection = Matrix4.perspective(fov,aspect,near,far);
        return this;
    }
    
    /**
     * 
     * @param height
     * @param aspect width / height
     * @param near
     * @param far
     * @return
     */
    public Camera setOrthographic(float height, float aspect, float near, float far) {
        orthographic = true;
        this.height = height;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        projection = Matrix4.orthographic(height,aspect,near,far);
        return this;
    }
    
    public Camera setOrthographicBox(float height, float width, float near, float far) {
        return setOrthographic(height,width/height,near,far);
    }
    
    public Camera setPerspective() {
        orthographic = false;
        projection = Matrix4.perspective(fov,aspect,near,far);
        return this;
    }
    
    public Camera setOrthographic() {
        orthographic = true;
        projection = Matrix4.orthographic(height,aspect,near,far);
        return this;
    }
    
    public boolean isOrthograpic() {
        return orthographic;
    }
    
    /**
     * Sets the viewport ratio: ex -> (0,0,1,1) for fullscreen
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public void setViewport(float left, float bottom, float right, float top) {
        viewLeft = left;
        viewBottom = bottom;
        viewRight = right;
        viewTop = top;
    }
    
    public Vector4 getViewport() {
        return new Vector4(viewLeft,viewBottom,viewRight,viewTop);
    }
    
    public Camera setClearColor(float[] color) {
        clearColor = color;
        return this;
    }
    
    public void setClearColor(float r, float g, float b, float a) {
        clearColor = new float[]{r,g,b,a};
    }
    
    public Camera setScissor(boolean scissor) {
        this.scissor = scissor;
        return this;
    }
    
    /** @return copy of this matrix projection */
    public Matrix4 getProjection() {
        return projection.get();
    }
    
    
    
    @Override
    void updateComponent() {
        if (scaleProjection) {
            aspect = window().aspect();
            setPerspective();
        }
    }
    
    
    
    
    public Target getTargetType() {
        return type;
    }
    
    public void setTargetType(Target type) {
        this.type = type;
    }
    
    public boolean hasTarget(GameObject obj) {
        return targets.contains(obj);
    }
    
    public void addTargets(GameObject... objects) {
        for (GameObject o : objects) {
            targets.add(o);
        }
    }
    
    public void removeTargets(GameObject... objects) {
        for (GameObject o : objects) {
            targets.remove(o);
        }
    }
    
    public void removeAllTargets() {
        targets = new ArrayList<GameObject>();
    }
    
    
    
    /** @return computes the view matrix given a transform */
    public static Matrix4 computeViewMatrix(Transform transform) {
        Matrix4 view = Matrix4.identity()
            .rotate(transform.rotation)
            .translate(transform.translation.get().neg());
        return view;
    }
    
    
}
