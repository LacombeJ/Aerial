package ax.tea.graphics;

import java.awt.image.BufferedImage;

import ax.aui.HAlign;
import ax.aui.VAlign;
import ax.commons.misc.AwtFont;
import ax.commons.structs.LimitingPool;
import ax.graphics.GL;
import ax.graphics.Mesh;
import ax.graphics.Program;
import ax.graphics.Texture;
import ax.math.vector.Color;
import ax.math.vector.Matrix4;

/**
 * Based on TextModule
 * 
 * @author Jonathan
 *
 */
public class TTextManager {

    GL gl;
    
    LimitingPool<TText,Texture> textMap;
    
    public TTextManager(GL gl) {
        
        this.gl = gl;
        
        textMap = new LimitingPool<>(1000, (t) -> createTexture(t), (t) -> destroyTexture(t));
        
    }
    
    public void render(String text, float mx, float my, HAlign halign, VAlign valign, TFont font, Color color, Matrix4 ortho, Mesh mesh, Program fontProgram) {
        TText ttext = new TText(text);
        ttext.setAlign(TText.CENTER);
        ttext.setFont(font);
        
        Matrix4 M = Matrix4.identity();
        M.translate(mx, my, 0);
        
        Texture texture = textMap.get(ttext);
        float width = texture.getWidth();
        float height = texture.getHeight();
        float x = 0;
        float y = 0;
        
        if (halign==HAlign.LEFT) {
            x = width * 0.5f;
        } else if (halign==HAlign.RIGHT) {
            x = -width * 0.5f;
        }
        if (valign==VAlign.BOTTOM) {
            y = height * 0.5f;
        } else if (valign==VAlign.TOP) {
            y = -height * 0.5f;
        }
        
        // Post-scale alignment translation
        M.translate(x, y, 0);
        
        // Scale
        // In matrix multiplication backwards-operations, we perform scale afterwards although logically
        // this means we scale first
        M.scale(width, -height, 1); //-height for top-left orientation
        
        Matrix4 MVP = ortho.get().multiply(M);
        
        gl.glUseProgram(fontProgram);
        
        fontProgram.setUniformMat4("MVP",MVP.toArray());
        fontProgram.setUniform("fontColor", color.r, color.g, color.b, color.a);
        fontProgram.setTexture("texture", texture, 0);
        
        gl.glRender(mesh, GL.TRIANGLES);
        
        gl.glUseProgram(null);
    }
    
    private Texture createTexture(TText text) {
        AwtFont font = text.getFont().awt();
        GL.Filter filter = (text.getFont().antialias()) ? GL.LINEAR : GL.NEAREST;
        BufferedImage image = font.genBufferedImage(text.getText(), toAwtAlign(text.getAlign()));
        Texture tex = gl.glGenTexture(image, GL.RGBA16, GL.CLAMP, filter);
        return tex;
    }
    
    private void destroyTexture(Texture texture) {
        texture.delete();
    }
    
    public static int toAwtAlign(int align) {
        switch (align) {
        case TText.LEFT      : return AwtFont.HA_LEFT;
        case TText.CENTER    : return AwtFont.HA_CENTER;
        case TText.RIGHT     : return AwtFont.HA_RIGHT;
        default: return AwtFont.HA_LEFT;
        }
    }
    
}
