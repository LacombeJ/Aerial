package jonl.ge;

import jonl.vmath.MathUtil;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class Mesh {
    
    public float[] vertices;
    public float[] normals;
    public float[] texCoords;
    public int[] indices;
    
    //Tangents and bitangents are not public and are handled by the AppRenderer currently
    float[] tangents;
    float[] bitangents;
    
    boolean staticData = true;
    boolean changed = true;
    
    Mesh() {
        
    }
    
    public Mesh(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }
    
    public Mesh(Vector3[] vertices, int[] indices) {
        this(MathUtil.getFloatArray(vertices),indices);
    }
    
    /** Sets whether this mesh will free its mesh data after it is loaded into the GL */
    public void setStatic(boolean staticData) {
        this.staticData = staticData;
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
        this.vertices = MathUtil.getFloatArray(array);
    }
    
    void calculateTangents() {
        if (vertices!=null && normals!=null && texCoords!=null) {
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
    
    // https://learnopengl.com/#!Advanced-Lighting/Normal-Mapping
    //TODO calculate smooth tangents instead of flat tangents?
    void calculateTangents2() {
        if (vertices!=null && normals!=null && texCoords!=null) {
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
