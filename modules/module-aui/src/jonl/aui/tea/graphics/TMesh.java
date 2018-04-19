package jonl.aui.tea.graphics;

import jonl.jgl.utils.MeshData;
import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntArray;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

public class TMesh {

    public final MeshData data;
    
    TMesh(MeshData data) {
        this.data = data;
    }
    
    public final static TMesh LINE;
    public final static TMesh BOX;
    public final static TMesh BOX_OUTLINE;
    public final static TMesh CIRCLE;
    public final static TMesh CIRCLE_OUTLINE;

    static {
        // Line
        {
            Vector3 v0 = new Vector3(0,0,0);
            Vector3 v1 = new Vector3(1,0,0);
            int[] indices = { 0, 1 };
            float[] vertices = Vector3.pack(v0, v1);
            LINE = new TMesh(new MeshData(vertices,null,null,indices));
        }
        // BOX
        {
            float d = 0.5f;
            
            Vector3 v0 = new Vector3(-d, -d, 0);
            Vector3 v1 = new Vector3(+d, -d, 0);
            Vector3 v2 = new Vector3(+d, +d, 0);
            Vector3 v3 = new Vector3(-d, +d, 0);
            
            Vector3 n = new Vector3(0, 0, -1);
            
            Vector2 t0 = new Vector2(0, 1);
            Vector2 t1 = new Vector2(1, 1);
            Vector2 t2 = new Vector2(1, 0);
            Vector2 t3 = new Vector2(0, 0);
            
            float[] vertices = Vector3.pack(v0, v1, v2, v3);
            float[] normals = Vector3.pack(n, n, n, n);
            float[] texCoords = Vector2.pack(t0, t1, t2, t3);
            int[] indices = { 0, 1, 2, 0, 2, 3 };
            int[] outline = { 0, 1, 2, 3, 0 };
            BOX = new TMesh(new MeshData(vertices,normals,texCoords,indices));
            BOX_OUTLINE = new TMesh(new MeshData(vertices,normals,texCoords,outline));
        }
        
        // CIRCLE
        {
            float radius = 0.5f;
            int segments = 64;
            int numVerts = segments + 2;
            
            FloatArray vertices = new FloatArray(3 * numVerts);
            FloatArray normals = new FloatArray(3 * numVerts);
            FloatArray texCoords = new FloatArray(2 * numVerts);
            IntArray indices = new IntArray(3 * segments);
            IntArray outline = new IntArray(segments + 1);
            
            vertices.put( 0, 0, 0 );
            normals.put( 0, 0, 1 );
            texCoords.put( 0.5f, 0.5f );
            
            for (int s = 0, i = 3; s <= segments; s++, i+=3) {
                
                float segment = s / (float)segments * Mathf.TWO_PI;
                
                float vx = radius * Mathf.cos( segment );
                float vy = radius * Mathf.sin( segment );

                vertices.put( vx, vy, 0 );
                
                normals.put( 0, 0, 1 );
                
                float tx = ( vertices.get(i) / radius + 1 ) / 2f;
                float ty = ( vertices.get(i+1) / radius + 1 ) / 2f;

                texCoords.put( tx, ty );
                
                if (s!=0) {
                    indices.put( s, s + 1, 0 );
                    outline.put(s);
                }
            }
            outline.put(1); // Finish circle outline by coming back to first vertex (0 is center)
            
            CIRCLE = new TMesh(new MeshData(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray()));
            CIRCLE_OUTLINE = new TMesh(new MeshData(vertices.getArray(),normals.getArray(),texCoords.getArray(),outline.getArray()));
        }
    }

}
