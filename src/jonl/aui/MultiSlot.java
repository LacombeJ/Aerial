package jonl.aui;

public interface MultiSlot extends Container {
    
    void add(Widget w);
    
    Widget remove(int i);
    
    Widget getChild(int i);
    
}
