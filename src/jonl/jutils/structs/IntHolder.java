package jonl.jutils.structs;

public class IntHolder {

    public int i;
    
    public IntHolder() {
        
    }
    
    public IntHolder(int i) {
        this.i = i;
    }
    
    @Override
    public String toString() {
        return i+"";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof IntHolder) {
            return ((IntHolder)o).i == i;
        }
        return false;
    }
    
}
