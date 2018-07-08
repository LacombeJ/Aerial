package ax.aui;

import ax.commons.func.Callback2D;

public interface ComboBox extends Widget {

    String text();
    
    Object data();
    
    int index();
    
    void setIndex(int index);
    
    String text(int index);
    
    void setText(int index, String text);
    
    Object data(int index);
    
    void setData(int index, Object data);
    
    void add(String text);
    
    void add(String text, Object data);
    
    void remove(String text);
    
    void removeData(Object data);
    
    void remove(int index);
    
    boolean contains(String text);
    
    boolean containsData(Object data);
    
    int count();
    
    int indexOf(String text);
    
    int indexOfData(Object data);
    
    Signal<Callback2D<Integer,String>> changed();
    
}
