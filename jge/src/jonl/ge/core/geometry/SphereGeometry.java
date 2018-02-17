package jonl.ge.core.geometry;

import jonl.ge.core.Geometry;
import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntArray;
import jonl.jutils.structs.IntArray2D;
import jonl.jutils.structs.IntList;
import jonl.vmath.Mathf;
import jonl.vmath.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/SphereGeometry.js

public class SphereGeometry extends Geometry {
    
    private FloatArray vertices;
    private FloatArray normals;
    private FloatArray texCoords;
    private IntList indices;
    
    public SphereGeometry() {
        this(0.5f);
    }
    
    public SphereGeometry(float radius) {
        this(radius,32,32);
    }
    
    public SphereGeometry(float radius, int widthSegments, int heightSegments) {
        this(radius,widthSegments,heightSegments, 0, Mathf.TWO_PI, 0, Mathf.PI);
    }
    
    public SphereGeometry(float radius, int widthSegments, int heightSegments, float phiStart, float phiLength, float thetaStart, float thetaLength) {
        
        int numVerts = (widthSegments+1) * (heightSegments+1);
        
        vertices = new FloatArray(3 * numVerts);
        normals = new FloatArray(3 * numVerts);
        texCoords = new FloatArray(2 * numVerts);
        indices = new IntList();
        
        float thetaEnd = thetaStart + thetaLength;
        
        int ix, iy;
        
        int index = 0;
        IntArray2D grid = new IntArray2D(heightSegments+1);
        
        Vector3 vertex = new Vector3();
        Vector3 normal = new Vector3();
        
        for ( iy = 0; iy <= heightSegments; iy ++ ) {

            IntArray verticesRow = new IntArray(widthSegments+1);

            float v = iy / (float)heightSegments;

            for ( ix = 0; ix <= widthSegments; ix ++ ) {

                float u = ix / (float)widthSegments;

                // vertex

                vertex.x = - radius * Mathf.cos( phiStart + u * phiLength ) * Mathf.sin( thetaStart + v * thetaLength );
                vertex.y = radius * Mathf.cos( thetaStart + v * thetaLength );
                vertex.z = radius * Mathf.sin( phiStart + u * phiLength ) * Mathf.sin( thetaStart + v * thetaLength );

                vertices.put( vertex.x, vertex.y, vertex.z );
                
                // normal

                normal.set( vertex.x, vertex.y, vertex.z );
                normal.normalize();
                normals.put( normal.x, normal.y, normal.z );

                // uv

                texCoords.put( u, 1 - v );

                verticesRow.put( index ++ );

            }

            grid.put( verticesRow );

        }

        // indices
        
        int[][] gridA = grid.getArray();
        
        for ( iy = 0; iy < heightSegments; iy ++ ) {

            for ( ix = 0; ix < widthSegments; ix ++ ) {

                int a = gridA[ iy ][ ix + 1 ];
                int b = gridA[ iy ][ ix ];
                int c = gridA[ iy + 1 ][ ix ];
                int d = gridA[ iy + 1 ][ ix + 1 ];

                if ( iy != 0 || thetaStart > 0 ) {
                    indices.put( a, b, d );
                }
                if ( iy != heightSegments - 1 || thetaEnd < Math.PI ) {
                    indices.put( b, c, d );
                }

            }

        }
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.toArray());
        
    }
    
    
}
