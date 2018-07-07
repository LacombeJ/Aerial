package ax.engine.core;

import java.util.ArrayList;

import ax.commons.func.Callback0D;
import ax.commons.func.List;
import ax.commons.structs.AttributeMap;
import ax.commons.structs.TreeNode;
import ax.engine.core.Input.CursorState;
import ax.math.vector.Matrix4;

public final class SceneObject extends TreeNode<SceneObject> {
    
    String name = "SceneObject";
    
    Transform transform;
    
    private Scene scene;
    final ArrayList<Component> components = new ArrayList<>();
    
    private final AttributeMap dataMap = new AttributeMap();
    
    public SceneObject() {
        transform = new Transform();
    }
    
    public SceneObject(String name) {
    	this();
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
        for (SceneObject g : this) {
            g.setScene(scene);
        }
    }
    
    public SceneObject parent() {
    	return getParent();
    }
    
    public ArrayList<SceneObject> children() {
    	return getChildren();
    }
    
    public void addComponent(Component c) {
        c.sceneObject = this;
        components.add(c);
        if (scene!=null && c instanceof Property) {
            ((Property)c).create();
        }
    }
    
    public boolean removeComponent(Component c) {
        if (components.remove(c)) {
            c.sceneObject = null;
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
    
    public SceneObject getChild(String name) {
    	return getChild((g) -> g.name.equals(name));
    }
    
    void create() {
      //Makes a copy of lists to prevent concurrent modification
        List.iterate(List.list(components), (c) -> {
        	if (c instanceof Property) {
        		Property p = (Property) c;
                p.create();
        	}
    	} );
        List.iterate(List.list(children()), (g) -> g.create());
    }
    
    void update() {
        //Makes a copy of lists to prevent concurrent modification
        List.iterate(List.list(components), (c) -> {
            if (c instanceof Property) {
                Property p = (Property) c;
                p.update();
            }
        } );
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
    
    public SceneObject findSceneObject(String name) {
        return scene.findSceneObject(name);
    }
    
    public SceneObject findSceneObjectWithData(String key) {
        return scene.findSceneObjectWithData(key);
    }
    
    public SceneObject findSceneObject(Class<? extends Component> c) {
        return scene.findSceneObject(c);
    }
    
    public SceneObject findSceneObjectOfType(Class<? extends Component> c) {
        return scene.findSceneObjectOfType(c);
    }
    
    /** @return parent().computeWorldTransform() or Transform.identity() if there is no parent. */
    public Transform parentWorldTransform() {
        if (parent()==null) {
            return Transform.identity();
        }
        return parent().computeWorldTransform();
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
    
    /** @return SceneObject data */
    public AttributeMap data() {
        return dataMap;
    }
    
    @Override
    public String toString() {
        return name+": "+super.toString();
    }
    
}
