package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.HAlign;
import jonl.aui.Layout;
import jonl.aui.TitlePanel;
import jonl.aui.VAlign;
import jonl.aui.tea.graphics.TFont;
import jonl.vmath.Color;

public class TTitlePanel extends TPanel implements TitlePanel {

    String title = "";
    
    public TTitlePanel(String title, Layout layout) {
        super(layout);
        setTitle(title);
        layout.setMargin(9,9,27,9);
    }
    
    public TTitlePanel(String title) {
        this(title, new TListLayout(Align.VERTICAL));
    }
    
    @Override
    public String title() {
        return title;
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void paint(TGraphics g) {
        style().widget().paint(this, info(), g);
        TFont font = style().font();
        float width = font.getWidth(title());
        float dx = 4f;
        float dxs = 4f;
        float toph = font.getHeight()/2f;;
        float lefth = 1;
        float both = 1;
        float righth = 1;
        float thick = 1f;
        Color color = style().textColor();
        g.renderLine(lefth,toph,lefth,height()-both,color,thick);
        g.renderLine(width()-righth,toph,width()-righth,height()-both,color,thick);
        g.renderLine(lefth,height()-both,width()-righth,height()-both,color,thick);
        g.renderText(title(),lefth+dx+dxs,toph,HAlign.LEFT,VAlign.MIDDLE,font,style().textColor());
        g.renderLine(lefth,toph,lefth+dx,toph,color,thick);
        g.renderLine(lefth+dx+width+dxs+dxs,toph,width()-righth,toph,color,thick);
        paint().emit(cb->cb.f(g));
    }
    
}
