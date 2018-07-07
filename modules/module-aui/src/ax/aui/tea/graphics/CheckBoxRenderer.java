package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TCheckBox;
import ax.aui.tea.TGraphics;

public class CheckBoxRenderer {

    public static void paint(TCheckBox checkBox, TGraphics g, Info info) {
        ButtonBoxRenderer.paint(checkBox, "CheckBox", g, info);
    }
    
}
