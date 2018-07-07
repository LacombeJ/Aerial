 package jonl.ge.core;

import java.util.ArrayList;

import jonl.ge.core.Input.CursorState;
import jonl.ge.core.app.AbstractApplication;
import jonl.jutils.func.List;
import jonl.jutils.structs.AttributeMap;
import jonl.jutils.structs.TreeForest;

public class Scene {
    
    TreeForest<SceneObject> root = null;
    
    String name = "Scene";
    AbstractApplication application;
    boolean persistent = false;
    boolean created = false;
    ArrayList<SceneObject> priors = new ArrayList<>();
    
    AttributeMap data = new AttributeMap();
    
    public Scene() {
    	root = new TreeForest<>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /** @return Scene data */
    public AttributeMap data() {
        return data;
    }
    
    public void add(SceneObject g) {
    	g.setScene(this);
        root.addChild(g);
        if (created) {
        	g.create();
        } else {
        	priors.add(g);
        }
    }
    
    public void remove(SceneObject g) {
        g.setScene(null);
        root.removeChild(g);
        if (!created) {
            priors.remove(g);
        }
    }

    /**
     * Resets scene unless persistent
     */
    void create() {
        if (root==null || !persistent) {
            List.iterate(priors, (g)->g.create());
            priors = new ArrayList<>();
            created = true;
        }
    }
    
    void update() {
    	List.iterate(root.getChildren(), (g)->g.update()); //copy of children to prevent concurrent modification
    }
    
    public <T extends Component> ArrayList<T> findComponents(Class<T> c) {
        ArrayList<T> array = new ArrayList<>();
        root.recurse((g)->{
        	array.addAll(g.getComponents(c));
        });
        return array;
    }
    
    public <T extends Component> ArrayList<T> findComponentsOfType(Class<T> c) {
    	ArrayList<T> array = new ArrayList<>();
        root.recurse((g)->{
        	array.addAll(g.getComponentsOfType(c));
        });
        return array;
    }
    
    public SceneObject findSceneObject(String name) {
    	return root.getDescendant((g) -> g.name.equals(name));
    }
    
    public SceneObject findSceneObjectWithData(String key) {
    	return root.getDescendant((g) -> g.data().containsKey(key));
    }
    
    public <T extends Component> SceneObject findSceneObject(Class<T> c) {
    	return root.getDescendant((g) -> g.getComponent(c)!=null);
    }
    
    public <T extends Component> SceneObject findSceneObjectOfType(Class<T> c) {
    	return root.getDescendant((g) -> g.getComponentOfType(c)!=null);
    }
    
    /**
     * @return all scene objects in scene found recursively
     */
    public ArrayList<SceneObject> getAllSceneObjects() {
    	return root.getDescendants((g)->true);
    }
    
    public Input input() {
        return application.input();
    }
    
    public CursorState getCursorState() {
        return application.getCursorState();
    }
    
    public void setCursorState(CursorState state) {
        application.setCursorState(state);
    }
    
    public Window window() {
        return application.window();
    }
    
    public String info(String key) {
        return application.info(key);
    }
    
    public Time time() {
        return application.time();
    }
    
    public Delegate delegate() {
    	return application.delegate();
    }
    
    public Service service() {
    	return application.service();
    }
    
    @Override
    public String toString() {
        return name+": "+super.toString();
    }
    
}
