package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.ge.Input.CursorState;
import jonl.jutils.func.Callback0D;
import jonl.jutils.time.Clock;
import jonl.vmath.Matrix4;

public final class GameObject {
    
    String name = "GameObject";
    final Transform transform = new Transform();
    
    private Scene scene;
    private GameObject parent = null;
    final ArrayList<GameObject> children = new ArrayList<>();
    final ArrayList<Component> components = new ArrayList<>();
    
    private final HashMap<String,Object> dataMap = new HashMap<>();
    
    public GameObject() {
        
    }
    
    public GameObject(String name) {
        setName(name);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Transform getTransform() {
        return transform;
    }
    
    public Scene getScene() {
        return scene;
    }
    
    void setScene(Scene scene) {
        this.scene = scene;
        for (GameObject g : children) {
            g.setScene(scene);
        }
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
    public void addProperty(Callback0D lamdaCreate, Callback0D lamdaUpdate) {
        Property p = new Property() {
            @Override public void create() { lamdaCreate.f(); }
            @Override public void update() { lamdaUpdate.f(); }
        };
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
        for (GameObject g : children) {
            if (g.name.equals(name)) {
                return g;
            }
        }
        return null;
    }
    
    public GameObject getChildAt(int index) {
        return children.get(index);
    }
    
    public int getChildrenCount() {
        return children.size();
    }
    
    public GameObject[] getChildren() {
        return children.toArray(new GameObject[0]);
    }
    
    public void addChild(GameObject g) {
        g.parent = this;
        children.add(g);
    }
    
    public GameObject removeChildAt(int index) {
        GameObject g = children.remove(index);
        if (g!=null) {
            g.parent = null;
        }
        return g;
    }
    
    public void removeChild(GameObject g) {
        if (children.remove(g)) {
            g.parent = null;
        }
    }
    
    public GameObject getParent() {
        return parent;
    }
    
    void create() {
        Component[] ccreate = components.toArray(new Component[0]);
        for (Component c : ccreate) { //To prevent concurrent modification
            if (c instanceof Property) {
                Property p = (Property) c;
                p.create();
            }
        }
        GameObject[] gcreate = children.toArray(new GameObject[0]);
        for (GameObject g : gcreate) { //To prevent concurrent modification
            g.create();
        }
    }
    
    void update() {
        Component[] cupdate = components.toArray(new Component[0]);
        for (Component c : cupdate) { //To prevent concurrent modification
            c.updateComponent();
        }
        GameObject[] gupdate = children.toArray(new GameObject[0]);
        for (GameObject g : gupdate) { //To prevent concurrent modification
            g.update();
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> c) {
        for (Component comp : components) {
            if (comp.getClass().equals(c)) {
                return (T) comp;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponents(Class<T> c) {
        ArrayList<T> array = new ArrayList<>();
        for (Component comp : components) {
            if (comp.getClass().equals(c)) {
                array.add((T) comp);
            }
        }
        return (ArrayList<T>) array;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponentOfType(Class<T> c) {
        for (Component comp : components) {
            if (c.isInstance(comp)) {
                return (T) comp;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Component> ArrayList<T> getComponentsOfType(Class<T> c) {
        ArrayList<T> array = new ArrayList<>();
        for (Component comp : components) {
            if (c.isInstance(comp)) {
                array.add((T) comp);
            }
        }
        return (ArrayList<T>) array;
    }
    
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
        if (parent==null) {
            return transform.get();
        }
        return transform.get().multiply(parent.computeWorldTransform());
    }
    
    public Matrix4 computeWorldMatrix() {
        Matrix4 matrix = transform.computeMatrix();
        if (parent==null) {
            return matrix;
        }
        return parent.computeWorldMatrix().multiply(matrix);
    }
    
    public Input getInput() {
        return scene.getInput();
    }
    
    public CursorState getCursorState() {
        return scene.getCursorState();
    }
    
    public void setCursorState(CursorState state) {
        scene.setCursorState(state);
    }
    
    public Window getWindow() {
        return scene.getWindow();
    }
    
    public String getInfo(String key) {
        return scene.getInfo(key);
    }
    
    public Clock getClock() {
        return scene.getClock();
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
