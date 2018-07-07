package ax.aui;

public class SizePolicy {

    public static final int FIXED = 0;
    public static final int MINIMUM = 1;
    public static final int PREFERRED = 2;
    public static final int EXPANDING = 3;
    
    private final int horizontal;
    private final int vertical;
    
    public SizePolicy(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
    
    public SizePolicy() {
        this(FIXED, FIXED);
    }
    
    public int horizontal() {
        return horizontal;
    }
    
    public int vertical() {
        return vertical;
    }
    
}
