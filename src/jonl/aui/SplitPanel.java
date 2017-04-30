package jonl.aui;

public interface SplitPanel extends DoubleSlot {
    
    void setSplit(Widget w1, Widget w2, Align align, double ratio);
    
    void setAlign(Align align);
    
    void setRatio(double d);
    
    void addRatioChangedListener(DoubleChangedListener vc);
    
}
