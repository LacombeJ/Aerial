package jonl.ge.utils;

import java.util.ArrayList;
import java.util.List;

import jonl.ge.core.geometry.Geometry;
import jonl.jutils.misc.ArrayUtils;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class MeshBuilder {
    
    private List<Vector3> vertices;
    private List<Vector3> normals;
    private List<Vector2> texCoords;
    private List<Integer> indices;
    
    public MeshBuilder() {
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        indices = new ArrayList<>();
    }
    
    public Geometry build() {
        return new Geometry(Vector3.pack(vertices),Vector3.pack(normals),Vector2.pack(texCoords),ArrayUtils.toIntArray(indices));
    }
    
    public void addVertex(Vector3... vertices) {
        for (Vector3 v : vertices) {
            this.vertices.add(v);
        }
    }
    
    public void addNormal(Vector3... normals) {
        for (Vector3 v : normals) {
            this.normals.add(v);
        }
    }
    
    public void addTexCoord(Vector2... texCoords) {
        for (Vector2 v : texCoords) {
            this.texCoords.add(v);
        }
    }
    
    public void addIndices(int... indices) {
        for (int i: indices) {
            this.indices.add(i);
        }
    }
    
    
}
