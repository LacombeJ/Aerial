package jonl.jutils.image;

import java.awt.image.BufferedImage;

import jonl.jutils.misc.ImageUtils;

public class Image {

    private BufferedImage image;
    
    public Image(int rows, int columns) {
        image = ImageUtils.load(rows, columns);
    }
    
    public Image(BufferedImage bi) {
        
    }
    
    public void setRGBA(int i, int j, int r, int g, int b, int a) {
        int rgba = ImageUtils.getRgbaInt(r,g,b,a);
        image.setRGB(i, j, rgba);
    }
    
    public int[] getRGBA(int i, int j) {
        int rgba = image.getRGB(i, j);
        int[] color = ImageUtils.getColorInt(rgba, true);
        return color;
    }
    
    public int width() {
        return image.getWidth();
    }
    
    public int height() {
        return image.getHeight();
    }

    public BufferedImage getBufferedImage() {
        return image;
    }

}
