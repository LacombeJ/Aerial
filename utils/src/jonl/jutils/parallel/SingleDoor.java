package jonl.jutils.parallel;

public class SingleDoor {
    
    private boolean open = false;
    
    public SingleDoor(boolean isOpen) {
        open = isOpen;
    }
    
    public SingleDoor() {
        this(false);
    }
    
    public synchronized boolean isOpen() {
        return open;
    }
    
    public synchronized void close() {
        open = false;
    }
    
    public synchronized void open() {
        open = true;
    }
    
}
