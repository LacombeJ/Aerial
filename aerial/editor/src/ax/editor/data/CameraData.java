package ax.editor.data;

import java.util.ArrayList;

import ax.engine.core.Camera;
import ax.engine.core.Camera.ScissorMode;
import ax.engine.core.Camera.Target;
import ax.math.vector.Vector4;

public class CameraData extends ComponentData {

    int order = 0;
    
    float height = 10f;
    float fov = 90;
    float aspect = 16/9f;
    float near = 0.1f;
    float far = 100f;
    
    boolean orthographic = false;
    
    float viewLeft = 0;
    float viewBottom = 0;
    float viewRight = 1;
    float viewTop = 1;
    
    Vector4 clearColor = new Vector4(0,0,0,1);
    ScissorMode scissorMode = Camera.VIEWPORT;
    int scissorLeft = 0;
    int scissorRight = 0;
    int scissorTop = 0;
    int scissorBottom = 0;
    
    boolean shouldClearColor = true;
    
    Target type = Target.ALL;
    ArrayList<String> targets = new ArrayList<>();
    
}
