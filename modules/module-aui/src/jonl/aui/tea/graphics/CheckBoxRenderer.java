package jonl.aui.tea.graphics;

import jonl.aui.Info;
import jonl.aui.tea.TCheckBox;
import jonl.aui.tea.TGraphics;

public class CheckBoxRenderer {

    public static void paint(TCheckBox checkBox, TGraphics g, Info info) {
        ButtonBoxRenderer.paint(checkBox, "CheckBox", g, info);
    }
    
}
