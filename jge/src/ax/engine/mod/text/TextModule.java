package ax.engine.mod.text;

import ax.commons.func.Callback0D;
import ax.commons.func.Callback2D;
import ax.engine.core.Attachment;
import ax.engine.core.Camera;
import ax.engine.core.Delegate;
import ax.engine.core.Geometry;
import ax.engine.core.SceneObject;
import ax.engine.core.Service;
import ax.engine.core.Transform;
import ax.engine.core.geometry.PlaneGeometry;
import ax.engine.utils.GLUtils;
import ax.engine.utils.PresetData;
import ax.graphics.GL;
import ax.graphics.Mesh;
import ax.graphics.Program;
import ax.graphics.Texture;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;

public class TextModule extends Attachment {

    private Service service;
    private Callback0D load;
    private Callback2D<SceneObject,Camera> renderText;
    
    private GL gl;
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
        delegate.onSceneObjectRender().add(renderText);
        this.service = service;
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onLoad().remove(load);
        delegate.onSceneObjectRender().remove(renderText);
    }
    
    public void load() {
        gl = service.getGL();
        
        int version = gl.glGetGLSLVersioni();
        
        String vs = PresetData.fontVSSource(version);
        String fs = PresetData.fontFSSource(version);
        
        fontProgram = GLUtils.createProgramFromSource(gl, vs, fs);
        
        Geometry geometry = new PlaneGeometry();
        geometry.scaleTexCoords(1f, -1f);
        mesh = gl.glGenMesh(
                Vector3.pack(geometry.getVertexArray()),
                Vector3.pack(geometry.getNormalArray()),
                Vector2.pack(geometry.getTexCoordArray()),
                geometry.getIndices());
        
        ti.load(gl);
    }
    
    public void renderText(SceneObject g, Camera camera) {
        
        TextMesh textMesh = g.getComponent(TextMesh.class);
        if (textMesh != null) {
            Text text = textMesh.getText();
            
            Transform cameraWorld = service.getWorldTransform(camera.sceneObject());
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
                gl.glDisable(GL.DEPTH_TEST);
            }
            
            gl.glRender(mesh, GL.TRIANGLES);
            
            if (!textMesh.getDepthTest()) {
                gl.glEnable(GL.DEPTH_TEST);
            }
            
            gl.glEnable(GL.CULL_FACE);
            
            gl.glUseProgram(null);
        }
        
    }

    
    
}
