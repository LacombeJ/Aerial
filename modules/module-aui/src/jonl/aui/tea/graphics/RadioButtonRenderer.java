package jonl.aui.tea.graphics;

import jonl.aui.Info;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TRadioButton;

public class RadioButtonRenderer {

    public static void paint(TRadioButton radioButton, TGraphics g, Info info) {
        ButtonBoxRenderer.paint(radioButton, "RadioButton", g, info);
    }
    
}
