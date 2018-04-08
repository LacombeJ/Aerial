package jonl.jutils.misc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import jonl.jutils.structs.FloatArray;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class ImageUtils {

    public final static int BYTES_PER_PIXEL = 4; //RGBA
    
    public static BufferedImage load(String file) {
        try {
            return ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static BufferedImage load(int width, int height) {
        return new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }
    
    public static BufferedImage load(int[][] data, int width, int height) {
        BufferedImage image = load(width,height);
        for (int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                int index = width*i + j;
                int color = getRgbaInt(data[index]);
                image.setRGB(j,i,color);
            }
        }
        return image;
    }
    
    //TODO handle other image formats (if needed)
    /** Saves image using png format */
    public static void save(BufferedImage image, String file) {
        try {
            File out = new File(file);
            ImageIO.write(image, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static float[] data(BufferedImage bi) {
        FloatArray data = new FloatArray(bi.getHeight() * bi.getWidth() * BYTES_PER_PIXEL);
        for (int i=0; i<bi.getHeight(); i++) {
            for (int j=0; j<bi.getWidth(); j++) {
                float[] c = getColor(bi.getRGB(j,i),true);
                data.put(c);
            }
        }
        return data.getArray();
    }
    
    /**
     * Recycle with BufferPool.returnFloatBuffer
     */
    public static float[] data(float[] color, int width, int height) {
        int size = width*height;
        FloatArray data = new FloatArray(width * height * BYTES_PER_PIXEL);
        float r = color.length>0 ? color[0] : 0;
        float g = color.length>1 ? color[1] : 0;
        float b = color.length>2 ? color[2] : 0;
        float a = color.length>3 ? color[3] : 1;
        for (int i=0; i<size; i++) {
            data.put(r);
            data.put(g);
            data.put(b);
            data.put(a);
        }
        return data.getArray();
    }
    
    /** @return a float array of length 4 with values r,g,b,a */
    public static float[] getColor(int rgba, boolean hasAlpha) {
        return new float[]{
                ((rgba >> 16) & 255)/255f,
                ((rgba >> 8) & 255)/255f,
                ((rgba) & 255)/255f,
                hasAlpha ? ((rgba >> 24)&255) / 255f : 1
        };
    }
    
    /** @return a float array of length 4 with values r,g,b,a */
    public static int[] getColorInt(int rgba, boolean hasAlpha) {
        return new int[]{
                ((rgba >> 16) & 255),
                ((rgba >> 8) & 255),
                ((rgba) & 255),
                hasAlpha ? ((rgba >> 24)&255): 255
        };
    }
    
    /** @return a float array of length 4 with values r,g,b,a */
    public static int getRgbaInt(int[] color) {
        return getRgbaInt(color[0],color[1],color[2],color[3]);
    }

    public static int getRgbaInt(int r, int g, int b, int a) {
        int R = r << 16;
        int G = g << 8;
        int B = b;
        int A = a << 24;
        return R | G | B | A;
    }
    
}
