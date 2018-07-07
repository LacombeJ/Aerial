package jonl.aui;

import java.util.ArrayList;

import jonl.jutils.func.Callback;
import jonl.jutils.func.List;

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
