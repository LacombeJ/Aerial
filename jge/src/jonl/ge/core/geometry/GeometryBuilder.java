package jonl.ge.core.geometry;

import java.util.ArrayList;

import jonl.jutils.func.List;
import jonl.jutils.structs.IntList;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class GeometryBuilder {
    
	private ArrayList<Vector3> vertices;
    private ArrayList<Vector3> normals;
    private ArrayList<Vector2> texCoords;
    private IntList indices;
    
    int numVerts = 0;
    
    boolean calculateTangents = true;
	
    public GeometryBuilder() {
    	vertices = new ArrayList<>();
    	normals = new ArrayList<>();
    	texCoords = new ArrayList<>();
    	indices = new IntList();
    }
    
    public void add(Geometry geometry) {
    	int verts = geometry.getNumVertices();
    	
    	//TODO add method of joining duplicated vertices
    	
    	vertices.addAll(List.list(geometry.getVertexArray()));
    	normals.addAll(List.list(geometry.getNormalArray()));
    	texCoords.addAll(List.list(geometry.getTexCoordArray()));
    	for (int index : geometry.getIndices()) {
    		indices.put(index + numVerts); 
    	}
    	
    	numVerts += verts;
    	
    	calculateTangents = calculateTangents && geometry.shouldCalculateTangents();
    	
    }
    
    public void addVertex(Vector3 v) {
        vertices.add(v);
    }
	
    public Geometry build() {
    	Geometry geometry = new Geometry(
    	        vertices.toArray(new Vector3[vertices.size()]),
    	        normals.toArray(new Vector3[normals.size()]),
    	        texCoords.toArray(new Vector2[texCoords.size()]),
    	        indices.toArray());
    	
    	geometry.setCalculateTangents(calculateTangents);
    	
    	return geometry;
    }
    
}
