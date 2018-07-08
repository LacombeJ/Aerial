package ax.std.fx;

import java.util.ArrayList;

import ax.commons.func.List;
import ax.engine.core.Camera;
import ax.engine.core.Geometry;
import ax.engine.core.Material;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.Service;
import ax.engine.core.Transform;
import ax.graphics.GL;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class DepthSort extends FXService {
    
    @Override
    void prepare(Scene scene, Service service) {
        
    }
    
    @Override
    void update(SceneObject g, Camera camera, Service service) {
        
        Mesh mesh = g.getComponent(Mesh.class);
        if (mesh != null) {
            Geometry geometry = mesh.getGeometry();
            Material mat = mesh.getMaterial();
            if (mat instanceof PointsMaterial) {
                
                // We want to update the glMesh to change the rendering order from back to front
                // but we don't want to modify the underlying Geometry so we grab the gl mesh instead
                
                ax.graphics.Mesh glMesh = service.getOrCreateMesh(geometry);
                
                Transform cameraWorld = service.getWorldTransform(camera.sceneObject());
                Matrix4 V = Camera.computeViewMatrix(cameraWorld);
                Matrix4 P = camera.getProjection();
                Matrix4 VP = P.get().multiply(V);
                
                Transform t = service.getWorldTransform(g);
                Matrix4 M = t.computeMatrix();
                
                Matrix4 MVP = VP.get().multiply(M);
                
                Vector3[] vertices = geometry.getVertexArray();
                ArrayList<Vertex> toOrder = new ArrayList<>();
                
                for (int i=0; i<vertices.length; i++) {
                    Vector4 screen = MVP.multiply(new Vector4(vertices[i],1));
                    
                    float invW = 1 / screen.w;

                    screen.x *= invW;
                    screen.y *= invW;
                    screen.z *= invW;

                    boolean visible =
                        screen.x >= - 1 && screen.x <= 1 &&
                        screen.y >= - 1 && screen.y <= 1 &&
                        screen.z >= - 1 && screen.z <= 1;
                        
                    if (visible) {
                        toOrder.add(new Vertex(vertices[i], screen.z));
                    }
                }
                
                ArrayList<Vertex> sorted = List.order(toOrder, (v0,v1) -> - Float.compare(v0.depth, v1.depth));
                
                ArrayList<Vector3> sorted3 = List.map(sorted, (v) -> v.vertex);
                
                float[] vertexArray = Vector3.pack(sorted3);
                if (vertexArray.length!=0) {
                    glMesh.setVertexAttrib(vertexArray, 3);
                }
            }
        }
    }
    
    @Override
    void begin(SceneObject g, Mesh mesh, GL gl, Service service) {
        Material mat = mesh.getMaterial();
        if (mat instanceof PointsMaterial) {
            PointsMaterial pm = (PointsMaterial)mat;
            gl.glEnable(GL.PROGRAM_POINT_SIZE);
            if (pm.hasTexture()) {
                gl.glEnable(GL.POINT_SPRITE);
            }
            
        }
    }

    @Override
    void end(SceneObject g, Mesh mesh, GL gl, Service service) {
        gl.glDisable(GL.PROGRAM_POINT_SIZE);
        gl.glDisable(GL.POINT_SPRITE);
    }
    
    static class Vertex {
        Vector3 vertex;
        float depth;
        Vertex(Vector3 vertex, float depth) {
            this.vertex = vertex;
            this.depth = depth;
        }
    }
    
}
