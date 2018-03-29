package jonl.aui.tea.graphics;

import java.awt.image.BufferedImage;

import jonl.jutils.misc.ImageUtils;

public class TImage {

    private BufferedImage image;
    
    public TImage(String file) {
        image = ImageUtils.load(file);
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
