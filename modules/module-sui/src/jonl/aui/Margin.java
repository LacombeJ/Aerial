package jonl.aui;

public class Margin {

    public int left;
    public int right;
    public int top;
    public int bottom;
    
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
    
}
