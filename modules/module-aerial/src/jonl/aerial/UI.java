package jonl.aerial;

import java.io.InputStream;

import jonl.aui.UIManager;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TIcon;
import jonl.aui.tea.TLabel;
import jonl.aui.tea.TSizeHint;
import jonl.aui.tea.TSizeReasoning;
import jonl.aui.tea.graphics.ButtonRenderer;
import jonl.aui.tea.graphics.TFont;
import jonl.aui.tea.graphics.TextRenderer;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.jss.Style;
import jonl.jutils.misc.ImageUtils;
import jonl.vmath.Color;

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
