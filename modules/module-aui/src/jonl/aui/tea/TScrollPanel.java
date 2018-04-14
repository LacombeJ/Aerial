package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.ScrollPanel;
import jonl.aui.Widget;
import jonl.vmath.Mathd;

public class TScrollPanel extends TWidget implements ScrollPanel {

    TScrollContent content; 
    TScrollBar horBar;
    TScrollBar verBar;
    
    public TScrollPanel() {
        super();
        content = new TScrollContent();
        horBar = new TScrollBar(Align.HORIZONTAL);
        verBar = new TScrollBar(Align.VERTICAL);
        setWidgetLayout(new TScrollLayout());
        widgetLayout().add(content);
        widgetLayout().add(horBar);
        widgetLayout().add(verBar);
        
        content.scrolled().connect((sy)->{
            verBar.setValue(verBar.value() - sy*10);
            content.invalidateLayout();
        });
        horBar.scrolled().connect((sy)->{
            horBar.setValue(horBar.value() - sy*10);
            content.invalidateLayout();
        });
        verBar.scrolled().connect((sy)->{
            verBar.setValue(verBar.value() - sy*10);
            content.invalidateLayout();
        });
        
        horBar.moved().connect((value)->{
            content.invalidateLayout();
        });
        verBar.moved().connect((value)->{
            content.invalidateLayout();
        });
    }
    
    @Override
    public TWidget widget() {
        return content.widget();
    }

    @Override
    public void setWidget(Widget widget) {
        content.setWidget(widget);
        invalidateLayout();
    }
    
    void updateScrollPanel() {
        if (widget()!=null) {
            
            int horWidth = 0;
            if (content.width>=widget().width) {
                horWidth = content.width;
            } else {
                double horRatio = (double)(content.width) / widget().width();
                horRatio = Mathd.clamp(horRatio,0,1);
                horWidth = (int) Mathd.lerp(horRatio,horBar.height,content.width);
            }
            
            int verHeight = 0;
            if (content.height>=widget().height) {
                verHeight = content.height;
            } else {
                double verRatio = (double)(content.height) / widget().height();
                verRatio = Mathd.clamp(verRatio,0,1);
                verHeight = (int) Mathd.lerp(verRatio,verBar.width,content.height);
            }
            
            horBar.setBarSize(horWidth);
            verBar.setBarSize(verHeight);
            
        }
    }

}
