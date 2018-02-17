package jonl.ge.core.geometry;

import jonl.ge.base.BaseGeometry;
import jonl.vmath.MathUtil;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class Geometry extends BaseGeometry {
    
    public Geometry() {
        
    }
    
    public Geometry(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
        set(vertices,normals,texCoords,indices);
    }
    
    public Geometry(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }
    
    public Geometry(Vector3[] vertices, int[] indices) {
        this(MathUtil.getFloatArray(vertices),indices);
    }
    
    protected void set(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
        update = true;
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.indices = indices;
    }
    
    public float[] getVertices() {
        return vertices;
    }
    public float[] getNormals() {
        return normals;
    }
    public float[] getTexCoords() {
        return texCoords;
    }
    public int[] getIndices() {
        return indices;
    }
    
    public void setVertices(float[] v) {
        update = true;
        vertices = v;
    }
    public void setNormals(float[] n) {
        update = true;
        normals = n;
    }
    public void setTexCoords(float[] t) {
        update = true;
        texCoords = t;
    }
    public void setIndices(int[] i) {
        update = true;
        indices = i;
    }
    
    /**
     * 
     * @return a read-only copy of the vertex data
     */
    public Vector3[] getVertexArray() {
        return MathUtil.getVectorArray(vertices,new Vector3(),new Vector3[0]);
    }
    
    public void setVectorArray(Vector3[] array) {
        update = true;
        vertices = MathUtil.getFloatArray(array);
    }
    
    public Vector2[] getTexCoordArray() {
        return MathUtil.getVectorArray(texCoords,new Vector2(),new Vector2[0]);
    }
    
    public void setTexCoordArray(Vector2[] array) {
        update = true;
        texCoords = MathUtil.getFloatArray(array);
    }
    
    public int getNumVertices() {
        return vertices.length/3;
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
