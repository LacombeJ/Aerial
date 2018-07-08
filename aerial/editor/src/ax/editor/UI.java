package ax.editor;

import java.io.InputStream;

import ax.aui.UIManager;
import ax.tea.TButton;
import ax.tea.TGraphics;
import ax.tea.TIcon;
import ax.tea.TLabel;
import ax.tea.TSizeHint;
import ax.tea.TSizeReasoning;
import ax.tea.graphics.ButtonRenderer;
import ax.tea.graphics.TFont;
import ax.tea.graphics.TextRenderer;
import ax.tea.spatial.TBox;
import ax.commons.jss.Style;
import ax.commons.misc.ImageUtils;
import ax.math.vector.Color;

public class UI {
    
    public static TIcon icon(UIManager ui, Class<?> c, String loc) {
        InputStream in = c.getResourceAsStream(loc);
        if (in != null) {
            return new TIcon(ImageUtils.load(in));
        } else {
            System.err.println("Failed to find icon resource: "+loc);
            return null;
        }
    }
    
    public static void resourceIcon(UIManager ui, Class<?> c, String loc, String resource) {
        ui.resource(resource, icon(ui,c,loc));
    }
    
    public static class ProjectLabel extends TLabel {
        
        public ProjectLabel(String text) {
            super(text);
            setName("ProjectLabel");
        }
        
        @Override
        protected TSizeHint sizeHint() {
            return TSizeReasoning.label(this, TFont.CALIBRI_48);
        }
        
    }
    
    public static class ProjectButton extends TButton {

        String path = "";
        
        public ProjectButton(String text, String path) {
            super(text);
            setMinSize(0,100);
            this.path = path;
        }
        
        @Override
        protected TSizeHint sizeHint() {
            TSizeHint hint = super.sizeHint();
            hint.width += 32;
            return hint;
        }
        
        @Override
        protected void paint(TGraphics g) {
            Style style = ButtonRenderer.style(this,"ProjectButton",g,info());
            
            ButtonRenderer.paint(this,"ProjectButton",g,info());
            
            TBox box = new TBox(0,20,width(),height());
            TextRenderer.paint(style, path, box, g);
            paint().emit(cb->cb.f(g));
        }
        
    }
    
    public static String buttonStyle(Color color, String icon) {
        int[] c = Color.toInt(color);
        String bg = "rgba("+c[0]+","+c[1]+","+c[2]+","+"64)";
        String bh = "rgba("+c[0]+","+c[1]+","+c[2]+","+"128)";
        String bd = "rgba("+c[0]+","+c[1]+","+c[2]+","+"32)";
        return ""
            + "ProjectButton {"
            + "     background: "+bg+";"
            +"      image: \""+icon+"\";"
            + "}"
            + "ProjectButton:hover {"
            + "     background: "+bh+";"
            + "}"
            + "ProjectButton:hover:down {"
            + "     background: "+bd+";"
            + "}";
    }
    
}
