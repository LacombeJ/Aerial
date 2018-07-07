package ax.aui;

import java.util.ArrayList;

import ax.commons.func.Callback;
import ax.commons.func.List;

public class Signal<T> {
    
    private ArrayList<T> slots = new ArrayList<>();
    
    public void connect(T slot) {
        slots.add(slot);
    }
    
    public void disconnect(T slot) {
        slots.remove(slot);
    }
    
    public void emit(Callback<T> cb) {
        List.iterate(slots, cb);
    }
    
    public ArrayList<T> slots() {
        return List.copy(slots);
    }
    
}
