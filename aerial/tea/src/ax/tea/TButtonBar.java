package ax.tea;

import java.util.ArrayList;

import ax.aui.Align;
import ax.aui.Button;
import ax.aui.SizePolicy;

public abstract class TButtonBar extends TWidget {

    private TSpacer spacer;
    
    public TButtonBar(Align align) {
        super();
        
        TListLayout menuBarLayout = new TListLayout(align);
        menuBarLayout.setMargin(0, 0, 0, 0);
        menuBarLayout.setSpacing(0);
        
        setWidgetLayout(menuBarLayout);
        
        spacer = new TSpacer();
        
        if (align == Align.HORIZONTAL) {
            this.setSizePolicy(new SizePolicy(SizePolicy.EXPANDING, SizePolicy.FIXED));
        } else {
            this.setSizePolicy(new SizePolicy(SizePolicy.FIXED, SizePolicy.EXPANDING));
        }
        
        widgetLayout().add(spacer);
    }
    
    public TButton get(int index) {
        return (TButton) widgetLayout().getWidget(index);
    }

    public void add(Button button) {
        widgetLayout().remove(spacer);
        widgetLayout().add(button);
        widgetLayout().add(spacer);
    }

    public void remove(Button button) {
        widgetLayout().remove(button);
    }
    
    public void remove(int index) {
        widgetLayout().remove(index);
    }

    public void removeAll() {
        widgetLayout().removeAll();
        widgetLayout().add(spacer);
    }
    
    public int indexOf(Button button) {
        return widgetLayout().indexOf(button);
    }
    
    public int count() {
        return widgetLayout().count() - 1; // exclude spacer
    }
    
    public void addSeparator() {
        widgetLayout().remove(spacer);
        widgetLayout().add(new TSpacer(10,10));
        widgetLayout().add(spacer);
    }
    
    ArrayList<TButton> buttons() {
        ArrayList<TButton> buttons = new ArrayList<>();
        
        for (TWidget widget : getChildren()) {
            if (widget instanceof TButton) {
                buttons.add((TButton) widget);
            }
        }
        
        return buttons;
    }
    
}
