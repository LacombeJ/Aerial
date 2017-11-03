package jonl.ge;

import java.util.ArrayList;

import jonl.ge.Input.CursorState;
import jonl.jutils.time.Clock;

public abstract class Component {

    GameObject gameObject;
    
    void updateComponent() {
        
    }
    
    public GameObject getGameObject() {
        return gameObject;
    }
    
    public GameObject getParent() {
        return gameObject.getParent();
    }
    
    public Transform computeWorldTransform() {
        return gameObject.computeWorldTransform();
    }
    
    public Transform getTransform() {
        return gameObject.transform;
    }
    
    public GameObject getChild(String name) {
        return gameObject.getChild(name);
    }
    
    public GameObject getChildAt(int index) {
        return gameObject.getChildAt(index);
    }
    
    public GameObject[] getChildren() {
        return gameObject.getChildren();
    }
    
    public int getChildCount() {
        return gameObject.getChildrenCount();
    }

    public <T extends Component> T getComponent(Class<T> c) {
        return gameObject.getComponent(c);
    }
    
    public <T extends Component> ArrayList<T> getComponents(Class<T> c) {
        return gameObject.getComponents(c);
    }
    
    public <T extends Component> T getComponentOfType(Class<T> c) {
        return gameObject.getComponentOfType(c);
    }
    
    public <T extends Component> ArrayList<T> getComponentsOfType(Class<T> c) {
        return gameObject.getComponentsOfType(c);
    }
    
    public Component[] getComponents() {
        return gameObject.getComponents();
    }
    
    public GameObject findGameObject(String name) {
        return gameObject.findGameObject(name);
    }
    
    public GameObject findGameObjectWithData(String key) {
        return gameObject.findGameObjectWithData(key);
    }
    
    public GameObject findGameObject(Class<? extends Component> c) {
        return gameObject.findGameObject(c);
    }
    
    public GameObject findGameObjectOfType(Class<? extends Component> c) {
        return gameObject.findGameObjectOfType(c);
    }
    
    public Input getInput() {
        return gameObject.getInput();
    }
    
    public CursorState getCursorState() {
        return gameObject.getCursorState();
    }
    
    public void setCursorState(CursorState state) {
        gameObject.setCursorState(state);
    }
    
    public Window getWindow() {
        return gameObject.getWindow();
    }
    
    public String getInfo(String key) {
        return gameObject.getInfo(key);
    }
    
    public Clock getClock() {
        return gameObject.getClock();
    }
    
}
