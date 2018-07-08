package ax.tea;

import java.awt.image.BufferedImage;

import ax.aui.Icon;
import ax.tea.graphics.TImage;

public class TIcon extends TImage implements Icon {

    public TIcon(String file) {
        super(file);
    }
    
    public TIcon(BufferedImage image) {
        super(image);
    }
    
}
