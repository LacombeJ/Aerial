package jonl.aui.tea;

public class TFillLayout extends TLayout {
    
    @Override
    void layout() {
        if (!isEmpty()) {
            TWidget p = (TWidget) parent;
            TWidget w = (TWidget) widgets()[0];
            TLayoutManager.setPositionAndRequestFire(w, 0, 0);
            TLayoutManager.setSizeAndRequestFire(w, p.width, p.height);
        }
    }

}
