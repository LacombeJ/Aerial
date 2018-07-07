package ax.engine.core;

import ax.commons.func.Callback;
import ax.commons.func.Callback2D;
import ax.commons.func.Callback3D;
import ax.commons.func.Function;
import ax.commons.func.Function0D;
import ax.commons.func.Function2D;
import ax.engine.core.render.RenderTexture;
import ax.graphics.GL;

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
	
	private Function<Geometry,ax.graphics.Mesh> getOrCreateMesh = null;
    public void implementGetOrCreateMesh(Function<Geometry,ax.graphics.Mesh> implementation)
    {
        getOrCreateMesh = implementation;
    }
    public ax.graphics.Mesh getOrCreateMesh(Geometry geometry) {
        return getOrCreateMesh.f(geometry);
    }
    
    private Function<Texture,ax.graphics.Texture> getOrCreateTexture = null;
    public void implementGetOrCreateTexture(Function<Texture,ax.graphics.Texture> implementation)
    {
        getOrCreateTexture = implementation;
    }
    public ax.graphics.Texture getOrCreateTexture(Texture texture) {
        return getOrCreateTexture.f(texture);
    }
    
    private Function<FrameBuffer,ax.graphics.FrameBuffer> getOrCreateFrameBuffer = null;
    public void implementGetOrCreateFrameBuffer(Function<FrameBuffer,ax.graphics.FrameBuffer> implementation)
    {
        getOrCreateFrameBuffer = implementation;
    }
    public ax.graphics.FrameBuffer getOrCreateFrameBuffer(FrameBuffer frameBuffer) {
        return getOrCreateFrameBuffer.f(frameBuffer);
    }
    
    private Function<Material,ax.graphics.Program> getOrCreateProgram = null;
    public void implementGetOrCreateProgram(Function<Material,ax.graphics.Program> implementation)
    {
        getOrCreateProgram = implementation;
    }
    public ax.graphics.Program getOrCreateProgram(Material material) {
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
    
    private Callback3D<Camera,Material,FrameBuffer> renderDirect = null;
    public void implementRenderDirect(Callback3D<Camera,Material,FrameBuffer> implementation)
    {
        renderDirect = implementation;
    }
    public void renderDirect(Camera camera, Material material, FrameBuffer buffer) {
        renderDirect.f(camera,material,buffer);
    }
	
}
