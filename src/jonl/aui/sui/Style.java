package jonl.aui.sui;

import jonl.jgl.GraphicsLibrary;
import jonl.vmath.Vector4;

class Style {

    static SFont calibri;
    static Vector4 buttonColor = new Vector4(0.8f,0.8f,0.8f,1);
    static Vector4 buttonColorHover = new Vector4(0.5f,0.5f,0.6f,1);
    
    public static void create(GraphicsLibrary gl) {
        calibri = new SFont("Calibri",16);
        calibri.font = gl.glGenFont("Calibri",jonl.jgl.Font.PLAIN,16,false);
    }
    
    
    
}
