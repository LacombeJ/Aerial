package jonl.aui.sui;

import java.util.HashMap;

import jonl.aui.Widget;
import jonl.aui.logic.AWidget;
import jonl.jgl.GraphicsLibrary;
import jonl.vmath.Vector4;

class Style {

    private static HashMap<GraphicsLibrary,Instance> map = new HashMap<>();
    
    static class Instance {
        
        private Instance() { }
        
        GraphicsLibrary gl;
        
        SFont calibri;
        Vector4 buttonColor = new Vector4(0.8f,0.8f,0.8f,1);
        Vector4 buttonColorHover = new Vector4(0.5f,0.5f,0.6f,1);
        
        private void create() {
            calibri = new SFont("Calibri",16);
            calibri.font = gl.glGenFont("Calibri",jonl.jgl.Font.PLAIN,16,false);
        }
    }
    
    static Instance get2(GraphicsLibrary gl) {
        Instance i = map.get(gl);
        if (i==null) {
            i = new Instance();
            i.gl = gl;
            i.create();
            map.put(gl, i);
        }
        return i;
    }

    static Instance get(Widget sw) {
        AWidget root = (AWidget) sw.getRoot();
        if (root instanceof SWindow) {
            SWindow window = (SWindow)root;
            return get2(window.gl);
        }
        return null;
    }
    
    
    
}
