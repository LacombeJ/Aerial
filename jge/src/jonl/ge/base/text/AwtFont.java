package jonl.ge.base.text;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import jonl.jutils.misc.ImageUtils;

class AwtFont {
    
    public static final int HA_LEFT   = 0;
    public static final int HA_CENTER = 1;
    public static final int HA_RIGHT  = 2;
    
    java.awt.Font font;
    FontMetrics metrics;
    boolean antialias;
    
    public AwtFont(String font, int type, int size, boolean antialias) {
        this.font = new java.awt.Font(font,type,size);
        this.antialias = antialias;
        
        int width = 256;
        int height = 256;
        BufferedImage bi = ImageUtils.load(width, height);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setFont(this.font);
        
        metrics = g.getFontMetrics();
    }
    
    public BufferedImage genBufferedImage(String text, int halign) {
        
        String[] lines = text.split("\n");
        int[] widths = new int[lines.length];
        
        int width = 0;
        for (int i=0; i<lines.length; i++) {
            String line = lines[i];
            
            widths[i] = metrics.stringWidth(line);
            width = Math.max(widths[i], width);
        }
        int lineHeight = metrics.getAscent() + metrics.getLeading();
        int height = lineHeight * lines.length;
        
        BufferedImage image = ImageUtils.load(width, height);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setFont(font);
        g.setColor(new java.awt.Color(1,1,1,1f));
        if (antialias) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        
        for (int i=0; i<lines.length; i++) {
            
            String line = lines[i];
            
            int lineWidth = widths[i];
            
            int y = i * lineHeight;
            int x = 0;
            
            if (halign==HA_LEFT) {
                x = 0;
            } else if (halign==HA_CENTER) {
                x = (width-lineWidth) / 2;
            } else if (halign==HA_RIGHT) {
                x = (width-lineWidth);
            }
            
            g.drawString(line, x, metrics.getAscent() + y);
            
        }
        
        return image;
    }
    
}
