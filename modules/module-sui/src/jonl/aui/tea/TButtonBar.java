package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Button;
import jonl.aui.tea.spatial.TSize;

public abstract class TButtonBar extends TWidget {

private TSpacerItem spacer;
    
    public TButtonBar() {
        super();
        TListLayout menuBarLayout = new TListLayout(Align.HORIZONTAL);
        menuBarLayout.setMargin(0, 0, 0, 0);
        menuBarLayout.setSpacing(0);
        setWidgetLayout(menuBarLayout);
        spacer = new TSpacerItem();
        spacer.setPreferredSize(Integer.MAX_VALUE,0);
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
        widgetLayout().remove(button);;
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
    
    @Override
    protected TSizePolicy getSizePolicy() {
        TLayout layout = widgetLayout();
        int vertical = layout.margin().top + layout.margin().bottom;
        TSizePolicy sp = new TSizePolicy();
        for (TWidget wt : getChildren()) {
            sp.minHeight = Math.max(sp.minHeight, wt.minHeight() + vertical);
            sp.maxHeight = Math.min(sp.maxHeight, wt.maxHeight() + vertical);
            sp.prefHeight = Math.max(sp.prefHeight, wt.preferredHeight() + vertical);
        }
        return sp;
    }
    
}
