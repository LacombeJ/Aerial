package jonl.ge.editor;

import jonl.ge.utils.Cereal;

public class EditorConfiguration {

    static abstract class EC {
        /** Updates the configuration to the next version */
        abstract EC update();
    }
    
    static EC10 update(EC ec) {
        EC updated = ec.update();
        while (!(updated instanceof EC10)) {
            updated = updated.update();
        }
        return (EC10) updated;
    }

    static class EC10 extends EC {
        String name;
        String version;
        Object date;
        
        EC update() {
            return Cereal.copy(this);
        }
    }
    
    
    
}
