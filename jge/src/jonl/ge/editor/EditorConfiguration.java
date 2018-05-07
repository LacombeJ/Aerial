package jonl.ge.editor;

import jonl.jutils.data.Cereal;

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
        String name = "editor";
        String version = "1.0";
        String lastProj = "";
        
        EC update() {
            return Cereal.copy(this);
        }
    }
    
    
    
}
