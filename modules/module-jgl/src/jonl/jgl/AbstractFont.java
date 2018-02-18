package jonl.jgl;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import jonl.jutils.misc.ImageUtils;


public abstract class AbstractFont implements Font {

    private static final int CHAR_SIZE = 256;
    
    private BufferedImage bufferedImage;
    
    private float height;
    //private float width;
    
    private Glyph[] array = new Glyph[CHAR_SIZE];
    
    public void setBufferedImage(java.awt.Font font, boolean antialias) {
        int width = 256;
        int height = 256;
        BufferedImage bi = ImageUtils.load(width,height);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);;
        
        int x = 0;
        for (int i=0; i<CHAR_SIZE; i++) {
            Glyph glyph = new Glyph((char)i,x,fm.charWidth((char)i));
            //The +4 is used to separate characters for rendering (artifacts)
            x += glyph.width + 4;
            array[i] = glyph;
        }
        
        height = fm.getHeight();
        width = x;
        
        bi = ImageUtils.load(width,height);
        
        g = (Graphics2D) bi.getGraphics();
        g.setFont(font);
        g.setColor(new java.awt.Color(1,1,1,1f));
        if (antialias) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        for (int i=0; i<CHAR_SIZE; i++) {
            g.drawString(array[i].c+"",array[i].x,fm.getAscent());
        }
        this.height = height;
        //this.width = width;
        
        bufferedImage = bi;
    }
    
    public void setBufferedImage(String font, int type, int size, boolean antialias) {
        setBufferedImage(new java.awt.Font(font,(type!=BOLD) ? (type==ITALIC) ?
                java.awt.Font.ITALIC : java.awt.Font.PLAIN : java.awt.Font.BOLD,
                size),antialias);
    }
    
    public void setBufferedImage(String font, int type, int size) {
        setBufferedImage(font,type,size,true);
    }
    
    public void setBufferedImage(String font, int size) {
        setBufferedImage(font,PLAIN,size);
    }
    
    public void setBufferedImage(String font) {
        setBufferedImage(font,12);
    }
    
    public BufferedImage getAndDestroyBufferedImage() {
        BufferedImage bi = bufferedImage;
        bufferedImage = null;
        return bi;
    }
    
    @Override
    public float[] getIndices(char c) {
        Font.Glyph g = getGlyph(c);
        float x1 = (g.x)/getTexture().getWidth();
        float x2 = (g.x+g.width)/getTexture().getWidth();
        float[] tex = new float[]{x1,1, x2,1, x2,0, x1,0};
        return tex;
    }
    
    @Override
    public float getWidth(String str) {
        float width = 0;
        for (int i=0; i<str.length(); i++) {
            width += getWidth(str.charAt(i));
        }
        return width;
    }
    
    @Override
    public float getWidth(char c) {
        if (c=='\t') {
            return getGlyph(' ').width*4;
        }
        return getGlyph(c).width;
    }
    
    @Override
    public float getHeight() {
        return height;
    }
    
    @Override
    public Glyph getGlyph(char c) {
        return array[(int)c];
    }
    
}
