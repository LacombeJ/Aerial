package jonl.ge.core;

import java.util.ArrayList;
import java.util.List;

import jonl.ge.core.light.Light;
import jonl.jgl.GraphicsLibrary;
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
	
	private final ArrayList<Callback<Scene>> sceneCreate = new ArrayList<>();
	private final ArrayList<Callback<Scene>> sceneUpdate = new ArrayList<>();
	
	
	/** @return Callback list for app load call */
	public ArrayList<Callback0D> onLoad() { return appLoad; }
	
	/** @return Callback list for app update call */
	public ArrayList<Callback0D> onUpdate() { return appUpdate; }
	
	/** @return Callback list for app close call */
	public ArrayList<Callback0D> onClose() { return appClose; }
	
	
	/** @return Callback list for scene create call */
	public ArrayList<Callback<Scene>> onSceneCreate() { return sceneCreate; }
	
	/** @return Callback list for scene update call */
	public ArrayList<Callback<Scene>> onSceneUpdate() { return sceneUpdate; }
	
	
	
	// Extended
	
	private final ArrayList<Callback3D<Program,Material,Camera>> programUpdate = new ArrayList<>();
	
	private final ArrayList<Callback<List<Light>>> findLights = new ArrayList<>();
	
	private final ArrayList<Callback2D<GameObject,Camera>> gameObjectRender = new ArrayList<>();
	
	private final ArrayList<Callback2D<GameObject, Transform>> parentWorldTransformUpdate = new ArrayList<>();
	
	private final ArrayList<Callback3D<GameObject, Mesh, GraphicsLibrary>> glPreRender = new ArrayList<>();
	
	private final ArrayList<Callback3D<GameObject, Mesh, GraphicsLibrary>> glPostRender = new ArrayList<>();
	
	
	/** @return Updating material programs with scene light and camera information */
	public ArrayList<Callback3D<Program,Material,Camera>> onProgramUpdate() { return programUpdate; }

	/** @return Called in render loop after finding all standard lights in the scene */
    public ArrayList<Callback<List<Light>>> onFindLights() { return findLights; }
	
	/** @return Called when rendering gameObject in respect to camera */
    public ArrayList<Callback2D<GameObject,Camera>> onGameObjectRender() { return gameObjectRender; }
    
    /** @return Called right before world transform is calculated and passes gameObject and its PARENT's world transform */
    public ArrayList<Callback2D<GameObject, Transform>> onParentWorldTransformUpdate() { return parentWorldTransformUpdate; }
    
    /** @return Called right before glRender is called */
    public ArrayList<Callback3D<GameObject, Mesh, GraphicsLibrary>> onGLPreRender() { return glPreRender; }
    
    /** @return Called right after glRender is called */
    public ArrayList<Callback3D<GameObject, Mesh, GraphicsLibrary>> onGLPostRender() { return glPostRender; }
	
}
