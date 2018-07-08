package ax.engine.core.geometry;

import ax.commons.structs.FloatArray;
import ax.commons.structs.IntArray;
import ax.engine.core.Geometry;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/BoxGeometry.js

public class PlaneOutlineGeometry extends Geometry {
    
    public PlaneOutlineGeometry() {
        this(1,1);
    }
    
    public PlaneOutlineGeometry(float width, float height) {
        
        int numVerts = 4;
        
        FloatArray vertices = new FloatArray(3 * numVerts);
        FloatArray normals = new FloatArray(3 * numVerts);
        FloatArray texCoords = new FloatArray(2 * numVerts);
        IntArray indices = new IntArray(2 * numVerts);
        
        float width_half = width / 2f;
        float height_half = height / 2f;

        int ix, iy;

        // generate vertices, normals and uvs

        for ( iy = 0; iy < 2; iy ++ ) {

            float y = iy * height - height_half;

            for ( ix = 0; ix < 2; ix ++ ) {

                float x = ix * width - width_half;

                vertices.put( x, - y, 0 );

                normals.put( 0, 0, 1 );

                texCoords.put( ix );
                texCoords.put( 1 - ( iy ) );

            }

        }

        // indices

        indices.put( 0, 1 );
        indices.put( 1, 3 );
        indices.put( 3, 2 );
        indices.put( 2, 0 );
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.getArray());
        
    }
    
    
}
