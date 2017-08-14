package jonl.aui.sui;

import jonl.aui.Border;
import jonl.aui.Widget;
import jonl.aui.logic.AFixedSplitPanel;

public class SFixedSplitPanel extends AFixedSplitPanel {

    SFixedSplitPanel() {
        
    }
    
    SFixedSplitPanel(Widget w1, Widget w2, Border type, int i) {
        setSplit(w1,w2,type,i);
    }

}
