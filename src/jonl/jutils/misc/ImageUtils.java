package jonl.jutils.misc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import jonl.jutils.misc.BufferPool;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class ImageUtils {

    public final static int BYTES_PER_PIXEL = 4; //RGBA
    
    public static BufferedImage loadBufferedImage(String file) {
        try {
            return ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static BufferedImage loadBufferedImage(int width, int height) {
        return new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }
    
    /**
     * Buffer data may be overwritten by other data
     * @param bi
     * @return
     */
    public static FloatBuffer getBufferData(BufferedImage bi) {
        FloatBuffer buffer = BufferPool.getFloatBuffer(bi.getWidth()*bi.getHeight()*BYTES_PER_PIXEL,false);
        for (int i=0; i<bi.getHeight(); i++) {
            for (int j=0; j<bi.getWidth(); j++) {
                float[] c = getColor(bi.getRGB(j,i),true);
                buffer.put(c[0]);
                buffer.put(c[1]);
                buffer.put(c[2]);
                buffer.put(c[3]);
            }
        }
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer getBufferData(float[] color, int width, int height) {
        int size = width*height;
        FloatBuffer buffer = BufferPool.getFloatBuffer(size*BYTES_PER_PIXEL,false);
        float r = color.length>0 ? color[0] : 0;
        float g = color.length>1 ? color[1] : 0;
        float b = color.length>2 ? color[2] : 0;
        float a = color.length>3 ? color[3] : 1;
        for (int i=0; i<size; i++) {
            buffer.put(r);
            buffer.put(g);
            buffer.put(b);
            buffer.put(a);
        }
        buffer.flip();
        return buffer;
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
    
}
