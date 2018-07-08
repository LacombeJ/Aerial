package ax.tea;

import java.util.ArrayList;

import ax.aui.Align;
import ax.aui.ComboBox;
import ax.aui.Signal;
import ax.aui.SizePolicy;
import ax.commons.func.Callback2D;
import ax.graphics.Input;
import ax.tea.event.TMouseEvent;
import ax.tea.graphics.ButtonRenderer;
import ax.tea.graphics.ComboBoxRenderer;
import ax.tea.graphics.WidgetRenderer;

public class TComboBox extends TWidget implements ComboBox {

    TComboBoxBar bar = new TComboBoxBar(Align.VERTICAL);
    ArrayList<Object> data = new ArrayList<>();
    
    int currentIndex = 0;
    
    Signal<Callback2D<Integer, String>> changed = new Signal<>();
    
    public TComboBox() {
        super();
        this.setSizePolicy(new SizePolicy(SizePolicy.MINIMUM, SizePolicy.FIXED));
    }
    
    @Override
    public String text() {
        if (bar.count()==0) {
            return "";
        }
        return bar.get(currentIndex).text();
    }
    
    @Override
    public Object data() {
        return data.get(currentIndex);
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
    public String text(int index) {
        return bar.get(index).text();
    }
    
    @Override
    public void setText(int index, String text) {
        bar.get(index).setText(text);
    }
    
    @Override
    public Object data(int index) {
        return data.get(index);
    }
    
    @Override
    public void setData(int index, Object data) {
        this.data.set(index, data);
    }
    
    @Override
    public void add(String text, Object data) {
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
        this.data.add(null);
    }
    
    @Override
    public void add(String text) {
        add(text, null);
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
    public void removeData(Object data) {
        int index = 0;
        for (Object d : this.data) {
            if (d.equals(data)) {
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
    public boolean containsData(Object data) {
        return this.data.contains(data);
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
    public int indexOfData(Object data) {
        return this.data.indexOf(data);
    }

    @Override
    public Signal<Callback2D<Integer, String>> changed() {
        return changed;
    }
    
    @Override
    protected TSizeHint sizeHint() {
        return TSizeReasoning.comboBox(this);
    }
    
    @Override
    protected void paint(TGraphics g) {
        ComboBoxRenderer.paint(this,g,info());
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
            widgetLayout().setMargin(2,2,2,2);
        }
        
        @Override
        protected void paint(TGraphics g) {
            WidgetRenderer.paint(this,"ComboBox.Bar",g,info());
            paint().emit(cb->cb.f(g));
        }
        
    }
    
    /**
     * ComboBox menu buttons
     */
    class TComboBoxButton extends TButton {
        
        TComboBoxButton(String text) {
            super(text);
        }
        
        @Override
        protected void paint(TGraphics g) {
            ButtonRenderer.paint(this,"ComboBox.Button",g,info());
            paint().emit(cb->cb.f(g));
        }
        
    }
    
    
}
