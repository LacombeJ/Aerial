package jonl.aui;

import jonl.jutils.func.Callback0D;

public interface Menu {
    
    String text();
    
    void setText(String text);
    
    Icon icon();
    
    void setIcon(Icon icon);
    
    void add(Menu menu);
    
    void addSeparator();
    
    Signal<Callback0D> pressed();
    
    Signal<Callback0D> released();
    
    Signal<Callback0D> clicked();

}
