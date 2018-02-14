 package jonl.ge;

import java.util.ArrayList;

import jonl.ge.Input.CursorState;
import jonl.ge.base.App;
import jonl.jutils.func.List;
import jonl.jutils.structs.TreeForest;

public abstract class Scene {
    
	TreeForest<GameObject> root = null;
    String name = "Scene";
    App application;
    boolean persistent = false;
    boolean created = false;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /** Adds GameObjects and assigns Components to them */
    protected abstract void prepare();
    
    public void add(GameObject g) {
    	g.setScene(this);
        root.addChild(g);
        if (created) {
        	g.create();
        }
    }

    /**
     * Resets scene unless persistent
     */
    void create() {
        if (root==null || !persistent) {
            root = new TreeForest<>();
            prepare();
            List.iterate(root.getChildren(), (g)->g.create()); //copy of children to prevent concurrent modification
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
    
    public GameObject findGameObject(String name) {
    	return root.getDescendant((g) -> g.name.equals(name));
    }
    
    public GameObject findGameObjectWithData(String key) {
    	return root.getDescendant((g) -> g.hasData(key));
    }
    
    public <T extends Component> GameObject findGameObject(Class<T> c) {
    	return root.getDescendant((g) -> g.getComponent(c)!=null);
    }
    
    public <T extends Component> GameObject findGameObjectOfType(Class<T> c) {
    	return root.getDescendant((g) -> g.getComponentOfType(c)!=null);
    }
    
    /**
     * @return all game objects in scene found recursively
     */
    public ArrayList<GameObject> getAllGameObjects() {
    	return root.getDescendants((g)->true);
    }
    
    public Input getInput() {
        return application.getInput();
    }
    
    public CursorState getCursorState() {
        return application.getCursorState();
    }
    
    public void setCursorState(CursorState state) {
        application.setCursorState(state);
    }
    
    public Window getWindow() {
        return application.getWindow();
    }
    
    public String getInfo(String key) {
        return application.getInfo(key);
    }
    
    public Time getTime() {
        return application.getTime();
    }
    
    @Override
    public String toString() {
        return name+": "+super.toString();
    }
    
}
