package ax.engine.core;

import java.util.ArrayList;

import ax.engine.core.Input.CursorState;
import ax.math.vector.Matrix4;

public abstract class Component {

    SceneObject sceneObject;
    
    public SceneObject sceneObject() {
        return sceneObject;
    }
    
    public SceneObject parent() {
        return sceneObject.parent();
    }
    
    public SceneObject getParent() {
    	return sceneObject.getParent();
    }
    
    public Transform parentWorldTransform() {
        return sceneObject.parentWorldTransform();
    }
    
    public Transform computeWorldTransform() {
        return sceneObject.computeWorldTransform();
    }
    
    public Matrix4 computeWorldMatrix() {
        return sceneObject.computeWorldMatrix();
    }
    
    public Transform transform() {
        return sceneObject.transform();
    }
    
    public Scene scene() {
        return sceneObject.scene();
    }
    
    public SceneObject getChild(String name) {
        return sceneObject.getChild(name);
    }
    
    public SceneObject getChildAt(int index) {
        return sceneObject.getChildAt(index);
    }
    
    public ArrayList<SceneObject> children() {
    	return sceneObject.children();
    }
    
    public ArrayList<SceneObject> getChildren() {
        return sceneObject.getChildren();
    }
    
    public int getChildCount() {
        return sceneObject.getChildrenCount();
    }

    public <T extends Component> T getComponent(Class<T> c) {
        return sceneObject.getComponent(c);
    }
    
    public <T extends Component> ArrayList<T> getComponents(Class<T> c) {
        return sceneObject.getComponents(c);
    }
    
    public <T extends Component> T getComponentOfType(Class<T> c) {
        return sceneObject.getComponentOfType(c);
    }
    
    public <T extends Component> ArrayList<T> getComponentsOfType(Class<T> c) {
        return sceneObject.getComponentsOfType(c);
    }
    
    public Component[] getComponents() {
        return sceneObject.getComponents();
    }
    
    public SceneObject findSceneObject(String name) {
        return sceneObject.findSceneObject(name);
    }
    
    public SceneObject findSceneObjectWithData(String key) {
        return sceneObject.findSceneObjectWithData(key);
    }
    
    public SceneObject findSceneObject(Class<? extends Component> c) {
        return sceneObject.findSceneObject(c);
    }
    
    public SceneObject findSceneObjectOfType(Class<? extends Component> c) {
        return sceneObject.findSceneObjectOfType(c);
    }
    
    public Input input() {
        return sceneObject.input();
    }
    
    public CursorState getCursorState() {
        return sceneObject.getCursorState();
    }
    
    public void setCursorState(CursorState state) {
        sceneObject.setCursorState(state);
    }
    
    public Window window() {
        return sceneObject.window();
    }
    
    public String info(String key) {
        return sceneObject.info(key);
    }
    
    public Time time() {
        return sceneObject.time();
    }
    
    public Delegate delegate() {
    	return sceneObject.delegate();
    }
    
    public Service service() {
    	return sceneObject.service();
    }
    
}
