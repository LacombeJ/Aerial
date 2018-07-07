package ax.aui.tea;

import java.util.ArrayList;

import ax.commons.func.List;

public class TManagerUpdate {

    void update(TWidget w) {
        handleTime(TUIManager.instance().timers());
        updateInternal(w);
    }
    
    private void updateInternal(TWidget w) {
        handleTime(w.timers());
        if (w.hasChildren()) {
            for (TWidget child : w.getChildren()) {
                updateInternal(child);
            }
        }
    }
    
    private void handleTime(ArrayList<TTimer> list) {
        // Copy of array for concurrent modification
        ArrayList<TTimer> timers = List.copy(list);
        for (TTimer timer : timers) {
            timer.update();
        }
    }
    
}
