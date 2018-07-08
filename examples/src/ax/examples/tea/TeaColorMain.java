package ax.examples.tea;

import ax.aui.*;
import ax.commons.func.Function0D;
import ax.commons.io.Console;
import ax.commons.reflect.FieldDef;
import ax.commons.reflect.Reflect;
import ax.math.vector.Color;
import ax.tea.TLabel;
import ax.tea.TSizeHint;
import ax.tea.TSizeReasoning;
import ax.tea.TUIManager;
import ax.tea.graphics.TFont;

import java.util.ArrayList;

public class TeaColorMain {

    public static void main(String[]args) {
        new TeaColorMain().run();
    }
    
    Window window;
    Widget widget;
    
    void run() {
        
        TUIManager ui = TUIManager.instance();
        
        window = ui.window();
        
        window.call("SET_COLOR (3f, 1, 0.5, 1) ");
        
        window.setWidth(1024);
        window.setHeight(576);
        
        window.setResizable(true);

        Panel panel = ui.panel();
        panel.layout().setMargin(0,0,0,0);

        List list = ui.list();

        ArrayList<FieldDef<Color>> colorFields = Reflect.getFieldDefsOfType(Color.class, Color.class);

        for (FieldDef<Color> colorField : colorFields) {
            list.add(panel(ui, colorField.name(), colorField.value()));
        }

        panel.add(list);

        window.setWidget(panel);
        window.create();
        
        window.setVisible(true);

    }

    Panel panel(UIManager ui, String text, Color color) {
        Panel panel = ui.panel();

        panel.setMinSize(0, 100);

        Color.ColorInt c = Color.toInt(color);
        String background = String.format("rgba(%d,%d,%d,%d)", c.r, c.g, c.b, c.a);

        Color textColor = (color.toHSL().l < 0.4f) ? Color.WHITE : Color.BLACK;

        panel.addStyle(
            "Widget {"
            + "     background: "+background+";"
            + "     border-color: rgb(0,0,0);"
            + "     border: 2;"
            + "     radius: 8;"
            + "}");

        CustomLabel label = new CustomLabel(text, textColor);

        panel.add(label);

        return panel;
    }

    public static class CustomLabel extends TLabel {

        public CustomLabel(String text, Color textColor) {
            super(text);

            Color.ColorInt c = textColor.toInt();
            String color = String.format("rgb(%d,%d,%d)", c.r, c.g, c.b);

            addStyle(
                "Label {"
                + "     color: "+color+";"
                + "     font: calibri_48;"
                + "     text-align: center;"
                + "}");
        }

        @Override
        protected TSizeHint sizeHint() {
            return TSizeReasoning.label(this, TFont.CALIBRI_48);
        }

    }

}
