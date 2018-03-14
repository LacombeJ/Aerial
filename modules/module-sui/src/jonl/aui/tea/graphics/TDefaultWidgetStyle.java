package jonl.aui.tea.graphics;

import jonl.aui.tea.TWidget;

public class TDefaultWidgetStyle extends TWidgetStyle<TWidget> {
    
    public TDefaultWidgetStyle(TStyle style) {
        super(style);
        
        this.setPainter((widget,i,g)->{
            g.renderRect(0,0,widget.width(),widget.height(),style.background());
        });
    }
    
}
