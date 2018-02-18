package jonl.ge.core;

import jonl.ge.core.render.RenderTexture;
import jonl.jgl.GraphicsLibrary;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback2D;
import jonl.jutils.func.Function;
import jonl.jutils.func.Function0D;

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
	
}
