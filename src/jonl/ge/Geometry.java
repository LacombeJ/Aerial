package jonl.ge;

import jonl.vmath.MathUtil;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class Geometry {
    
	//TODO simply this geometry class (complex geometries can be extended to another class)
	//Find better way for handling static/dynamic data and keeping or removing vertices on gl_mesh creation
	
    private float[] vertices;
    private float[] normals;
    private float[] texCoords;
    private int[] indices;
    
    //Tangents and bitangents are not public and are handled by the AppRenderer currently
    private float[] tangents;
    private float[] bitangents;
    
    private boolean calculateTangents = true;
    private boolean staticData = true;
    private boolean changed = true;
    
    boolean instancedSet = false;
    
    Geometry() {
        
    }
    
    Geometry(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
        set(vertices,normals,texCoords,indices);
    }
    
    public Geometry(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }
    
    public Geometry(Vector3[] vertices, int[] indices) {
        this(MathUtil.getFloatArray(vertices),indices);
    }
    
    void set(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
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
    
    protected void setCalculateTangents(boolean calculate) {
    	calculateTangents = calculate;
    }
    
    boolean shouldCalculateTangents() {
    	return calculateTangents;
    }
    
    // https://learnopengl.com/#!Advanced-Lighting/Normal-Mapping
    //TODO calculate smooth tangents instead of flat tangents?
    void calculateTangents() {
        if (vertices!=null && normals!=null && texCoords!=null && calculateTangents) {
            tangents = new float[vertices.length];
            bitangents = new float[vertices.length];
            for (int i=0; i<indices.length; i+=3) {
                int a = indices[i]*3;
                int b = indices[i+1]*3;
                int c = indices[i+2]*3;
                Vector3 vertexA = new Vector3(vertices[a],vertices[a+1],vertices[a+2]);
                Vector3 vertexB = new Vector3(vertices[b],vertices[b+1],vertices[b+2]);
                Vector3 vertexC = new Vector3(vertices[c],vertices[c+1],vertices[c+2]);
                
                int ta = indices[i]*2;
                int tb = indices[i+1]*2;
                int tc = indices[i+2]*2;
                Vector2 texCoordA = new Vector2(texCoords[ta],texCoords[ta+1]);
                Vector2 texCoordB = new Vector2(texCoords[tb],texCoords[tb+1]);
                Vector2 texCoordC = new Vector2(texCoords[tc],texCoords[tc+1]);
                
                Vector3 edge1 = Mathf.sub(vertexB,vertexA);
                Vector3 edge2 = Mathf.sub(vertexC,vertexA);
                Vector2 deltaUV1 = Mathf.sub(texCoordB,texCoordA);
                Vector2 deltaUV2 = Mathf.sub(texCoordC,texCoordA);
                
                float f =  1f / Vector2.cross(deltaUV1,deltaUV2);
                
                Vector3 tangent = new Vector3();
                tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
                tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
                tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
                tangent.normalize();
                
                Vector3 bitangent = new Vector3();
                bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
                bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
                bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
                bitangent.normalize();
                
                tangents[a  ] = tangent.x;
                tangents[a+1] = tangent.y;
                tangents[a+2] = tangent.z;
                tangents[b  ] = tangent.x;
                tangents[b+1] = tangent.y;
                tangents[b+2] = tangent.z;
                tangents[c  ] = tangent.x;
                tangents[c+1] = tangent.y;
                tangents[c+2] = tangent.z;
                
                bitangents[a  ] = bitangent.x;
                bitangents[a+1] = bitangent.y;
                bitangents[a+2] = bitangent.z;
                bitangents[b  ] = bitangent.x;
                bitangents[b+1] = bitangent.y;
                bitangents[b+2] = bitangent.z;
                bitangents[c  ] = bitangent.x;
                bitangents[c+1] = bitangent.y;
                bitangents[c+2] = bitangent.z;
            }
        }
    }
    
}
