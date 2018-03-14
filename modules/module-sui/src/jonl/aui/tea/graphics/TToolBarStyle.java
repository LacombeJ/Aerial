package jonl.aui.tea.graphics;

import jonl.aui.tea.TToolBar;

public class TToolBarStyle extends TWidgetStyle<TToolBar> {
    
    private TColor color;
    
    public TToolBarStyle(TStyle style) {
        super(style);
        
        color = style.light();
        
        this.setPainter((toolBar,i,g)->{
            g.renderRect(0,0,toolBar.width(),toolBar.height(),color);
        });
        
    }
    
    public TColor color() { return color; }
    
    public void color(TColor color) { this.color = color; }
    
}
