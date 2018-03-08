package jonl.aui.tea;

import java.util.HashMap;

import jonl.jgl.GraphicsLibrary;
import jonl.vmath.Vector4;

class TOldStyle {

    private static HashMap<GraphicsLibrary,Instance> map = new HashMap<>();
    
    static class Instance {
        
        private Instance() { }
        
        GraphicsLibrary gl;
        
        TOldFont calibri;
        Vector4 buttonColor = new Vector4(0.8f,0.8f,0.8f,1);
        Vector4 buttonColorHover = new Vector4(0.5f,0.5f,0.6f,1);
        
        private void create() {
            calibri = new TOldFont("Calibri",16);
            calibri.font = gl.glGenFont("Calibri",jonl.jgl.Font.PLAIN,16,false);
        }
    }
    
    static Instance get(GraphicsLibrary gl) {
        Instance i = map.get(gl);
        if (i==null) {
            i = new Instance();
            i.gl = gl;
            i.create();
            map.put(gl, i);
        }
        return i;
    }

    static Instance get(TWidget w) {
        TWidget sw = w.root();
        if (sw instanceof TWindow) {
            TWindow window = (TWindow)sw;
            return get(window.manager.getGL());
        }
        return null;
    }
    
    
    
}
