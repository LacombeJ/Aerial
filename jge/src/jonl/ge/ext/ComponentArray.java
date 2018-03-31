package jonl.ge.ext;

import java.util.ArrayList;

import jonl.ge.core.Component;
import jonl.ge.core.Property;

public class ComponentArray extends Property {

    private ArrayList<Component> components = new ArrayList<>();
    private boolean isAdded = false;
    
    public ComponentArray() {
        
    }
    
    public void add(Component c) {
        components.add(c);
        if (isAdded) {
            gameObject().addComponent(c);
        }
    }
    
    public void remove(Component c) {
        components.remove(c);
        if (isAdded) {
            gameObject().removeComponent(c);
        }
    }
    
    @Override
    public void create() {
        for (Component c : components) {
            gameObject().addComponent(c);
        }
        isAdded = true;
    }

    @Override
    public void update() {
        
    }
    
}
