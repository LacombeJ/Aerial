 package jonl.ge;

import java.util.ArrayList;

import jonl.ge.Input.CursorState;
import jonl.jutils.time.Clock;

public abstract class Scene {
    
    String name = "Scene";
    App application;
    boolean persistent = false;
    ArrayList<GameObject> gameObjects = null;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /** Adds GameObjects and assigns Components to them */
    protected abstract void prepare();
    
    protected void add(GameObject g) {
        GameObject gameObject = g;
        gameObject.setScene(this);
        gameObjects.add(g);
    }

    /**
     * Resets scene unless persistent
     */
    void create() {
        if (gameObjects==null || !persistent) {
            gameObjects = new ArrayList<>();
            prepare();
            GameObject[] gcreate = gameObjects.toArray(new GameObject[0]);
            for (GameObject g : gcreate) { //To prevent concurrent modification
                g.create();
            }
        }
    }
    
    void update() {
        GameObject[] gupdate = gameObjects.toArray(new GameObject[0]);
        for (GameObject g : gupdate) { //To prevent concurrent modification
            g.update();
        }
    }
    
    <T extends Component> ArrayList<T> findComponents(Class<T> c) {
        ArrayList<T> array = new ArrayList<>();
        for (GameObject g : gameObjects) {
            findComponents(c,g,array);
        }
        return array;
    }
    
    <T extends Component> void findComponents(Class<T> c, GameObject g, ArrayList<T> array) {
        array.addAll(g.getComponents(c));
        for (GameObject child : g.children) {
            findComponents(c,child,array);
        }
    }
    
    <T extends Component> ArrayList<T> findComponentsOfType(Class<T> c) {
        ArrayList<T> array = new ArrayList<>();
        for (GameObject g : gameObjects) {
            findComponentsOfType(c,g,array);
        }
        return array;
    }
    
    <T extends Component> void findComponentsOfType(Class<T> c, GameObject g, ArrayList<T> array) {
        array.addAll(g.getComponentsOfType(c));
        for (GameObject child : g.children) {
            findComponentsOfType(c,child,array);
        }
    }
    
    
    
    GameObject findGameObject(String name) {
        for (GameObject g : gameObjects) {
            GameObject ret = findGameObject(name,g);
            if (ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
    private GameObject findGameObject(String name, GameObject gameObject) {
        if (gameObject.name.equals(name)) {
            return gameObject;
        }
        for (GameObject g : gameObject.children) {
            GameObject ret = findGameObject(name,g);
            if (ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
    <T extends Component> GameObject findGameObject(Class<T> c) {
        for (GameObject g : gameObjects) {
            GameObject ret = findGameObject(c,g);
            if (ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
    <T extends Component> GameObject findGameObject(Class<T> c, GameObject gameObject) {
        if (gameObject.getComponent(c)!=null) {
            return gameObject;
        }
        for (GameObject g : gameObject.children) {
            GameObject ret = findGameObject(c,g);
            if (ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
    <T extends Component> GameObject findGameObjectOfType(Class<T> c) {
        for (GameObject g : gameObjects) {
            GameObject ret = findGameObjectOfType(c,g);
            if (ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
    <T extends Component> GameObject findGameObjectOfType(Class<T> c, GameObject gameObject) {
        if (gameObject.getComponentOfType(c)!=null) {
            return gameObject;
        }
        for (GameObject g : gameObject.children) {
            GameObject ret = findGameObject(c,g);
            if (ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
    /**
     * @return all game objects in scene found recursively
     */
    ArrayList<GameObject> getAllGameObjects() {
        ArrayList<GameObject> array = new ArrayList<>();
        for (GameObject g : gameObjects) {
            getAllGameObjects(g,array);
        }
        return array;
    }
    
    private void getAllGameObjects(GameObject g, ArrayList<GameObject> array) {
        array.add(g);
        for (GameObject gameObject : g.children) {
            getAllGameObjects(gameObject,array);
        }
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
    
    public int[] getWindowSize() {
        return application.getSize();
    }
    
    public void closeWindow() {
        application.close();
    }
    
    public String getInfo(String key) {
        return application.getInfo(key);
    }
    
    public Clock getClock() {
        return application.getClock();
    }
    
    @Override
    public String toString() {
        return name+": "+super.toString();
    }
    
}
