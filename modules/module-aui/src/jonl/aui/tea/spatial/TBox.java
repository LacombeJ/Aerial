package jonl.aui.tea.spatial;

import jonl.vmath.Mathf;

public class TBox extends TShape {

    int x;
    int y;
    int width;
    int height;
    
    public TBox() {
        
    }
    
    public TBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean isWithin(int x, int y) {
        return Mathf.isWithin(x, y, this.x, this.y, width, height);
    }
    
}
