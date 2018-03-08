package jonl.aui.tea;

public class TSizePolicy {

    public int minWidth = 0;
    public int minHeight = 0;
    public int maxWidth = Integer.MAX_VALUE;
    public int maxHeight = Integer.MAX_VALUE;
    public int prefWidth = 0;
    public int prefHeight = 0;
    
    public TSizePolicy() {
        
    }
    
    public TSizePolicy(int minWidth, int minHeight, int maxWidth, int maxHeight, int prefWidth, int prefHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.prefWidth = prefWidth;
        this.prefHeight = prefHeight;
    }
    
}
