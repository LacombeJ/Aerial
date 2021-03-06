package ax.std.misc;

import java.util.ArrayList;

import ax.engine.core.Component;
import ax.engine.core.Property;

public class ComponentArray extends Property {

    private ArrayList<Component> components = new ArrayList<>();
    private boolean isAdded = false;
    
    public ComponentArray() {
        
    }
    
    public void add(Component c) {
        components.add(c);
        if (isAdded) {
            sceneObject().addComponent(c);
        }
    }
    
    public void remove(Component c) {
        components.remove(c);
        if (isAdded) {
            sceneObject().removeComponent(c);
        }
    }
    
    @Override
    public void create() {
        for (Component c : components) {
            sceneObject().addComponent(c);
        }
        isAdded = true;
    }

    @Override
    public void update() {
        
    }
    
}
