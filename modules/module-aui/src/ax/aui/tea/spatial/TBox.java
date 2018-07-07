package ax.aui.tea.spatial;

import ax.math.vector.Mathf;

public class TBox extends TShape {

    public int x;
    public int y;
    public int width;
    public int height;
    
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
