package jonl.aui.tea.graphics;

import jonl.aui.tea.TOldFont;
import jonl.jgl.GraphicsLibrary;

public class TStyle {

    private TColor primary = TColor.fromFloat(0.8f, 0.8f, 0.8f);
    private TColor secondary = TColor.fromFloat(0.5f, 0.5f, 0.6f);
    private TColor tertiary = TColor.fromFloat(0.4f, 0.5f, 0.6f);
    
    private TColor light = TColor.fromFloat(0.85f, 0.8f, 0.95f);
    private TColor dark = TColor.fromFloat(0.05f, 0.7f, 0.1f);
    
    private TButtonStyle button = new TButtonStyle(this);
    
    // ------------------------------------------------------------------------
    
    public TColor primary() { return primary; }
    public TColor secondary() { return secondary; }
    public TColor tertiary() { return tertiary; }
    public TColor light() { return light; }
    public TColor dark() { return dark; }
    
    public TColor textColor() { return TColor.BLACK; }
    
    //TODO remove this
    public TOldFont calibri;
    public void init(GraphicsLibrary gl) {
        calibri = new TOldFont("Calibri",16);
        calibri.font = gl.glGenFont("Calibri",jonl.jgl.Font.PLAIN,16,false);
    }
    
    public TButtonStyle button() { return button; }
    
}
