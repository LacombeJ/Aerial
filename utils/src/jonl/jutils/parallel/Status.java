package jonl.jutils.parallel;

public final class Status {
    
    public int value = 0;
    public String text = "";
    
    @Override
    public String toString() {
        return String.format("[ %d | %s ]",value,text);
    }
            
}



