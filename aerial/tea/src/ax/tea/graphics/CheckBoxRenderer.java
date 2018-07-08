package ax.tea.graphics;

import ax.aui.Info;
import ax.tea.TCheckBox;
import ax.tea.TGraphics;

public class CheckBoxRenderer {

    public static void paint(TCheckBox checkBox, TGraphics g, Info info) {
        ButtonBoxRenderer.paint(checkBox, "CheckBox", g, info);
    }
    
}
