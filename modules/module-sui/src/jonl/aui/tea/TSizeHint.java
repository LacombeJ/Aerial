package jonl.aui.tea;

public class TSizeHint {

    public int width;
    public int height;
    
    public TSizeHint() {
        
    }
    
    public TSizeHint(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof TSizeHint) {
            TSizeHint sh = (TSizeHint)o;
            return width==sh.width && height==sh.height;
        }
        return super.equals(o);
    }
    
    @Override
    public String toString() {
        return "SizeHint["+width+","+height+"]";
    }
    
}
