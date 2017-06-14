package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.ge.Input.CursorState;
import jonl.vmath.Matrix4;

public final class GameObject {
    
    String name = "GameObject";
    final Transform transform = new Transform();
    
    private Scene scene;
    private GameObject parent = null;
    final ArrayList<GameObject> children = new ArrayList<>();
    final ArrayList<Component> components = new ArrayList<>();
    private final HashMap<String,Object> dataMap = new HashMap<>();
    
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
    
    public void removeComponent(Component c) {
        if (components.remove(c)) {
            c.gameObject = null;
        }
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
        for (Component c : components) {
            if (c instanceof Property) {
                Property p = (Property) c;
                p.create();
            }
        }
        for (GameObject g : children) {
            g.create();
        }
    }
    
    void update() {
        for (Component c : components) {
            c.updateComponent();
        }
        for (GameObject g : children) {
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
            if (comp.getClass().isInstance(c)) {
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
    
    public GameObject findGameObject(Class<? extends Component> c) {
        return scene.findGameObject(c);
    }
    
    public GameObject findGameObjectOfType(Class<? extends Component> c) {
        return scene.findGameObjectOfType(c);
    }
    
    public Transform computeWorldTransform() {
        if (parent==null) {
            return new Transform(transform);
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
    
    public int[] getWindowSize() {
        return scene.getWindowSize();
    }
    
    public void closeWindow() {
        scene.closeWindow();
    }
    
    public String getInfo(String key) {
        return scene.getInfo(key);
    }
    
    @Override
    public String toString() {
        return name+": "+super.toString();
    }

    public void putData(String key, Object data) {
        dataMap.put(key, data);
    }
    
    public Object getData(String key) {
        return dataMap.get(key);
    }
    
}
