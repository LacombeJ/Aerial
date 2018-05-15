package jonl.aerial;

import jonl.aui.Icon;
import jonl.vmath.Color;

public interface SubEditorTool {

    /**
     * Called once by the editor when creating loading tools before other interface methods
     */
    void init(Pivot pivot);
    
    String id();
    
    String name();
    
    String description();
    
    Color color();
    
    Icon icon();
    
    SubEditor open();
    
    SubEditor open(Object store);
    
}
