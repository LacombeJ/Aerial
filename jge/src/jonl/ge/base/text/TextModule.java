package jonl.ge.base.text;

import jonl.ge.core.Attachment;
import jonl.ge.core.Camera;
import jonl.ge.core.Delegate;
import jonl.ge.core.GameObject;
import jonl.ge.core.Service;
import jonl.ge.core.Transform;
import jonl.ge.core.geometry.BoxGeometry;
import jonl.ge.core.geometry.Geometry;
import jonl.ge.core.geometry.PlaneGeometry;
import jonl.ge.core.text.Align;
import jonl.ge.core.text.Text;
import jonl.ge.core.text.TextMesh;
import jonl.ge.utils.GLUtils;
import jonl.ge.utils.PresetData;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Mesh;
import jonl.jgl.Program;
import jonl.jgl.Texture;
import jonl.jgl.GraphicsLibrary.Mode;
import jonl.jgl.GraphicsLibrary.Target;
import jonl.jutils.func.Callback0D;
import jonl.jutils.func.Callback2D;
import jonl.jutils.io.Console;
import jonl.vmath.Matrix4;

public class TextModule extends Attachment {

    private Service service;
    private Callback0D load;
    private Callback2D<GameObject,Camera> renderText;
    
    private GraphicsLibrary gl;
    private Program fontProgram;
    private Mesh mesh;
    
    private TextImplementation ti;
    
    public TextModule() {
        super("text-module");
        
        load = () -> load();
        renderText = (g,c) -> renderText(g,c);
        
        ti = new DynamicTextImplementation();
    }

    @Override
    public void add(Delegate delegate, Service service) {
        delegate.onLoad().add(load);
        delegate.onGameObjectRender().add(renderText);
        this.service = service;
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onLoad().remove(load);
        delegate.onGameObjectRender().remove(renderText);
    }
    
    public void load() {
        gl = service.getGL();
        
        int version = gl.glGetGLSLVersioni();
        
        String vs = PresetData.fontVSSource(version);
        String fs = PresetData.fontFSSource(version);
        
        fontProgram = GLUtils.createProgramFromSource(gl, vs, fs);
        
        Geometry geometry = new PlaneGeometry();
        geometry.scaleTexCoords(1f, -1f);
        mesh = gl.glGenMesh(geometry.getVertices(), geometry.getNormals(), geometry.getTexCoords(), geometry.getIndices());
        
        ti.load(gl);
    }
    
    public void renderText(GameObject g, Camera camera) {
        
        TextMesh textMesh = g.getComponent(TextMesh.class);
        if (textMesh != null) {
            Text text = textMesh.getText();
            
            Transform cameraWorld = service.getWorldTransform(camera.gameObject());
            Matrix4 V = Camera.computeViewMatrix(cameraWorld);
            Matrix4 P = camera.getProjection();
            Matrix4 VP = P.get().multiply(V);
            
            Transform t = service.getWorldTransform(g);
            Matrix4 M = t.computeMatrix();
            
            
            Texture texture = ti.get(text);
            float width = texture.getWidth();
            float height = texture.getHeight();
            Align halign = textMesh.getHAlign();
            Align valign = textMesh.getVAlign();
            float x = 0;
            float y = 0;
            if (halign==Align.HA_LEFT) {
                x = width * 0.5f;
            } else if (halign==Align.HA_RIGHT) {
                x = -width * 0.5f;
            }
            if (valign==Align.VA_BOTTOM) {
                y = height * 0.5f;
            } else if (valign==Align.VA_TOP) {
                y = -height * 0.5f;
            }
            
            // Post-scale alignment translation
            M.translate(x, y, 0);
            
            // Scale
            // In matrix multiplication backwards-operations, we perform scale afterwards although logically
            // this means we scale first
            M.scale(width, height, 1);
            
            Matrix4 MVP = VP.get().multiply(M);
            
            gl.glUseProgram(fontProgram);
            
            GLUtils.setUniform(fontProgram, "MVP", MVP);
            GLUtils.setUniform(fontProgram, "fontColor", textMesh.getColor());
            GLUtils.setTexture(fontProgram, "texture", texture, 0);
            
            if (!textMesh.getDepthTest()) {
                gl.glDisable(Target.DEPTH_TEST);
            }
            
            gl.glRender(mesh, Mode.TRIANGLES);
            
            gl.glEnable(Target.CULL_FACE);
            
            gl.glUseProgram(null);
        }
        
    }

    
    
}
