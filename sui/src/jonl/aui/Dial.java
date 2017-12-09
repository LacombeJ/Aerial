package jonl.aui;

public interface Dial extends Widget {
    
    int getValue();
    void setValue(int value);
    
    int getMinValue();
    void setMinValue(int min);
    
    int getMaxValue();
    void setMaxValue(int max);
    
    void addValueChangedListener(IntChangedListener vc);
    
}
