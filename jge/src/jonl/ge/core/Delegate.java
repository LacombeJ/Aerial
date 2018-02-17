package jonl.ge.core;

import java.util.ArrayList;

import jonl.jutils.func.Callback0D;

public class Delegate {
	
	private final ArrayList<Callback0D> appLoad = new ArrayList<>();
	private final ArrayList<Callback0D> appUpdate = new ArrayList<>();
	private final ArrayList<Callback0D> appClose = new ArrayList<>();
	
	private final ArrayList<Callback0D> sceneCreate = new ArrayList<>();
	private final ArrayList<Callback0D> sceneUpdate = new ArrayList<>();
	
	private final ArrayList<Callback0D> renderCreate = new ArrayList<>();
	private final ArrayList<Callback0D> renderUpdate = new ArrayList<>();
	
	
	/** @return Callback list for app load call */
	public ArrayList<Callback0D> onLoad() { return appLoad; }
	
	/** @return Callback list for app update call */
	public ArrayList<Callback0D> onUpdate() { return appUpdate; }
	
	/** @return Callback list for app close call */
	public ArrayList<Callback0D> onClose() { return appClose; }
	
	
	/** @return Callback list for scene create call */
	public ArrayList<Callback0D> onSceneCreate() { return sceneCreate; }
	
	/** @return Callback list for scene update call */
	public ArrayList<Callback0D> onSceneUpdate() { return sceneUpdate; }
	
	
	/** @return Callback list for render create call */
	public ArrayList<Callback0D> onRenderCreate() { return renderCreate; }
	
	/** @return Callback list for render update call */
	public ArrayList<Callback0D> onRenderUpdate() { return renderUpdate; }
	
	
	
}
