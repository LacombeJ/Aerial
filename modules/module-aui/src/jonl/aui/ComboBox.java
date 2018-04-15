package jonl.aui;

import jonl.jutils.func.Callback2D;

public interface ComboBox extends Widget {

    String text();
    
    void add(String text);
    
    void remove(String text);
    
    void remove(int index);
    
    boolean contains(String text);
    
    int count();
    
    int indexOf(String text);
    
    int index();
    
    void setIndex(int index);
    
    Signal<Callback2D<Integer,String>> changed();
    
}
