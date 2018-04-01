package jonl.aui.tea.graphics;

import jonl.aui.tea.TToolBar;
import jonl.vmath.Color;

public class TToolBarStyle extends TWidgetStyle<TToolBar> {
    
    private Color color;
    
    public TToolBarStyle(TStyle style) {
        super(style);
        
        color = style.light();
        
        this.setPainter((toolBar,i,g)->{
            g.renderRect(0,0,toolBar.width(),toolBar.height(),color);
        });
        
    }
    
    public Color color() { return color; }
    
    public void color(Color color) { this.color = color; }
    
}
