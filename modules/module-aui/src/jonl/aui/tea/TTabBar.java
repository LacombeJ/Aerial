package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Button;
import jonl.vmath.Color;

public class TTabBar extends TButtonBar {

    TTabButton newTabButton = null;
    boolean hasNewTabButton = false;
    
    public TTabBar() {
        super(Align.HORIZONTAL);
        newTabButton = new TTabButton("+");
        newTabButton.setMaxSize(30,Integer.MAX_VALUE);
        newTabButton.info().put("cButton",Color.fromFloat(0.1f,0.2f,0.2f));
        newTabButton.info().put("cHover",Color.fromFloat(0.1f,0.15f,0.15f));
        newTabButton.info().put("cToggle",Color.fromFloat(0.1f,0.15f,0.15f));
        newTabButton.setCheckable(false);
    }
    
    @Override
    public void add(Button button) {
        if (hasNewTabButton) {
            super.remove(newTabButton);
        }
        super.add(button);
        if (hasNewTabButton) {
            super.add(newTabButton);
        }
    }
    
    void addNewTabButton() {
        if (!hasNewTabButton) {
            super.add(newTabButton);
            hasNewTabButton = true;
        }
    }
    
    void removeNewTabButton() {
        if (hasNewTabButton) {
            super.remove(newTabButton);
            hasNewTabButton = false;
        }
    }
    

}
