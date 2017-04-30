package jonl.ge;

import jonl.vmath.Matrix4;
import jonl.vmath.Vector4;

public class Camera extends Component {
    
    float height = 10f; //used in orthographic projection
    float fov = 90; //used in perspective projection
    float aspect = 16/9f;
    float near = 0.1f;
    float far = 100f;
    
    boolean orthographic = false;
    Matrix4 projection  = Matrix4.perspective(fov,aspect,near,far);
    
    float viewLeft = 0;
    float viewBottom = 0;
    float viewRight = 1;
    float viewTop = 1;
    
    public boolean renderToTexture = true;
    FrameBuffer gBuffer = null;
    FrameBuffer buffer = null;
    
    float[] clearColor = { 0, 0, 0, 1 };
    boolean scissor = true;
    boolean scaleProjection = false;
    
    

    public Camera() {
        
    }
    
    public float height() { return height; }
    
    public float fov() { return fov; }
    
    public float aspect() { return aspect; }
    
    public float near() { return near; }
    
    public float far() { return far; }
    
    public Camera setPerspective(float fov, float aspect, float near, float far) {
        orthographic = false;
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        projection = Matrix4.perspective(fov,aspect,near,far);
        return this;
    }
    
    public Camera setOrthographic(float height, float aspect, float near, float far) {
        orthographic = true;
        this.height = height;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        projection = Matrix4.orthographic(height,aspect,near,far);
        return this;
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
            GameObject o = getGameObject();
            int[] window = o.getWindowSize();
            aspect = (float)window[0]/window[1];
            setPerspective();
        }
    }
    
    
    
    public static final int POSITION_TEXTURE    = 0;
    public static final int NORMALS_TEXTURE     = 1;
    public static final int DIFFUSE_TEXTURE     = 2;
    public static final int SPECULAR_TEXTURE    = 3;
    public static final int MATERIAL_TEXTURE    = 4;
    public static final int RENDER_TEXTURE      = 5;
    
    public Texture getTexture(int type) {
        if (type==RENDER_TEXTURE) return buffer.getTexture(0);
        return gBuffer.getTexture(type);
    }
    
    public Texture getRenderTexture() {
        return buffer.getTexture(0);
    }
    
    
    
    
}
