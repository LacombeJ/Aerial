package jonl.aui;

public class Margin {

    public static final Margin ZERO = new Margin(0,0,0,0);
    
    public final int left;
    public final int right;
    public final int top;
    public final int bottom;
    
    public Margin(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
    
    public Margin(Margin margin) {
        left = margin.left;
        right = margin.right;
        top = margin.top;
        bottom = margin.bottom;
    }
    
    public int width() {
        return left + right;
    }
    
    public int height() {
        return bottom + top;
    }
    
    @Override
    public String toString() {
        return "Margin("+left+","+right+","+top+","+bottom+")";
    }
    
}
