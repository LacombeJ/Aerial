package jonl.ge.core;

import jonl.ge.core.render.RenderTexture;
import jonl.jgl.GL;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback2D;
import jonl.jutils.func.Function;
import jonl.jutils.func.Function0D;
import jonl.jutils.func.Function2D;

/**
 * Used to extend the engine by getting internal functionality
 * 
 * @author Jonathan
 *
 */
public class Service {

	private Callback2D<Camera,Scene> renderCameraSeparately = null;
	public void implementRenderCameraSeparately(Callback2D<Camera,Scene> implementation)
	{
		renderCameraSeparately = implementation;
	}
	public void renderCameraSeparately(Camera camera, Scene scene) {
		renderCameraSeparately.f(camera, scene);
	}
	
	private Callback<RenderTexture> renderTexture = null;
	public void implementRenderTexture(Callback<RenderTexture> implementation)
	{
		renderTexture = implementation;
	}
	public void renderTexture(RenderTexture rt) {
		renderTexture.f(rt);
	}
	
	private Function<SceneObject,Transform> worldTransform = null;
	public void implementGetWorldTransform(Function<SceneObject,Transform> implementation)
	{
		worldTransform = implementation;
	}
	public Transform getWorldTransform(SceneObject so) {
		return worldTransform.f(so);
	}
	
	private Function0D<GL> gl = null;
	public void implementGetGL(Function0D<GL> implementation)
	{
	    gl = implementation;
	}
	public GL getGL() {
	    return gl.f();
	}
	
	private Function<Geometry,jonl.jgl.Mesh> getOrCreateMesh = null;
    public void implementGetOrCreateMesh(Function<Geometry,jonl.jgl.Mesh> implementation)
    {
        getOrCreateMesh = implementation;
    }
    public jonl.jgl.Mesh getOrCreateMesh(Geometry geometry) {
        return getOrCreateMesh.f(geometry);
    }
    
    private Function<Texture,jonl.jgl.Texture> getOrCreateTexture = null;
    public void implementGetOrCreateTexture(Function<Texture,jonl.jgl.Texture> implementation)
    {
        getOrCreateTexture = implementation;
    }
    public jonl.jgl.Texture getOrCreateTexture(Texture texture) {
        return getOrCreateTexture.f(texture);
    }
    
    private Function<FrameBuffer,jonl.jgl.FrameBuffer> getOrCreateFrameBuffer = null;
    public void implementGetOrCreateFrameBuffer(Function<FrameBuffer,jonl.jgl.FrameBuffer> implementation)
    {
        getOrCreateFrameBuffer = implementation;
    }
    public jonl.jgl.FrameBuffer getOrCreateFrameBuffer(FrameBuffer frameBuffer) {
        return getOrCreateFrameBuffer.f(frameBuffer);
    }
    
    private Function<Material,jonl.jgl.Program> getOrCreateProgram = null;
    public void implementGetOrCreateProgram(Function<Material,jonl.jgl.Program> implementation)
    {
        getOrCreateProgram = implementation;
    }
    public jonl.jgl.Program getOrCreateProgram(Material material) {
        return getOrCreateProgram.f(material);
    }
    
    private Function2D<Camera,SceneObject,Boolean> targetInvalid = null;
    public void implementTargetInvalid(Function2D<Camera,SceneObject,Boolean> implementation)
    {
        targetInvalid = implementation;
    }
    public boolean targetInvalid(Camera camera, SceneObject g) {
        return targetInvalid.f(camera,g);
    }
	
}
