package jonl.aerial.editor.spline;

import jonl.aerial.editor.StoreTrait;
import jonl.aerial.editor.SubEditor;
import jonl.jutils.data.Cereal;

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
