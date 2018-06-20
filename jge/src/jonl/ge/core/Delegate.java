package jonl.ge.core;

import java.util.ArrayList;
import java.util.List;

import jonl.jgl.GL;
import jonl.jgl.Program;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.jutils.func.Callback2D;
import jonl.jutils.func.Callback3D;

/**
 * Used to extend the engine by adding external functionality
 * 
 * @author Jonathan
 *
 */
public class Delegate {
	
	// Base
	
	private final ArrayList<Callback0D> appLoad = new ArrayList<>();
	private final ArrayList<Callback0D> appUpdate = new ArrayList<>();
	private final ArrayList<Callback0D> appClose = new ArrayList<>();
	
	private final ArrayList<Callback<Scene>> scenePreCreate = new ArrayList<>();
	private final ArrayList<Callback<Scene>> sceneCreate = new ArrayList<>();
	private final ArrayList<Callback<Scene>> scenePreUpdate = new ArrayList<>();
	private final ArrayList<Callback<Scene>> sceneUpdate = new ArrayList<>();
	
	
	/** @return Callback list for app load call */
	public ArrayList<Callback0D> onLoad() { return appLoad; }
	
	/** @return Callback list for app update call */
	public ArrayList<Callback0D> onUpdate() { return appUpdate; }
	
	/** @return Callback list for app close call */
	public ArrayList<Callback0D> onClose() { return appClose; }
	
	
	/** @return Callback list for scene create call */
    public ArrayList<Callback<Scene>> onScenePreCreate() { return scenePreCreate; }
	
	/** @return Callback list for scene create call */
	public ArrayList<Callback<Scene>> onSceneCreate() { return sceneCreate; }
	
	/** @return Callback list for scene update call */
    public ArrayList<Callback<Scene>> onScenePreUpdate() { return scenePreUpdate; }
	
	/** @return Callback list for scene update call */
	public ArrayList<Callback<Scene>> onSceneUpdate() { return sceneUpdate; }
	
	
	
	// Extended
	
	private final ArrayList<Callback3D<Program,Material,Camera>> materialUpdate = new ArrayList<>();
	
	private final ArrayList<Callback2D<Program,Camera>> programUpdate = new ArrayList<>();
	
	private final ArrayList<Callback<List<SceneObject>>> findSceneObjects = new ArrayList<>();
	
	private final ArrayList<Callback2D<SceneObject,Camera>> sceneObjectRenderer = new ArrayList<>();
	
	private final ArrayList<Callback2D<SceneObject, Transform>> parentWorldTransformUpdate = new ArrayList<>();
	
	private final ArrayList<Callback3D<SceneObject, Mesh, GL>> glPreRender = new ArrayList<>();
	
	private final ArrayList<Callback3D<SceneObject, Mesh, GL>> glPostRender = new ArrayList<>();
	
	private final ArrayList<Callback<Camera>> preRenderCamera = new ArrayList<>();
    
    private final ArrayList<Callback<Camera>> postRenderCamera = new ArrayList<>();
	
	
	/** @return Updating programs with material and camera information */
	public ArrayList<Callback3D<Program,Material,Camera>> onMaterialUpdate() { return materialUpdate; }
	
	/** @return Updating programs with camera information */
    public ArrayList<Callback2D<Program,Camera>> onProgramUpdate() { return programUpdate; }
    
    /** @return Called in render loop after finding all scene objects in the scene */
    public ArrayList<Callback<List<SceneObject>>> onFindSceneObjects() { return findSceneObjects; }
	
	/** @return Called when rendering sceneObject in respect to camera */
    public ArrayList<Callback2D<SceneObject,Camera>> onSceneObjectRender() { return sceneObjectRenderer; }
    
    /** @return Called right before world transform is calculated and passes sceneObject and its PARENT's world transform */
    public ArrayList<Callback2D<SceneObject, Transform>> onParentWorldTransformUpdate() { return parentWorldTransformUpdate; }
    
    /** @return Called right before glRender is called */
    public ArrayList<Callback3D<SceneObject, Mesh, GL>> onGLPreRender() { return glPreRender; }
    
    /** @return Called right after glRender is called */
    public ArrayList<Callback3D<SceneObject, Mesh, GL>> onGLPostRender() { return glPostRender; }
    
    /** @return Called before camera is set up and objects are rendered */
    public ArrayList<Callback<Camera>> onPreRenderCamera() { return preRenderCamera; }
    
    /** @return Called after camera is detached */
    public ArrayList<Callback<Camera>> onPostRenderCamera() { return postRenderCamera; }

}
