package ax.editor.spline;

import ax.commons.data.Cereal;
import ax.editor.StoreTrait;
import ax.editor.SubEditor;

public class SplineStoreTrait extends StoreTrait {
    
    @Override
    public Class<?> getStoreDefinition() {
        return SplineState.class;
    }
    
    @Override
    public synchronized Object getStore(SubEditor editor) {
        return ((SplineEditor)editor).stateCopy;
    }
    
    @Override
    public long storeFrequency() {
        return 1000; // 1 minute
    }
    
    synchronized void setStore(SplineEditor editor) {
        editor.stateCopy = Cereal.copy(editor.state);
    }

    @Override
    public boolean onClose() {
        return true;
    }
    
}
