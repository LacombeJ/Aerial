package jonl.ge.core;

import jonl.ge.base.BaseGeometry;
import jonl.ge.core.geometry.GeometryOperation;
import jonl.vmath.MathUtil;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class Geometry extends BaseGeometry {
    
    private boolean staticData = true;
    private boolean changed = true;
    
    boolean instancedSet = false;
    
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
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.indices = indices;
    }
    
    /** Sets whether this mesh will free its mesh data after it is loaded into the GL */
    public void setStatic(boolean staticData) {
        this.staticData = staticData;
    }
    
    public boolean isStatic() {
        return staticData;
    }
    boolean isChanged() {
        return changed;
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
    
    float[] getTangents() {
        return tangents;
    }
    float[] getBiTangents() {
        return bitangents;
    }
    
    public void setVertices(float[] v) {
        changed = true;
        vertices = v;
    }
    public void setNormals(float[] n) {
        changed = true;
        normals = n;
    }
    public void setTexCoords(float[] t) {
        changed = true;
        texCoords = t;
    }
    public void setIndices(int[] i) {
        changed = true;
        indices = i;
    }
    
    void setVerticesNull() {
        vertices = null;
    }
    void setNormalsNull() {
        normals = null;
    }
    void setTexCoordsNull() {
        texCoords = null;
    }
    void setIndicesNull() {
        indices = null;
    }
    
    void setTangentsNull() {
        tangents = null;
    }
    void setBiTangentsNull() {
        bitangents = null;
    }
    
    boolean isVerticesNull() {
        return vertices==null;
    }
    boolean isNormalsNull() {
        return normals==null;
    }
    boolean isTexCoordsNull() {
        return texCoords==null;
    }
    boolean isIndicesNull() {
        return indices==null;
    }
    
    /**
     * 
     * @return a read-only copy of the vertex data
     */
    public Vector3[] getVertexArray() {
        return MathUtil.getVectorArray(vertices,new Vector3(),new Vector3[0]);
    }
    
    public void setVectorArray(Vector3[] array) {
        changed = true;
        vertices = MathUtil.getFloatArray(array);
    }
    
    public Vector2[] getTexCoordArray() {
        return MathUtil.getVectorArray(texCoords,new Vector2(),new Vector2[0]);
    }
    
    public void setTexCoordArray(Vector2[] array) {
        changed = true;
        texCoords = MathUtil.getFloatArray(array);
    }
    
    void overrideChanged() {
        changed = false;
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
