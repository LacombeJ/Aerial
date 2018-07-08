package ax.engine.core.geometry;

import ax.commons.structs.FloatArray;
import ax.commons.structs.IntArray;
import ax.engine.core.Geometry;
import ax.math.vector.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/BoxGeometry.js

public class BoxGeometry extends Geometry {
    
    private static int X = 0;
    private static int Y = 1;
    private static int Z = 2;
    
    private class Variables {
        int numberOfVertices = 0;
    }
    
    public BoxGeometry() {
        this(1,1,1);
    }
    
    public BoxGeometry(float width, float height, float depth) {
        this(width,height,depth,1,1,1);
    }
    
    public BoxGeometry(float width, float height, float depth, int widthSegments, int heightSegments, int depthSegments) {
        
        int numVerts = 6 * 4 * widthSegments * heightSegments * depthSegments;
        
        FloatArray vertices = new FloatArray(3 * numVerts);
        FloatArray normals = new FloatArray(3 * numVerts);
        FloatArray texCoords = new FloatArray(2 * numVerts);
        IntArray indices = new IntArray(6 * 6 * widthSegments * heightSegments * depthSegments);
        
        Variables vars = new Variables();
        
        buildPlane( vars, Z, Y, X, - 1, - 1, depth, height,   width,  depthSegments, heightSegments, vertices, normals, texCoords, indices); // px
        buildPlane( vars, Z, Y, X,   1, - 1, depth, height, - width,  depthSegments, heightSegments, vertices, normals, texCoords, indices); // nx
        buildPlane( vars, X, Z, Y,   1,   1, width, depth,    height, widthSegments, depthSegments, vertices, normals, texCoords, indices);  // py
        buildPlane( vars, X, Z, Y,   1, - 1, width, depth,  - height, widthSegments, depthSegments, vertices, normals, texCoords, indices);  // ny
        buildPlane( vars, X, Y, Z,   1, - 1, width, height,   depth,  widthSegments, heightSegments, vertices, normals, texCoords, indices); // pz
        buildPlane( vars, X, Y, Z, - 1, - 1, width, height, - depth,  widthSegments, heightSegments, vertices, normals, texCoords, indices); // nz
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray());
        
    }
    
    private void buildPlane(Variables vars, int u, int v, int w, float udir, float vdir, float width, float height, float depth, int gridX, int gridY,
            FloatArray vertices, FloatArray normals, FloatArray texCoords, IntArray indices) {

        float segmentWidth = width / gridX;
        float segmentHeight = height / gridY;

        float widthHalf = width / 2f;
        float heightHalf = height / 2f;
        float depthHalf = depth / 2f;

        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        int vertexCounter = 0;
        
        int ix, iy;

        Vector3 vector = new Vector3();

        // generate vertices, normals and uvs

        for ( iy = 0; iy < gridY1; iy ++ ) {

            float y = iy * segmentHeight - heightHalf;

            for ( ix = 0; ix < gridX1; ix ++ ) {

                float x = ix * segmentWidth - widthHalf;
                
                // set values to correct vector component
                
                vector.set(u, x * udir);
                vector.set(v, y * vdir);
                vector.set(w, depthHalf);
                
                // now apply vector to vertex buffer
                vertices.put(vector.x, vector.y, vector.z);
                
                // set values to correct vector component
                
                vector.set(u, 0);
                vector.set(v, 0);
                vector.set(w, depth > 0 ? 1 : - 1);
                
                // now apply vector to normal buffer
                
                normals.put(vector.x, vector.y, vector.z);
                
                // uvs
                
                texCoords.put(ix / (float)gridX, 1 - ( iy / gridY ));
                
                // counters
                
                vertexCounter += 1;
                
            }

        }

        // indices

        // 1. you need three indices to draw a single face
        // 2. a single segment consists of two faces
        // 3. so we need to generate six (2*3) indices per segment

        for ( iy = 0; iy < gridY; iy ++ ) {

            for ( ix = 0; ix < gridX; ix ++ ) {

                int a = vars.numberOfVertices + ix + gridX1 * iy;
                int b = vars.numberOfVertices + ix + gridX1 * ( iy + 1 );
                int c = vars.numberOfVertices + ( ix + 1 ) + gridX1 * ( iy + 1 );
                int d = vars.numberOfVertices + ( ix + 1 ) + gridX1 * iy;

                // faces

                indices.put(a, b, d);
                
                indices.put(b, c, d);

            }

        }

        // update total number of vertices

        vars.numberOfVertices += vertexCounter;

}
    
    
    
}
