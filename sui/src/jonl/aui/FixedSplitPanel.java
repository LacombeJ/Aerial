package jonl.aui;

public interface FixedSplitPanel extends DoubleSlot {
    
    void setSplit(Widget w1, Widget w2, Border type, int fix);
    
    void setType(Border type);
    
    void setFixedSize(int i);
    
}
