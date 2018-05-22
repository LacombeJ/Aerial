package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.RadioButton;
import jonl.aui.Widget;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.RadioButtonRenderer;
import jonl.jgl.Input;

public class TRadioButton extends TButton implements RadioButton {

    public TRadioButton() {
        super();
        setCheckable(true);
    }
    
    public TRadioButton(String text) {
        super(text);
        setCheckable(true);
    }
    
    private ArrayList<TRadioButton> siblings() {
        ArrayList<TRadioButton> siblings = new ArrayList<>();
        if (parentLayout != null) {
            for (Widget widget : parentLayout.widgets()) {
                if (widget instanceof TRadioButton && widget!=this) {
                    TRadioButton button = ((TRadioButton)widget);
                    siblings.add(button);
                }
            }
        }
        return siblings;
    }
    
    @Override
    public void setChecked(boolean checked) {
        if (checked && !checked()) {
            super.setChecked(checked);
            for (TRadioButton sibling : siblings()) {
                sibling.setChecked(false);
            }
        } else if (!checked) {
            super.setChecked(checked);
        }
    }

    @Override
    public void toggle() {
        if (!checked()) {
            setChecked(true);
        }
    }
    
    @Override
    protected boolean handleMouseButtonClick(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            clicked().emit(cb->cb.f());
            if (checkable() && !checked()) {
                super.setChecked(true);
                for (TRadioButton sibling : siblings()) {
                    sibling.setChecked(false);
                }
                toggled().emit(cb->cb.f(checked()));
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected TSizeHint sizeHint() {
        return TSizeReasoning.radioButton(this);
    }
    
    @Override
    protected void paint(TGraphics g) {
        RadioButtonRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
    
}
