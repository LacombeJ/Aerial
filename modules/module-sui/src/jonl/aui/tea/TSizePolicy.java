package jonl.aui.tea;

public class TSizePolicy {

    int minWidth = 0;
    int minHeight = 0;
    int maxWidth = Integer.MAX_VALUE;
    int maxHeight = Integer.MAX_VALUE;
    int prefWidth = 0;
    int prefHeight = 0;
    
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
