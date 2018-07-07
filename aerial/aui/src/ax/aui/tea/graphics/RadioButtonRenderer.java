package ax.aui.tea.graphics;

import ax.aui.Info;
import ax.aui.tea.TGraphics;
import ax.aui.tea.TRadioButton;

public class RadioButtonRenderer {

    public static void paint(TRadioButton radioButton, TGraphics g, Info info) {
        ButtonBoxRenderer.paint(radioButton, "RadioButton", g, info);
    }
    
}
