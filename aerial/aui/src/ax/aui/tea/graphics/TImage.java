package ax.aui.tea.graphics;

import java.awt.image.BufferedImage;

import ax.commons.misc.ImageUtils;

public class TImage {

    private BufferedImage image;
    
    public TImage(String file) {
        image = ImageUtils.load(file);
    }
    
    public TImage(BufferedImage image) {
        this.image = image;
    }
    
    BufferedImage image() {
        return image;
    }
    
    public int width() {
        return image.getWidth();
    }
    
    public int height() {
        return image.getHeight();
    }
    
}
