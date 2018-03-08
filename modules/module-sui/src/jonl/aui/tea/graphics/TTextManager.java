package jonl.aui.tea.graphics;

import java.awt.image.BufferedImage;
import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Texture;
import jonl.jgl.GraphicsLibrary.Mode;
import jonl.jgl.Mesh;
import jonl.jgl.Program;
import jonl.jgl.Texture.Filter;
import jonl.jgl.Texture.Internal;
import jonl.jgl.Texture.Wrap;
import jonl.jutils.io.Console;
import jonl.jutils.misc.AwtFont;
import jonl.jutils.structs.LimitingPool;
import jonl.vmath.Matrix4;

/**
 * Based on TextModule
 * 
 * @author Jonathan
 *
 */
public class TTextManager {

    GraphicsLibrary gl;
    
    LimitingPool<TText,Texture> textMap;
    
    public TTextManager(GraphicsLibrary gl) {
        
        this.gl = gl;
        
        textMap = new LimitingPool<>(1000, (t) -> createTexture(t), (t) -> destroyTexture(t));
        
    }
    
    public void render(String text, float mx, float my, Matrix4 ortho, HAlign halign, VAlign valign, TColor color, Mesh mesh, Program fontProgram) {
        TText ttext = new TText(text);
        ttext.setAlign(TText.CENTER);
        
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
        M.scale(width, height, 1);
        
        Matrix4 MVP = ortho.get().multiply(M);
        
        gl.glUseProgram(fontProgram);
        
        fontProgram.setUniformMat4("MVP",MVP.toArray());
        fontProgram.setUniform("fontColor", color.r, color.g, color.b, color.a);
        fontProgram.setTexture("texture", texture, 0);
        
        gl.glRender(mesh, Mode.TRIANGLES);
        
        gl.glUseProgram(null);
    }
    
    private Texture createTexture(TText text) {
        AwtFont font = text.getFont().awt();
        Filter filter = (text.getFont().antialias()) ? Filter.LINEAR : Filter.NEAREST;
        BufferedImage image = font.genBufferedImage(text.getText(), toAwtAlign(text.getAlign()));
        Texture tex = gl.glGenTexture(image, Internal.RGBA16, Wrap.CLAMP, filter);
        Console.log(tex.getWidth(), tex.getHeight(), image.getWidth(), image.getHeight());
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
