package ax.engine.core.geometry;

import java.util.ArrayList;

import ax.commons.func.List;
import ax.commons.structs.IntList;
import ax.engine.core.Geometry;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;

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
    
    public void addNormal(Vector3 n) {
        normals.add(n);
    }
    
    public void addTexCoord(Vector2 tc) {
        texCoords.add(tc);
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
