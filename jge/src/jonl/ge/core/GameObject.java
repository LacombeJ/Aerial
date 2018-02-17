package jonl.ge.core;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.ge.base.BaseSceneObject;
import jonl.ge.core.Input.CursorState;
import jonl.jutils.func.Callback0D;
import jonl.jutils.func.List;
import jonl.vmath.Matrix4;

public final class GameObject extends BaseSceneObject<GameObject> {
    
    String name = "GameObject";
    
    private Scene scene;
    final ArrayList<Component> components = new ArrayList<>();
    
    private final HashMap<String,Object> dataMap = new HashMap<>();
    
    public GameObject() {
        transform = new Transform();
    }
    
    public GameObject(String name) {
    	super();
        setName(name);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Transform transform() {
        return transform;
    }
    
    public Scene scene() {
        return scene;
    }
    
    void setScene(Scene scene) {
        this.scene = scene;
        for (GameObject g : this) {
            g.setScene(scene);
        }
    }
    
    public GameObject parent() {
    	return getParent();
    }
    
    public ArrayList<GameObject> children() {
    	return getChildren();
    }
    
    public void addComponent(Component c) {
        c.gameObject = this;
        components.add(c);
    }
    
    public boolean removeComponent(Component c) {
        if (components.remove(c)) {
            c.gameObject = null;
            return true;
        }
        return false;
    }
    
    /** Adds a new property component that calls these function on create and update */
    public void addProperty(Property p) {
        addComponent(p);
    }
    
    /** Adds a new property component that calls this function on create */
    public void addCreate(Callback0D lamda) {
        Property p = new Property() {
            @Override public void create() { lamda.f(); }
            @Override public void update() { }
        };
        addComponent(p);
    }
    
    /** Adds a new property component that calls this function on update */
    public void addUpdate(Callback0D lamda) {
        Property p = new Property() {
            @Override public void create() { }
            @Override public void update() { lamda.f(); }
        };
        addComponent(p);
    }
    
    public GameObject getChild(String name) {
    	return getChild((g) -> g.name.equals(name));
    }
    
    void create() {
        List.iterate(List.list(components), (c) -> {
        	if (c instanceof Property) {
        		Property p = (Property) c;
                p.create();
        	}
    	} );
        List.iterate(List.list(children()), (g) -> g.create());
    }
    
    void update() {
        //Make a copy of lists to prevent concurrent modification
        List.iterate(List.list(components), (c) -> c.updateComponent());
        List.iterate(List.list(children()), (g) -> g.update());
    }
    
    /** @return Component of the exact class */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> c) {
    	return (T) List.find(components, (comp)->comp.getClass().equals(c));
    }
    
    /** @return Components of the exact class */
    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponents(Class<T> c) {
        return List.map(List.filter(components, (comp)->comp.getClass().equals(c)), (comp)->(T)comp);
    }
    
    /** @return Component which are an instance of this class */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponentOfType(Class<T> c) {
        return (T) List.find(components, (comp)->c.isInstance(comp));
    }
    
    /** @return Components which are an instance of this class */
    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponentsOfType(Class<T> c) {
    	return List.map(List.filter(components, (comp)->c.isInstance(comp)), (comp)->(T)comp);
    }
    
    /** @return all components */
    public Component[] getComponents() {
        return components.toArray(new Component[0]);
    }
    
    public GameObject findGameObject(String name) {
        return scene.findGameObject(name);
    }
    
    public GameObject findGameObjectWithData(String key) {
        return scene.findGameObjectWithData(key);
    }
    
    public GameObject findGameObject(Class<? extends Component> c) {
        return scene.findGameObject(c);
    }
    
    public GameObject findGameObjectOfType(Class<? extends Component> c) {
        return scene.findGameObjectOfType(c);
    }
    
    public Transform computeWorldTransform() {
        if (parent()==null) {
            return transform.get();
        }
        return transform.get().multiply(parent().computeWorldTransform());
    }
    
    public Matrix4 computeWorldMatrix() {
        Matrix4 matrix = transform.computeMatrix();
        if (parent()==null) {
            return matrix;
        }
        return parent().computeWorldMatrix().multiply(matrix);
    }
    
    public Input input() {
        return scene.input();
    }
    
    public CursorState getCursorState() {
        return scene.getCursorState();
    }
    
    public void setCursorState(CursorState state) {
        scene.setCursorState(state);
    }
    
    public Window window() {
        return scene.window();
    }
    
    public String info(String key) {
        return scene.info(key);
    }
    
    public Time time() {
        return scene.time();
    }
    
    public Delegate delegate() {
    	return scene.delegate();
    }
    
    public Service service() {
    	return scene.service();
    }
    
    public void putData(String key, Object data) {
        dataMap.put(key, data);
    }
    
    public Object getData(String key) {
        return dataMap.get(key);
    }
    
    public boolean hasData(String key) {
        return dataMap.containsKey(key);
    }
    
    @Override
    public String toString() {
        return name+": "+super.toString();
    }
    
}
