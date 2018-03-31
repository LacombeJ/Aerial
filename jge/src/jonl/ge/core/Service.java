package jonl.ge.core;

import jonl.ge.core.render.RenderTexture;
import jonl.jgl.GraphicsLibrary;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback2D;
import jonl.jutils.func.Function;
import jonl.jutils.func.Function0D;

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
	
	private Function<GameObject,Transform> worldTransform = null;
	public void implementGetWorldTransform(Function<GameObject,Transform> implementation)
	{
		worldTransform = implementation;
	}
	public Transform getWorldTransform(GameObject go) {
		return worldTransform.f(go);
	}
	
	private Function0D<GraphicsLibrary> graphicsLibrary = null;
	public void implementGetGL(Function0D<GraphicsLibrary> implementation)
	{
	    graphicsLibrary = implementation;
	}
	public GraphicsLibrary getGL() {
	    return graphicsLibrary.f();
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
	
}
