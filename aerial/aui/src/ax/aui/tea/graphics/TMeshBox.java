package ax.aui.tea.graphics;

import ax.commons.structs.FloatArray;
import ax.commons.structs.IntArray;
import ax.graphics.GL;
import ax.graphics.Mesh;
import ax.graphics.utils.MeshData;
import ax.math.vector.Mathf;

public class TMeshBox {

    /** Number of points that define a curve edge corner */
    private static final int PRECISION = 16;
    
    /*
     * The Box Mesh:
     * 
     * Total number of vertices = (CORNERS + EDGES + INNER) = (PRECISION*4 + 2*4 + 1)
     * 
     * A box is usually created with the arguments: width,height,radius
     * A radius should not be greater than width/2 or height/2
     * 
     * 
     * 
     */
    
    // This box mesh will have edge vertices that can be adjusted to be rounded
    public static MeshData createBoxMeshData(float width, float height, float radius) {
        
        int numVerts = 1 + 2*4 + PRECISION*4;
        
        float r = radius;
        float w = width;
        float h = height;
        float hw = width*0.5f; //half-width
        float hh = height*0.5f; //half-height
        r = Mathf.min(r,hw,hh);
        
        float bw = hw - r; //inner width
        float bh = hh - r; //inner height
        
        float ai = Mathf.PI_OVER_2 / (PRECISION+1);
        
        FloatArray vertices = new FloatArray(3 * numVerts);
        FloatArray normals = new FloatArray(3 * numVerts);
        FloatArray texCoords = new FloatArray(2 * numVerts);
        IntArray indices = new IntArray(3 * (numVerts-1));
        
        // --------------------------------------------------------------------
        
        //Center
        vertices.put(0, 0, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(0,0,hw,hh));
        
        //Top-right
        vertices.put(bw, h, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(bw,h,hw,hh));
        
        //Top-left
        vertices.put(-bw, h, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(-bw,h,hw,hh));
        
        //Top-left corner
        {
            float angle = Mathf.PI_OVER_2;
            for (int i=0; i<PRECISION; i++) {
                angle += ai;
                float x = r*Mathf.cos(angle) - bw;
                float y = r*Mathf.sin(angle) + bh;
                vertices.put(x, y, 0);
                normals.put(0, 0, 1);
                texCoords.put(tc(x,y,hw,hh));
            }
        }
        
        //Left-top
        vertices.put(-w, bh, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(-w,bh,hw,hh));
        
        //Left-bottom
        vertices.put(-w, bh, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(-w,bh,hw,hh));
        
        //Bottom-left corner
        {
            float angle = Mathf.PI;
            for (int i=0; i<PRECISION; i++) {
                angle += ai;
                float x = r*Mathf.cos(angle) - bw;
                float y = r*Mathf.sin(angle) - bh;
                vertices.put(x, y, 0);
                normals.put(0, 0, 1);
                texCoords.put(tc(x,y,hw,hh));
            }
        }
        
        //Bottom-left
        vertices.put(-bw, -h, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(-bw,-h,hw,hh));
        
        //Bottom-right
        vertices.put(bw, -h, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(bw,-h,hw,hh));
        
        //Bottom-right corner
        {
            float angle = Mathf.THREE_PI_OVER_2;
            for (int i=0; i<PRECISION; i++) {
                angle += ai;
                float x = r*Mathf.cos(angle) + bw;
                float y = r*Mathf.sin(angle) - bh;
                vertices.put(x, y, 0);
                normals.put(0, 0, 1);
                texCoords.put(tc(x,y,hw,hh));
            }
        }
        
        //Right-bottom
        vertices.put(w, -bh, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(w,-bh,hw,hh));
        
        //Right-top
        vertices.put(w, bh, 0);
        normals.put(0, 0, 1);
        texCoords.put(tc(w,bh,hw,hh));
        
        //Top-right corner
        {
            float angle = 0;
            for (int i=0; i<PRECISION; i++) {
                angle += ai;
                float x = r*Mathf.cos(angle) + bw;
                float y = r*Mathf.sin(angle) + bh;
                vertices.put(x, y, 0);
                normals.put(0, 0, 1);
                texCoords.put(tc(x,y,hw,hh));
            }
        }
        
        // --------------------------------------------------------------------
        
        for (int i=1; i<numVerts; i++) {
            int next = (i==numVerts-1) ? 1 : i+1;
            indices.put(0,i,next);
        }
        
        MeshData md = new MeshData(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray());
        return md;
    }
    
    private static float[] tc(float x, float y, float hw, float hh) {
        float nx = x/hw;
        float ny = y/hh;
        nx = nx*0.5f + 0.5f;
        ny = ny*0.5f + 0.5f;
        ny = 1 - ny;
        return new float[]{nx,ny};
    }
    
    public static Mesh createBoxMesh(GL gl, float width, float height, float radius) {
        
        MeshData md = createBoxMeshData(width,height,radius);
        
        return gl.glGenMesh(md);
    }
    
    public static void adjust(Mesh mesh, float width, float height, float radius) {
        
        MeshData md = createBoxMeshData(width,height,radius);
        
        mesh.setVertexAttrib(md.vertexData,3);
        mesh.setTexCoordAttrib(md.texCoordData,2);
        
    }
    
    
}
