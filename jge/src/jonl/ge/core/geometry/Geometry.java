package jonl.ge.core.geometry;

import jonl.ge.base.BaseGeometry;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class Geometry extends BaseGeometry {
    
    // TODO switch to high level geometry that stores Vector3 / Vector2 values
    
    public Geometry() {
        
    }
    
    public Geometry(Vector3[] vertices, Vector3[] normals, Vector2[] texCoords, int[] indices) {
        set(vertices,normals,texCoords,indices);
    }
    
    public Geometry(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
        set(vertices,normals,texCoords,indices);
    }
    
    public Geometry(float[] vertices, int[] indices) {
        this.vertices = Vector3.unpackArray(vertices);
        this.indices = indices;
    }
    
    public Geometry(Vector3[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }
    
    public void set(Vector3[] vertices, Vector3[] normals, Vector2[] texCoords, int[] indices) {
        update = true;
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.indices = indices;
    }
    
    public void set(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
        set(Vector3.unpackArray(vertices), Vector3.unpackArray(normals), Vector2.unpackArray(texCoords), indices);
    }
    
    public Vector3[] getVertexArray() {
        return vertices;
    }
    
    public void setVectorArray(Vector3[] array) {
        update = true;
        vertices = array;
    }
    
    public Vector3[] getNormalArray() {
        return normals;
    }
    
    public void setNormalArray(Vector3[] array) {
        update = true;
        normals = array;
    }
    
    public Vector2[] getTexCoordArray() {
        return texCoords;
    }
    
    public void setTexCoordArray(Vector2[] array) {
        update = true;
        texCoords = array;
    }
    
    public int[] getIndices() {
        return indices;
    }
    
    public void setIndices(int[] array) {
        update = true;
        indices = array;
    }
    
    public int getNumVertices() {
        return vertices.length;
    }
    
    public void modify(GeometryOperation op) {
    	op.modify(this);
    }
    
    public void scaleTexCoords(float x, float y, float ox, float oy) {
    	Vector2[] uvs = getTexCoordArray();
    	Vector2 scalar = new Vector2(x,y);
    	Vector2 origin = new Vector2(ox,oy);
    	for (Vector2 uv : uvs) {
    		uv.scale(scalar, origin);
    	}
    	setTexCoordArray(uvs);
    }
    
    public void scaleTexCoords(float x, float y) {
    	scaleTexCoords(x,y,0.5f,0.5f);
    }
    
    public void translateTexCoords(float x, float y) {
    	Vector2[] uvs = getTexCoordArray();
    	Vector2 translate = new Vector2(x,y);
    	for (Vector2 uv : uvs) {
    		uv.add(translate);
    	}
    	setTexCoordArray(uvs);
    }
    
    public void setCalculateTangents(boolean calculate) {
    	calculateTangents = calculate;
    }
    
    public boolean shouldCalculateTangents() {
    	return calculateTangents;
    }
    
}
