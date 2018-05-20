package jonl.ge.core;

import java.util.ArrayList;
import java.util.List;

import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

public class Camera extends Component {
    
    public static final ScissorMode NONE        = ScissorMode.NONE;
    public static final ScissorMode VIEWPORT    = ScissorMode.VIEWPORT;
    public static final ScissorMode CUSTOM      = ScissorMode.CUSTOM;
    
    public static final Target ALL      = Target.ALL;
    public static final Target EXCEPT   = Target.EXCEPT;
    public static final Target ONLY     = Target.ONLY;
    
	int order = 0; //order in which to render cameras (higher order renders last)
	
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
    
    Vector4 clearColor = new Vector4(0,0,0,1);
    ScissorMode scissorMode = VIEWPORT;
    int scissorLeft = 0;
    int scissorRight = 0;
    int scissorTop = 0;
    int scissorBottom = 0;
    
    boolean shouldClearColor = true;
    
    private Target type = Target.ALL;
    private List<SceneObject> targets = new ArrayList<>();
    
    
    
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
    
    public int getOrder() {
    	return order;
    }
    
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
    public void setPerspective(float fov, float aspect, float near, float far) {
        orthographic = false;
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        projection = Matrix4.perspective(fov,aspect,near,far);
    }
    
    /**
     * 
     * @param height
     * @param aspect width / height
     * @param near
     * @param far
     * @return
     */
    public void setOrthographic(float height, float aspect, float near, float far) {
        orthographic = true;
        this.height = height;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        projection = Matrix4.orthographic(height,aspect,near,far);
    }
    
    public void setOrthographicBox(float height, float width, float near, float far) {
        setOrthographic(height,width/height,near,far);
    }
    
    public void setPerspective() {
        orthographic = false;
        projection = Matrix4.perspective(fov,aspect,near,far);
    }
    
    public void setOrthographic() {
        orthographic = true;
        projection = Matrix4.orthographic(height,aspect,near,far);
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
    
    /** @return Vector4(left, bottom, right, top) */
    public Vector4 getViewport() {
        return new Vector4(viewLeft,viewBottom,viewRight,viewTop);
    }
    
    public Vector4 getClearColor() {
    	return clearColor.get();
    }
    
    public void setClearColor(Vector4 color) {
        clearColor = color;
    }
    
    public void setClearColor(float r, float g, float b, float a) {
        clearColor = new Vector4(r,g,b,a);
    }
    
    public ScissorMode getScissorMode() {
    	return scissorMode;
    }
    
    public void setScissorMode(ScissorMode scissor) {
        this.scissorMode = scissor;
    }
    
    public void setScissor(int left, int bottom, int right, int top) {
        scissorLeft = left;
        scissorBottom = bottom;
        scissorRight = right;
        scissorTop = top;
    }
    
    public void enableClearColor(boolean clearColor) {
    	shouldClearColor = clearColor;
    }
    
    /** @return copy of this matrix projection */
    public Matrix4 getProjection() {
        return projection.get();
    }
    
    public Target getTargetType() {
        return type;
    }
    
    public void setTargetType(Target type) {
        this.type = type;
    }
    
    public boolean hasTarget(SceneObject obj) {
        return targets.contains(obj);
    }
    
    public void addTargets(SceneObject... objects) {
        for (SceneObject o : objects) {
            targets.add(o);
        }
    }
    
    public void removeTargets(SceneObject... objects) {
        for (SceneObject o : objects) {
            targets.remove(o);
        }
    }
    
    public void removeAllTargets() {
        targets = new ArrayList<SceneObject>();
    }
    
    public Matrix4 computeViewMatrix() {
        Transform worldTransform = sceneObject().computeWorldTransform();
        return Camera.computeViewMatrix(worldTransform);
    }
    
    public Matrix4 computeViewProjectionMatrix() {
        Transform worldTransform = sceneObject().computeWorldTransform();
        Matrix4 V = Camera.computeViewMatrix(worldTransform);
        Matrix4 P = getProjection();
        return P.multiply(V);
    }
    
    /** @return computes the view matrix given a transform */
    public static Matrix4 computeViewMatrix(Transform transform) {
        Matrix4 view = Matrix4.identity()
            .rotate(transform.rotation)
            .translate(transform.translation.get().neg());
        return view;
    }
    
    public enum Target {
        ALL,
        EXCEPT,
        ONLY,
    }
    
    public enum ScissorMode {
        NONE,
        VIEWPORT,
        CUSTOM
    }
    
}
