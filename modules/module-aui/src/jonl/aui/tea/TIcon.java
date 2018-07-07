package jonl.aui.tea;

import java.awt.image.BufferedImage;

import jonl.aui.Icon;
import jonl.aui.tea.graphics.TImage;

public class TIcon extends TImage implements Icon {

    public TIcon(String file) {
        super(file);
    }
    
    public TIcon(BufferedImage image) {
        super(image);
    }
    
}
