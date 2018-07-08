package ax.tea.spatial;

public abstract class TShape {

    public abstract boolean isWithin(int x, int y);
    
    public boolean isWithin(TPoint p) {
        return isWithin(p.x,p.y);
    }
    
}
