package jonl.ge;

import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntArray;
import jonl.jutils.structs.IntList;
import jonl.vmath.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/BoxGeometry.js

public class PlaneGeometry extends Geometry {
    
    private FloatArray vertices;
    private FloatArray normals;
    private FloatArray texCoords;
    private IntArray indices;
    
    public PlaneGeometry() {
        this(1,1);
    }
    
    public PlaneGeometry(float width, float height) {
        this(width,height,1,1);
    }
    
    public PlaneGeometry(float width, float height, int widthSegments, int heightSegments) {
        
        int numVerts = (widthSegments+1) * (heightSegments+1);
        
        vertices = new FloatArray(3 * numVerts);
        normals = new FloatArray(3 * numVerts);
        texCoords = new FloatArray(2 * numVerts);
        indices = new IntArray(6 * widthSegments * heightSegments);
        
        float width_half = width / 2f;
        float height_half = height / 2f;

        int gridX = widthSegments;
        int gridY = heightSegments;

        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        float segment_width = width / gridX;
        float segment_height = height / gridY;

        int ix, iy;

        // generate vertices, normals and uvs

        for ( iy = 0; iy < gridY1; iy ++ ) {

            float y = iy * segment_height - height_half;

            for ( ix = 0; ix < gridX1; ix ++ ) {

                float x = ix * segment_width - width_half;

                vertices.put( x, - y, 0 );

                normals.put( 0, 0, 1 );

                texCoords.put( ix / (float)gridX );
                texCoords.put( 1 - ( iy / (float)gridY ) );

            }

        }

        // indices

        for ( iy = 0; iy < gridY; iy ++ ) {

            for ( ix = 0; ix < gridX; ix ++ ) {

                int a = ix + gridX1 * iy;
                int b = ix + gridX1 * ( iy + 1 );
                int c = ( ix + 1 ) + gridX1 * ( iy + 1 );
                int d = ( ix + 1 ) + gridX1 * iy;

                // faces

                indices.put( a, b, d );
                indices.put( b, c, d );

            }

        }
        
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray());
        
    }
    
    
}
