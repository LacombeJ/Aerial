package jonl.jutils.image;

import java.awt.image.BufferedImage;

import jonl.jutils.math.Mathd;
import jonl.jutils.misc.ImageUtils;
import jonl.jutils.structs.Array2Di;

public class ImageArray extends Array2Di {

    public ImageArray(int rows, int columns) {
        super(rows, columns);
    }
    
    public ImageArray(BufferedImage bi) {
        super(bi.getWidth(),bi.getHeight());
        for (int i=0; i<bi.getWidth(); i++) {
            for (int j=0; j<bi.getHeight(); j++) {
                int rgba = bi.getRGB(i, j);
                set(i,j,rgba);
            }
        }
    }
    
    public void setRGBA(int i, int j, int r, int g, int b, int a) {
        int rgba = ImageUtils.getRgbaInt(r,g,b,a);
        set(i,j,rgba);
    }
    
    public int[] getRGBA(int i, int j) {
        int rgba = get(i,j);
        int[] color = ImageUtils.getColorInt(rgba, true);
        return color;
    }
    
    public ImageArray subImage(int i, int j, int width, int height) {
        ImageArray image = new ImageArray(width,height);
        for (int r=0; r<width; r++) {
            for (int c=0; c<height; c++) {
                int rgba = get(i+r,j+c);
                image.set(r, c, rgba);
            }
        }
        return image;
    }
    
    public ImageArray quadrant(int i, int j) {
        int width = getRows()/2;
        int height = getColumns()/2;
        int windex = i*width;
        int hindex = j*height;
        return subImage(windex,hindex,width,height);
    }
    
    public ImageArray halfImage() {
        ImageArray image = new ImageArray(getRows()/2,getColumns()/2);
        for (int i=0; i<image.getRows(); i++) {
            for (int j=0; j<image.getColumns(); j++) {
                int[] rgb00 = ImageUtils.getColorInt(get(i*2,     j*2    ), true);
                int[] rgb01 = ImageUtils.getColorInt(get(i*2,     j*2 + 1), true);
                int[] rgb11 = ImageUtils.getColorInt(get(i*2 + 1, j*2 + 1), true);
                int[] rgb10 = ImageUtils.getColorInt(get(i*2 + 1, j*2    ), true);
                int[] avg = Mathd.sum(rgb00,rgb01,rgb11,rgb10);
                Mathd.divBy(avg, 4);
                image.set(i, j, ImageUtils.getRgbaInt(avg));
            }
        }
        return image;
    }

    public BufferedImage getBufferedImage() {
        BufferedImage bi = ImageUtils.loadBufferedImage(getRows(), getColumns());
        for (int i=0; i<getRows(); i++) {
            for (int j=0; j<getColumns(); j++) {
                bi.setRGB(i, j, get(i,j));
            }
        }
        return bi;
    }

}
