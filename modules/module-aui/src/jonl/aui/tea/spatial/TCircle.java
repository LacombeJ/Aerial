package jonl.aui.tea.spatial;

import jonl.vmath.Mathf;

public class TCircle extends TShape {

    int x;
    int y;
    int radius;
    
    public TCircle() {
        
    }
    
    public TCircle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    
    @Override
    public boolean isWithin(int x, int y) {
        return Mathf.dist(x, y, this.x, this.y) < radius;
    }
    
}
