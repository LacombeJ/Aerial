package ax.editor.data;

import ax.engine.core.Mesh.Mode;

public class MeshData {

    String geometryId;
    String meshId;
    
    public boolean cullFace;
    public boolean castShadows;
    public boolean recieveShadows;
    public boolean recieveLight;
    
    Mode mode;
    float thickness = 1;
    boolean depthTest = true;
    boolean visible = true;
    boolean wireframe = false;
    
}
