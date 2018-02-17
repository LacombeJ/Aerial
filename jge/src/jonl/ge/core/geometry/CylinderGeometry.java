package jonl.ge.core.geometry;

import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntArray;
import jonl.jutils.structs.IntArray2D;
import jonl.jutils.structs.IntList;
import jonl.vmath.Mathf;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/CylinderGeometry.js

public class CylinderGeometry extends Geometry {
    
    private FloatArray vertices;
    private FloatArray normals;
    private FloatArray texCoords;
    private IntList indices;
    
    private class Variables {
        int index = 0;
    }
    
    public CylinderGeometry() {
        this(0.5f,1f);
    }
    
    public CylinderGeometry(float radius, float height) {
        this(radius,radius,height);
    }
    
    public CylinderGeometry(float radiusTop, float radiusBottom, float height) {
        this(radiusTop,radiusBottom,height,32,1);
    }
    
    public CylinderGeometry(float radiusTop, float radiusBottom, float height, int radialSegments, int heightSegments) {
        this(radiusTop,radiusBottom,height,radialSegments,heightSegments,false,0,Mathf.TWO_PI);
    }
    
    public CylinderGeometry(float radiusTop, float radiusBottom, float height, int radialSegments, int heightSegments, boolean openEnded, float thetaStart, float thetaLength) {
        
        int numVerts = (heightSegments+1) * (radialSegments+1) + 2 * ( radialSegments + radialSegments + 1);
        
        vertices = new FloatArray(3 * numVerts);
        normals = new FloatArray(3 * numVerts);
        texCoords = new FloatArray(2 * numVerts);
        indices = new IntList();
        
        Variables vars = new Variables();
        
        float halfHeight = height / 2f;
        
        generateTorso(vars,radiusTop,radiusBottom,height,radialSegments,heightSegments,thetaStart,thetaLength);
        
        if (openEnded == false) {
            
            if ( radiusTop > 0 ) generateCap(vars, true, radiusTop, radiusBottom, halfHeight, radialSegments, thetaStart, thetaLength);
            if ( radiusBottom > 0) generateCap(vars, false, radiusTop, radiusBottom, halfHeight, radialSegments, thetaStart, thetaLength);
            
        }
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.toArray());
        
    }
    
    private void generateTorso(Variables vars, float radiusTop, float radiusBottom, float height, int radialSegments, int heightSegments, float thetaStart, float thetaLength) {

        IntArray2D indexArray = new IntArray2D(heightSegments+1);
        
        int x, y;
        Vector3 normal = new Vector3();
        Vector3 vertex = new Vector3();

        // this will be used to calculate the normal
        float slope = ( radiusBottom - radiusTop ) / height;
        
        // generate vertices, normals and uvs

        for ( y = 0; y <= heightSegments; y ++ ) {

            IntArray indexRow = new IntArray(radialSegments+1);

            float v = y / (float)heightSegments;

            // calculate the radius of the current row

            float radius = v * ( radiusBottom - radiusTop ) + radiusTop;

            for ( x = 0; x <= radialSegments; x ++ ) {

                float u = x / (float)radialSegments;

                float theta = u * thetaLength + thetaStart;

                float sinTheta = Mathf.sin( theta );
                float cosTheta = Mathf.cos( theta );

                // vertex

                vertex.x = radius * sinTheta;
                vertex.y = - v * height + (height / 2f);
                vertex.z = radius * cosTheta;
                vertices.put( vertex.x, vertex.y, vertex.z );

                // normal

                normal.set( sinTheta, slope, cosTheta );
                normal.normalize();
                normals.put( normal.x, normal.y, normal.z );

                // uv

                texCoords.put( u, 1 - v );

                // save index of vertex in respective row

                indexRow.put( vars.index ++ );

            }

            // now save vertices of the row in our index array

            indexArray.put( indexRow );

        }

        int[][] indexArrayA = indexArray.getArray();
        
        // generate indices

        for ( x = 0; x < radialSegments; x ++ ) {

            for ( y = 0; y < heightSegments; y ++ ) {

                // we use the index array to access the correct indices

                int a = indexArrayA[ y ][ x ];
                int b = indexArrayA[ y + 1 ][ x ];
                int c = indexArrayA[ y + 1 ][ x + 1 ];
                int d = indexArrayA[ y ][ x + 1 ];

                // faces

                indices.put( a, b, d );
                indices.put( b, c, d );

            }

        }

    }

    void generateCap(Variables scope, boolean top, float radiusTop, float radiusBottom, float halfHeight, float radialSegments, float thetaStart, float thetaLength) {

        int x, centerIndexStart, centerIndexEnd;

        Vector2 uv = new Vector2();
        Vector3 vertex = new Vector3();

        float radius = ( top == true ) ? radiusTop : radiusBottom;
        int sign = ( top == true ) ? 1 : - 1;

        // save the index of the first center vertex
        centerIndexStart = scope.index;

        // first we generate the center vertex data of the cap.
        // because the geometry needs one set of uvs per face,
        // we must generate a center vertex per face/segment

        for ( x = 1; x <= radialSegments; x ++ ) {

            // vertex

            vertices.put( 0, halfHeight * sign, 0 );

            // normal

            normals.put( 0, sign, 0 );

            // uv

            texCoords.put( 0.5f, 0.5f );

            // increase index

            scope.index ++;

        }

        // save the index of the last center vertex

        centerIndexEnd = scope.index;

        // now we generate the surrounding vertices, normals and uvs

        for ( x = 0; x <= radialSegments; x ++ ) {

            float u = x / radialSegments;
            float theta = u * thetaLength + thetaStart;

            float cosTheta = Mathf.cos( theta );
            float sinTheta = Mathf.sin( theta );

            // vertex

            vertex.x = radius * sinTheta;
            vertex.y = halfHeight * sign;
            vertex.z = radius * cosTheta;
            vertices.put( vertex.x, vertex.y, vertex.z );

            // normal

            normals.put( 0, sign, 0 );

            // uv

            uv.x = ( cosTheta * 0.5f ) + 0.5f;
            uv.y = ( sinTheta * 0.5f * sign ) + 0.5f;
            texCoords.put( uv.x, uv.y );

            // increase index

            scope.index ++;

        }

        // generate indices

        for ( x = 0; x < radialSegments; x ++ ) {

            int c = centerIndexStart + x;
            int i = centerIndexEnd + x;

            if ( top == true ) {

                // face top

                indices.put( i, i + 1, c );

            } else {

                // face bottom

                indices.put( i + 1, i, c );

            }

        }
        

    }
    
    
}
