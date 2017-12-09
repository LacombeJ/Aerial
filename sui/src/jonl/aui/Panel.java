package jonl.aui;

public interface Panel extends MultiSlot {
    
    Layout<MultiSlot> getLayout();
    
    void setLayout(Layout<MultiSlot> layout);
    
}
