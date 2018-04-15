package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Align;
import jonl.aui.ComboBox;
import jonl.aui.Signal;
import jonl.aui.tea.event.TMouseEvent;
import jonl.jgl.Input;
import jonl.jutils.func.Callback2D;

public class TComboBox extends TWidget implements ComboBox {

    TComboBoxBar bar = new TComboBoxBar(Align.VERTICAL);
    
    int currentIndex = 0;
    
    Signal<Callback2D<Integer, String>> changed = new Signal<>();
    
    public TComboBox() {
        super();
        
    }
    
    @Override
    public String text() {
        if (bar.count()==0) {
            return "";
        }
        return bar.get(currentIndex).text();
    }
    
    @Override
    public void add(String text) {
        TComboBoxButton button = new TComboBoxButton(text);
        //It's okay to add signals since no-one else should access to this button
        button.clicked().connect(()->{
            int index = bar.indexOf(button);
            if (index!=currentIndex) {
                currentIndex = index;
                changed().emit((cb)->cb.f(currentIndex,button.text()));
            }
            rootPanel().remove(bar);
        });
        bar.add(button);
    }

    @Override
    public void remove(String text) {
        int index = 0;
        for (TButton button : bar.buttons()) {
            if (button.text().equals(text)) {
                remove(index);
            }
            index++;
        }
    }

    @Override
    public void remove(int index) {
        bar.remove(index);
        if (currentIndex>=index) {
            currentIndex--;
        }
    }

    @Override
    public boolean contains(String text) {
        return indexOf(text) == -1;
    }

    @Override
    public int count() {
        return bar.count();
    }

    @Override
    public int indexOf(String text) {
        ArrayList<TButton> buttons = bar.buttons();
        for (int i=0; i<buttons.size(); i++) {
            if (buttons.get(i).text().equals(text)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int index() {
        return currentIndex;
    }

    @Override
    public void setIndex(int index) {
        currentIndex = index;
    }

    @Override
    public Signal<Callback2D<Integer, String>> changed() {
        return changed;
    }
    
    @Override
    protected TSizeHint sizeHint() {
        return style().comboBox().getSizeHint(this,info());
    }
    
    @Override
    protected void paint(TGraphics g) {
        style().comboBox().paint(this,info(),g);
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            rootPanel().add(bar, 0, height(), width(), -1, this);
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseEnter(TMouseEvent event) {
        info().put("bIsMouseWithin", true);
        return true;
    }
    
    @Override
    protected boolean handleMouseExit(TMouseEvent event) {
        info().put("bIsMouseWithin", false);
        return true;
    }
    
    /**
     * ComboBox pop up bar
     */
    class TComboBoxBar extends TButtonBar {
        
        public TComboBoxBar(Align align) {
            super(align);
            
        }
        
    }
    
    /**
     * ComboBox menu buttons
     */
    class TComboBoxButton extends TButton {
        
        TComboBoxButton(String text) {
            super(text);
        }
        
    }
    
    
}
