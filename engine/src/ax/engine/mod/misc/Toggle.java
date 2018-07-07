package ax.engine.mod.misc;

import ax.engine.core.InputEvent;
import ax.engine.core.Property;

public abstract class Toggle extends Property {

    InputEvent event;
    
    public Toggle(InputEvent event) {
        this.event = event;
    }
    
    public Toggle() {
        
    }
    
    public final InputEvent getInputEvent() {
        return event;
    }
    
    public final void setInputEvent(InputEvent event) {
        this.event = event;
    }
    
}
