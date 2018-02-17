package jonl.ge.core.geometry;

import jonl.jutils.structs.FloatArray;
import jonl.jutils.structs.IntList;
import jonl.vmath.Mathf;
import jonl.vmath.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/geometries/TorusGeometry.js

public class TorusGeometry extends Geometry {
    
    private FloatArray vertices;
    private FloatArray normals;
    private FloatArray texCoords;
    private IntList indices;
    
    public TorusGeometry() {
        this(0.5f);
    }
    
    public TorusGeometry(float radius) {
        this(radius, radius*0.5f);
    }
    
    public TorusGeometry(float radius, float tube) {
    	this(radius,tube,32,32);
    }
    
    public TorusGeometry(float radius, float tube, int radialSegments, int tubularSegments) {
        this(radius,tube,radialSegments,tubularSegments,Mathf.TWO_PI);
    }
    
    public TorusGeometry(float radius, float tube, int radialSegments, int tubularSegments, float arc) {
        
        int numVerts = (radialSegments+1) * (tubularSegments+1);
        
        vertices = new FloatArray(3 * numVerts);
        normals = new FloatArray(3 * numVerts);
        texCoords = new FloatArray(2 * numVerts);
        indices = new IntList();
        
        Vector3 center = new Vector3();
        Vector3 vertex = new Vector3();
        Vector3 normal = new Vector3();

        // recalculate radius and tube
        radius = radius * 0.75f;
        tube = tube * 0.5f;
        
    	int j, i;

    	// generate vertices, normals and uvs

    	for ( j = 0; j <= radialSegments; j ++ ) {

    		for ( i = 0; i <= tubularSegments; i ++ ) {

    			float u = i / (float)tubularSegments * arc;
    			float v = j / (float)radialSegments * Mathf.TWO_PI;

    			// vertex

    			vertex.x = ( radius + tube * Mathf.cos( v ) ) * Mathf.cos( u );
    			vertex.y = ( radius + tube * Mathf.cos( v ) ) * Mathf.sin( u );
    			vertex.z = tube * Mathf.sin( v );

    			vertices.put( vertex.x, vertex.y, vertex.z );

    			// normal

    			center.x = radius * Mathf.cos( u );
    			center.y = radius * Mathf.sin( u );
    			normal = Mathf.sub( vertex, center ).normalize();

    			normals.put( normal.x, normal.y, normal.z );

    			// uv

    			texCoords.put( i / (float)tubularSegments );
    			texCoords.put( j / (float)radialSegments );

    		}

    	}

    	// generate indices

    	for ( j = 1; j <= radialSegments; j ++ ) {

    		for ( i = 1; i <= tubularSegments; i ++ ) {

    			// indices

    			int a = ( tubularSegments + 1 ) * j + i - 1;
    			int b = ( tubularSegments + 1 ) * ( j - 1 ) + i - 1;
    			int c = ( tubularSegments + 1 ) * ( j - 1 ) + i;
    			int d = ( tubularSegments + 1 ) * j + i;

    			// faces

    			indices.put( a, b, d );
    			indices.put( b, c, d );

    		}

    	}
        
        set(vertices.getArray(),normals.getArray(),texCoords.getArray(),indices.toArray());
        
    }
    
    
}
