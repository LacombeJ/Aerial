package jonl.ge;

import jonl.jutils.structs.FloatList;
import jonl.jutils.structs.IntList;

public class GeometryBuilder {
    
	private FloatList vertices;
    private FloatList normals;
    private FloatList texCoords;
    private IntList indices;
    
    int numVerts = 0;
    
    boolean calculateTangents = true;
	
    public GeometryBuilder() {
    	vertices = new FloatList();
    	normals = new FloatList();
    	texCoords = new FloatList();
    	indices = new IntList();
    }
    
    public void add(Geometry geometry) {
    	int verts = geometry.getNumVertices();
    	
    	//TODO add method of joining duplicated vertices
    	
    	vertices.put(geometry.getVertices());
    	normals.put(geometry.getNormals());
    	texCoords.put(geometry.getTexCoords());
    	for (int index : geometry.getIndices()) {
    		indices.put(index + numVerts); 
    	}
    	
    	numVerts += verts;
    	
    	calculateTangents = calculateTangents && geometry.shouldCalculateTangents();
    	
    }
	
    public Geometry build() {
    	Geometry geometry = new Geometry(vertices.toArray(),normals.toArray(),texCoords.toArray(),indices.toArray());
    	
    	geometry.setCalculateTangents(calculateTangents);
    	
    	return geometry;
    }
    
}
