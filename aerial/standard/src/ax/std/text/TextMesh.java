package ax.std.text;

import ax.engine.core.Component;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class TextMesh extends Component {
    
    private Text text;
    Vector4 color = new Vector4(1,1,1,1);
    private boolean depthTest;
    
    Align valign = Align.VA_MIDDLE;
    Align halign = Align.HA_CENTER;
    
    public TextMesh(Text text) {
        this.text = text;
    }
    
    public Text getText() {
        return text;
    }
    
    public void setText(Text text) {
        this.text = text;
    }
    
    public Align getHAlign() {
        return halign;
    }
    
    public void setHAlign(Align halign) {
        this.halign = halign;
    }
    
    public Align getVAlign() {
        return valign;
    }
    
    public void setVAlign(Align valign) {
        this.valign = valign;
    }

    public void setAlign(Align halign, Align valign) {
        this.halign = halign;
        this.valign = valign;
    }

    public Vector4 getColor() {
        return color.get();
    }
    
    public void setColor(Vector4 color) {
        this.color.set(color);
    }
    
    public void setColor(Vector3 color) {
        this.color = new Vector4(color, 1);
    }
    
    public void setDepthTest(boolean enable) {
        depthTest = enable;
    }
    
    public boolean getDepthTest() {
        return depthTest;
    }
    
}
