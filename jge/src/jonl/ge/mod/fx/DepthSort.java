package jonl.ge.mod.fx;

import java.util.ArrayList;

import jonl.ge.core.Camera;
import jonl.ge.core.GameObject;
import jonl.ge.core.Geometry;
import jonl.ge.core.Material;
import jonl.ge.core.Mesh;
import jonl.ge.core.Service;
import jonl.ge.core.Transform;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.Target;
import jonl.jutils.func.List;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class DepthSort extends FXService {

    @Override
    void update(GameObject g, Camera camera, Service service) {
        Mesh mesh = g.getComponent(Mesh.class);
        if (mesh != null) {
            Geometry geometry = mesh.getGeometry();
            Material mat = mesh.getMaterial();
            
            if (mat instanceof PointsMaterial) {
                
                // We want to update the glMesh to change the rendering order from back to front
                // but we don't want to modify the underlying Geometry so we grab the gl mesh instead
                
                jonl.jgl.Mesh glMesh = service.getOrCreateMesh(geometry);
                
                Transform cameraWorld = service.getWorldTransform(camera.gameObject());
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
                    toOrder.add(new Vertex(vertices[i], screen));
                }
                ArrayList<Vertex> sorted = List.order(toOrder, (v0,v1) -> Float.compare(v0.depth, v1.depth));
                ArrayList<Vector3> sorted3 = List.map(sorted, (v) -> v.vertex);
                
                float[] vertexArray = Vector3.pack(sorted3);
                
                glMesh.setVertexAttrib(vertexArray, vertexArray.length/3);
            }
        }
    }
    
    @Override
    void begin(GameObject g, Mesh mesh, GraphicsLibrary gl, Service service) {
        gl.glEnable(Target.PROGRAM_POINT_SIZE);
        gl.glEnable(Target.POINT_SPRITE);
    }

    @Override
    void end(GameObject g, Mesh mesh, GraphicsLibrary gl, Service service) {
        gl.glDisable(Target.PROGRAM_POINT_SIZE);
        gl.glDisable(Target.POINT_SPRITE);
    }
    
    static class Vertex {
        Vector3 vertex;
        float depth;
        Vertex(Vector3 vertex, Vector4 screen) {
            this.vertex = vertex;
            depth = - screen.z / screen.w;
        }
    }

    
}
